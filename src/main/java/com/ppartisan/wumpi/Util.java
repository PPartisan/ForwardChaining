package com.ppartisan.wumpi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

final class Util {

    static Collector<Literal, List<Literal>, Set<LiteralName>> collect() {
        return new Collector<>() {
            @Override
            public Supplier<List<Literal>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<Literal>, Literal> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<Literal>> combiner() {
                return (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).collect(toList());
            }

            @Override
            public Function<List<Literal>, Set<LiteralName>> finisher() {
                return (list) -> list.stream().map(Literal::name).collect(toSet());
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }

}
