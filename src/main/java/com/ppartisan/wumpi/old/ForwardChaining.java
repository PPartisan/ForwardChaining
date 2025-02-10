package com.ppartisan.wumpi.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

class ForwardChaining {
    ArrayList<int[]> clauses;

    public ForwardChaining() {
        clauses=new ArrayList<>();
    }

    public boolean forwardChaining(Map<Integer, String> codes) {
        // Simplified version of forward chaining algorithm: does not follow the 
        // textbook. This implementation does not run in linear time because it 
        // scans all clauses multiple times. 

        final int n = codes.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1);
        boolean[] model=new boolean[n+1];    // All symbols set to false initially.

        //  Sanity check clauses
        for(int i=0; i<clauses.size(); i++) {
            int posLits=0;
            int[] clause=clauses.get(i);
            for (int k : clause) {
                assert clause[i] <= n && clause[i] >= -n : "Found reference to variable larger than n.";
                if (k > 0) {
                    posLits++;
                }
            }

            assert posLits<=1 : "At most one positive literal is allowed in each clause.";
        }

        //  Iterate through set of clauses applying modus ponens until we reach a 
        //  fixpoint.
        boolean fixPoint=false;

        while(!fixPoint) {
            fixPoint=true;

            for (int[] clause : clauses) {
                //  Check all symbols that appear negated in this clause.
                //  If all true, then apply modus ponens. 

                boolean allTrue = true;

                for (int i : clause) {
                    if (i < 0 && !model[-i]) {
                        allTrue = false;
                        break;
                    }
                }

                if (allTrue) {
                    boolean goalClause = true;
                    for (int i : clause) {
                        if (i > 0) {
                            goalClause = false;
                            if (!model[i]) {
                                model[i] = true;
                                fixPoint = false;
                                System.out.printf(
                                        "Inferred '%s' with clause %s\n",
                                        codes.get(i),
                                        IntStream.of(clause).map(Math::abs).mapToObj(codes::get).collect(joining(",", "[", "]"))
                                );
                            }

                            break;
                        }
                    }
                    if (goalClause) {
                        // This is a goal clause
                        System.out.println("No models satisfy all clauses simultaneously. False goal clause: " + Arrays.toString(clause));
                        return false;
                    }
                }
            }
        }

        System.out.println();
        for(int i = 1; i < model.length; i++) {
            if (!model[i])
                continue;
            final String s = codes.getOrDefault(i, "");
            if(s.startsWith("Wumpus_")) {
                final int square = Integer.parseInt(s.substring(s.length() - 1));
                final int row = ((square - 1) / 2) + 1;
                final int col = ((square - 1) % 2) + 1;
                System.out.printf("Wumpus is at position [%s,%s]\n", row, col);
            }
        }
        System.out.println();
        System.out.println("Done.");
        return true;
    }

    public void addClause(int c, int... cs) {
        final int[] clauses = IntStream.concat(
                IntStream.of(c),
                cs == null || cs.length == 0 ? IntStream.empty() : IntStream.of(cs)
        ).toArray();
        this.clauses.add(clauses);
    }

    public void resetClauses() {
        clauses.clear();
    }

    public static void example() {

        // The following is the CNF of Figure 7.16 in AIMA
        // Symbols A, B, L, M, P, Q are numbered 1..6.
        ForwardChaining fc=new ForwardChaining();

        fc.addClause(-5, 6);
        fc.addClause(-3, -4, 5);
        fc.addClause(-2,-3,4);
        fc.addClause(-1, -5, 3);
        fc.addClause(-1,-2,3);
        fc.addClause(1);
        fc.addClause(2);

        //  Call forward chaining. 6 is the number of proposition symbols, which must be
        //  numbered 1..6.
        final Map<Integer, String> codes = IntStream.rangeClosed(1,6)
                .boxed()
                .collect(toMap(Integer::intValue, ForwardChaining::toLetter));
        boolean modelExists = fc.forwardChaining(codes);
        System.out.println("Model exists: "+modelExists);
    }

    private static final char[] LETTERS = { 'A', 'B', 'L', 'M', 'P', 'Q' };
    private static String toLetter(int i) {
        return String.valueOf(LETTERS[i-1]);
    }
    public static void main(String [] args) {
        example();
    }

}
