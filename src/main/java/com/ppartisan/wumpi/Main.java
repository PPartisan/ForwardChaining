package com.ppartisan.wumpi;

import static com.ppartisan.wumpi.Clause.or;
import static com.ppartisan.wumpi.ForwardChaining.withClauses;
import static com.ppartisan.wumpi.Literal.lit;
import static com.ppartisan.wumpi.Literal.neg;
import static com.ppartisan.wumpi.LiteralName.*;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        // The following is the CNF of Figure 7.16 in AIMA Symbols A, B, L, M, P, Q are numbered 1..6.
        final ForwardChaining fc = withClauses(
                or(neg(P), lit(Q)),
                or(neg(L), neg(M), lit(P)),
                or(neg(B), neg(L), lit(M)),
                or(neg(A), neg(P), lit(L)),
                or(neg(A), neg(B), lit(L)),
                or(lit(A)),
                or(lit(B))
        );

        // Call forward chaining. 6 is the number of proposition symbols, which must be numbered 1..6.
        out.printf("Model exists: %s\n", fc.forwardChaining());
    }

}
