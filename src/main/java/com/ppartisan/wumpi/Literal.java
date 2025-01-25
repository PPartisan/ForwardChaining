package com.ppartisan.wumpi;

import java.util.Objects;

class Literal {

    private final LiteralName name;
    private final Coordinates coordinates;
    private final boolean isPositive;

    private Literal(LiteralName name, Coordinates coordinates, boolean isPositive) {
        this.name = name;
        this.coordinates = coordinates;
        this.isPositive = isPositive;
    }

    static Literal lit(LiteralName name, Coordinates coordinates) {
        return new Literal(name, coordinates, true);
    }

    static Literal neg(LiteralName name, Coordinates coordinates) {
        return new Literal(name, coordinates, false);
    }

    Literal toLit() {
        return lit(name, coordinates);
    }

    LiteralName name() {
        return name;
    }

    Coordinates coordinates() {
        return coordinates;
    }

    boolean isPositive() {
        return isPositive;
    }

    boolean isNegated() {
        return !isPositive();
    }

    @Override
    public String toString() {
        return String.format("%s%s", name.toString(), coordinates);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return name == literal.name && coordinates.equals(literal.coordinates) && isPositive == literal.isPositive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, isPositive);
    }

}
