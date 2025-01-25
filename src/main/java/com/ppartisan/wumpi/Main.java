package com.ppartisan.wumpi;

import static com.ppartisan.wumpi.Clause.clause;
import static com.ppartisan.wumpi.Coordinates.at;
import static com.ppartisan.wumpi.ForwardChaining.withClauses;
import static com.ppartisan.wumpi.Literal.lit;
import static com.ppartisan.wumpi.Literal.neg;
import static com.ppartisan.wumpi.LiteralName.*;

public class Main {

    public static void main(String[] args) {
        final ForwardChaining fc = withClauses(world2());
        System.out.printf("\n%s\n", fc.forwardChaining());
    }

    /**
     * +---------+---------+
     * | [1,3]   | [2,3]   |
     * |         |         |
     * +---------+---------+
     * | [1,2]   | [2,2]   |
     * |         | Stench, |
     * |         | Safe    |
     * +---------+---------+
     * | [1,1]   | [2,1]   |
     * | Stench, | Safe    |
     * | Safe    |         |
     * +---------+---------+
     * @return Clauses for above world. Wumpus is in [1,2].
     */
    private static Clause[] world1() {
        return new Clause[]{
                clause(neg(STENCH, at(1, 1)), neg(SAFE, at(2, 1)), lit(WUMPUS, at(1, 2))),
                clause(lit(SAFE, at(2, 1))),
                clause(lit(SAFE, at(1, 1))),
                clause(lit(SAFE, at(2, 2))),
                clause(lit(STENCH, at(1, 1))),
                clause(lit(STENCH, at(2, 2)))
        };
    }

    //ToDo - model detects wumpus at [1,2] still, but it isn't possible on a safe square.
    /**
     * +---------+---------+
     * | [1,3]   | [2,3]   |
     * |         | Safe    |
     * +---------+---------+
     * | [1,2]   | [2,2]   |
     * | Safe    | Stench, |
     * |         | Safe    |
     * +---------+---------+
     * | [1,1]   | [2,1]   |
     * | Stench, |         |
     * | Safe    |         |
     * +---------+---------+
     * @return Clauses for above world. Wumpus is in [2,1].
     */
    private static Clause[] world2() {
        return new Clause[]{
                clause(neg(STENCH, at(2,2)), neg(STENCH, at(1,1)), lit(WUMPUS, at(2,1))),
                clause(neg(STENCH, at(2,2)), neg(STENCH, at(1,1)), lit(WUMPUS, at(1,2))),
                clause(lit(SAFE, at(1,1))),
                clause(lit(SAFE, at(1,2))),
                clause(lit(SAFE, at(2,2))),
                clause(lit(SAFE, at(2,3))),
                clause(lit(STENCH, at(1,1))),
                clause(lit(STENCH, at(2,2)))
        };
    }

}
