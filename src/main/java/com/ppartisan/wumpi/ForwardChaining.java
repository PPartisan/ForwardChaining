package com.ppartisan.wumpi;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static com.ppartisan.wumpi.Clause.satisfied;
import static com.ppartisan.wumpi.LiteralName.*;
import static com.ppartisan.wumpi.Util.collect;
import static java.lang.System.out;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

final class ForwardChaining {

    private final List<Clause> clauses;
    final Set<Literal> model = new HashSet<>();

    private ForwardChaining(List<Clause> clauses) {
        this.clauses = clauses;
    }

    static ForwardChaining withClauses(Clause... clauses) {
        return new ForwardChaining((clauses == null || clauses.length == 0) ? List.of() : List.of(clauses));
    }

    /**
     * Version of forward chaining algorithm.
     *
     * @return A message concerning the wumpus location.
     */
    String forwardChaining() {

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
                    .peek(clause -> out.println("Clause: " + clause))
                    .filter(clause -> clause.canInfer(model))
                    .forEach(addUnprocessedConsequentToModel(fixPoint));
        }

        // Check if all clauses are satisfied
        if (!clauses.stream().allMatch(satisfied(model)))
            return "No models satisfy all clauses simultaneously.";

        out.println();
        out.println("Model: ");
        model.stream()
                .sorted(Comparator.comparing(Literal::name))
                .map(literal -> String.format("Variable %s = %s", literal, model.contains(literal)))
                .forEach(out::println);

        return wumpusLocationMsg();
    }

    private String wumpusLocationMsg() {
        final List<Literal> wumpi = wumpi();
        if(wumpi.size() > 1)
            return "More than one possible wumpus location.";
        else if (wumpi.isEmpty())
            return "Could not find wumpus";
        else
            return String.format("Wumpus location is %s", wumpi.get(0).coordinates());
    }

    private List<Literal> wumpi() {
        final Map<Coordinates, Set<LiteralName>> rooms = rooms();
        // Todo - Declaring "safe" rooms in this way makes the program less flexible. It may be possible to achieve
        // the same outcome with specifying lots of clauses and facts.
        final Set<LiteralName> safe = EnumSet.of(CLEAN, SAFE);
        return model.stream()
                .filter(it -> it.name() == WUMPUS)
                .filter(it -> rooms.get(it.coordinates()).stream().noneMatch(safe::contains))
                .collect(toList());
    }

    private Map<Coordinates, Set<LiteralName>> rooms() {
        return model.stream().collect(groupingBy(Literal::coordinates, collect()));
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
