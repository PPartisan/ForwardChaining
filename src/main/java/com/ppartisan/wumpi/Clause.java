package com.ppartisan.wumpi;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

class Clause {

    private final List<Literal> literals;

    Clause(List<Literal> literals) {
        this.literals = literals;
    }

    static Clause clause(Literal... literals) {
        if (literals == null || literals.length == 0)
            return new Clause(List.of());
        return new Clause(List.of(literals));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    Literal getFactOrThrow() {
        return getFact().get();
    }

    Optional<Literal> getFact() {
        return literals.stream().filter(Literal::isPositive).findFirst();
    }

    boolean isInitialFact() {
        return literals.size() == 1 && literals.get(0).isPositive();
    }

    boolean canInfer(Set<Literal> model) {
        return literals.stream()
                .filter(Literal::isNegated)
                .map(Literal::toLit)
                .allMatch(model::contains);
    }

    boolean hasOnlyOnePositiveLiteral() {
        return literals.stream().filter(Literal::isPositive).count() == 1;
    }

    static Predicate<Clause> satisfied(Set<Literal> model) {
        return clause -> clause.containsConsequent(model) || clause.doesNotContainAntecedent(model);
    }

    private boolean containsConsequent(Set<Literal> model) {
        return literals.stream().anyMatch(it -> it.isPositive() && model.contains(it));
    }

    private boolean doesNotContainAntecedent(Set<Literal> model) {
        return literals.stream().anyMatch(it -> it.isNegated() && !model.contains(it));
    }

    @Override
    public String toString() {
        final int size = literals.size();
        final String antecedent = literals.stream()
                .limit(size - 1)
                .map(String::valueOf)
                .collect(joining(" ∧ "));
        final String consequent = literals.get(size - 1).toString();
        return String.format("%s ⇒ %s", antecedent, consequent);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(literals, clause.literals);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(literals);
    }
}
