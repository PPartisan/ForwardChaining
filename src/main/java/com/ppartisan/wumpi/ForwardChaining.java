package com.ppartisan.wumpi;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.ppartisan.wumpi.Clause.satisfied;
import static java.lang.System.out;
import static java.util.function.Predicate.not;

final class ForwardChaining {

    private final List<Clause> clauses;
    private final Set<Literal> model = new HashSet<>();

    private ForwardChaining(List<Clause> clauses) {
        this.clauses = clauses;
    }

    static ForwardChaining withClauses(Clause... clauses) {
        return new ForwardChaining((clauses == null || clauses.length == 0) ? List.of() : List.of(clauses));
    }

    /**
     * Version of forward chaining algorithm.
     *
     * @return {@code true} if a model satisfies all clauses.
     */
    boolean forwardChaining() {

        // All symbols set to false initially.
        model.clear();

        if (!clauses.stream().allMatch(Clause::hasOnlyOnePositiveLiteral))
            throw new AssertionError("At most one positive literal is allowed in each clause.");

        // Initialize model with unit clauses
        withFacts(fact -> {
            model.add(fact);
            out.printf("Initial fact: %s\n", fact);
        });

        // Iterate through set of clauses applying modus ponens until we reach a fixpoint.
        final AtomicBoolean fixPoint = new AtomicBoolean(false);
        while (!fixPoint.get()) {
            fixPoint.set(true);
            clauses.stream()
                    .filter(not(Clause::isInitialFact))
                    .filter(clause -> clause.allTrue(model))
                    .forEach(addUnprocessedConsequentToModel(fixPoint));
        }

        // Check if all clauses are satisfied
        if (!clauses.stream().allMatch(satisfied(model))) {
            out.println("No models satisfy all clauses simultaneously.");
            return false;
        }

        out.println();
        out.println("Model: ");
        model.stream()
                .sorted(Comparator.comparing(Literal::name))
                .map(literal -> String.format("Variable %s = %s", literal.name(), model.contains(literal)))
                .forEach(out::println);

        return true;
    }

    private Consumer<Clause> addUnprocessedConsequentToModel(AtomicBoolean fixPoint) {
        return clause -> clause.getFact()
                .filter(not(model::contains))
                .ifPresent(consequent -> {
                    model.add(consequent);
                    fixPoint.set(false);
                    out.printf("Inferred %s from %s\n", consequent, clause);
                });
    }


    private void withFacts(Consumer<Literal> onEachFact) {
        clauses.stream()
                .filter(Clause::isInitialFact)
                .map(Clause::getFactOrThrow)
                .forEach(onEachFact);
    }

}
