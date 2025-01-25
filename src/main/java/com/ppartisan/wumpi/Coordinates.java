package com.ppartisan.wumpi;

import java.util.Objects;

class Coordinates {
    private final int col, row;

    private Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    static Coordinates at(int col, int row) {
        return new Coordinates(col, row);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return col == that.col && row == that.row;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", col, row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
