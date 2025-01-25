package com.ppartisan.wumpi;

import java.util.Objects;

class Literal {

    private final LiteralName name;
    private final boolean isPositive;

    private Literal(LiteralName name, boolean isPositive) {
        this.name = name;
        this.isPositive = isPositive;
    }

    static Literal lit(LiteralName name) {
        return new Literal(name, true);
    }

    static Literal neg(LiteralName name) {
        return new Literal(name, false);
    }

    LiteralName name() {
        return name;
    }

    boolean isPositive() {
        return isPositive;
    }

    boolean isNegated() {
        return !isPositive();
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return name == literal.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
