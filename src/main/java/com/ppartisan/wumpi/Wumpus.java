package com.ppartisan.wumpi;

import java.util.Map;

import static java.util.Map.entry;

public class Wumpus {

    private static final Map<Integer, String> CODES = Map.ofEntries(
        entry(1, "Safe_1"),
        entry(2, "Safe_2"),
        entry(3, "Safe_3"),
        entry(4, "Safe_4"),
        entry(5, "Safe_5"),
        entry(6, "Safe_6"),
        entry(11, "Stench_1"),
        entry(12, "Stench_2"),
        entry(13, "Stench_3"),
        entry(14, "Stench_4"),
        entry(15, "Stench_5"),
        entry(16, "Stench_6"),
        entry(21, "Wumpus_1"),
        entry(22, "Wumpus_2"),
        entry(23, "Wumpus_3"),
        entry(24, "Wumpus_4"),
        entry(25, "Wumpus_5"),
        entry(26, "Wumpus_6")
    );

    public static void main(String[] args) {
        final ForwardChaining fc = new ForwardChaining();
        // 1,1 is safe
        fc.addClause(1);
        // 1,2 is safe
        fc.addClause(2);
        // 2,2 is safe
        fc.addClause(4);
        // 1,1 has stench
        fc.addClause(11);
        // 2,2 has stench
        fc.addClause(14);
        // If 1,1 has stench and 2,2 has stench then Wumpus in 2,1
        fc.addClause(-11, -14, 23);

        // run forward chaining
        fc.forwardChaining(CODES);
    }
}