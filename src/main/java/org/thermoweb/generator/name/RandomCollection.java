package org.thermoweb.generator.name;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public Optional<E> next(Predicate<Map.Entry<Double, E>> predicate) {
        double value = random.nextDouble() * total;
        TreeMap<Double, E> filteredOptions = map.entrySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, recent) -> recent, TreeMap::new));

        return Optional.ofNullable(filteredOptions
                .higherEntry(value))
                .map(Map.Entry::getValue);
    }

    public Stream<E> stream() {
        return map.values().stream();
    }
}
