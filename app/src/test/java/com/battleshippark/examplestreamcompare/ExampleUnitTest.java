package com.battleshippark.examplestreamcompare;

import com.annimon.stream.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import java8.util.function.Functions;
import java8.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void basic() throws Exception {
        List<Integer> prices = Arrays.asList(30, 17, 20, 15, 18, 45, 12);

        double sum = prices.stream()
                .filter(price -> price > 20)
                .map(price -> price * 0.9)
                .reduce(0.0, (_sum, _value) -> _sum + _value);
        assertThat(sum).isEqualTo(67.5);

        sum = com.annimon.stream.Stream.of(prices)
                .filter(price -> price > 20)
                .map(price -> price * 0.9)
                .reduce(0.0, (_sum, _value) -> _sum + _value);
        assertThat(sum).isEqualTo(67.5);

        sum = java8.util.stream.StreamSupport.stream(prices)
                .filter(price -> price > 20)
                .map(price -> price * 0.9)
                .reduce(0.0, (_sum, _value) -> _sum + _value);
        assertThat(sum).isEqualTo(67.5);
    }

    @Test
    public void speed1() throws Exception {
        long start1 = System.currentTimeMillis();
        com.annimon.stream.IntStream.range(0, Integer.MAX_VALUE)
                .filter(price -> price % 2 == 0)
                .map(price -> price + 1)
                .reduce(0, (_sum, _value) -> _sum + _value);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        java8.util.stream.IntStreams.range(0, Integer.MAX_VALUE)
                .filter(price -> price % 2 == 0)
                .map(price -> price + 1)
                .reduce(0, (_sum, _value) -> _sum + _value);
        long end2 = System.currentTimeMillis();

        System.out.println(String.format("%d, %d", end1 - start1, end2 - start2));
    }

    @Test
    public void speed2() throws Exception {
        long start1 = System.currentTimeMillis();
        com.annimon.stream.IntStream.range(0, Integer.MAX_VALUE)
                .filter(price -> price % 2 == 0)
                .map(price -> price + 1)
                .filter(price -> price % 3 == 0)
                .reduce(0, (_sum, _value) -> _sum + _value);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        java8.util.stream.IntStreams.range(0, Integer.MAX_VALUE)
                .filter(price -> price % 2 == 0)
                .map(price -> price + 1)
                .filter(price -> price % 3 == 0)
                .reduce(0, (_sum, _value) -> _sum + _value);
        long end2 = System.currentTimeMillis();

        System.out.println(String.format("%d, %d", end1 - start1, end2 - start2));
    }

    class Person {
        final String name;
        final int age;

        Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }

        int ageDiff(Person other) {
            return this.age - other.age;
        }

        @Override
        public String toString() {
            return String.format("%s - %d", name, age);
        }
    }

    @Test
    public void comparator1() throws Exception {
        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35)
        );
        List<Person> asc = com.annimon.stream.Stream.of(people)
                .sorted(Person::ageDiff)
                .collect(com.annimon.stream.Collectors.toList());

        List<Person> asc2 = java8.util.stream.StreamSupport.stream(people)
                .sorted(Person::ageDiff)
                .collect(java8.util.stream.Collectors.toList());
        assertThat(asc).isEqualTo(asc2);
    }

    @Test
    public void comparator2() throws Exception {
        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35)
        );
        Comparator<Person> compAsc = Person::ageDiff;
        Comparator<Person> compDesc = java8.util.Comparators.reversed(compAsc);

        java8.util.stream.StreamSupport.stream(people)
                .sorted(compAsc)
                .forEach(System.out::println);
        java8.util.stream.StreamSupport.stream(people)
                .sorted(compDesc)
                .forEach(System.out::println);
    }

    @Test
    public void comparator3() throws Exception {
        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35)
        );
        com.annimon.stream.Stream.of(people)
                .min(Person::ageDiff)
                .ifPresent(System.out::println);

        java8.util.stream.StreamSupport.stream(people)
                .min(Person::ageDiff)
                .ifPresent(System.out::println);
    }

    @Test
    public void lazyStream() {
        List<String> names = Arrays.asList("Brad", "Kate", "Kim", "Jack", "Joe", "Mike", "Susan", "George", "Robert", "Julia", "Parker", "Benson");

        System.out.println("LSA");
        String firstnameWith3Letters = com.annimon.stream.Stream.of(names).filter(name -> length(name) == 3)
                .map(this::toUpper).findFirst().get();

        System.out.println("Streamsupport");
        firstnameWith3Letters = java8.util.stream.StreamSupport.stream(names).filter(name -> length(name) == 3)
                .map(this::toUpper).findFirst().get();

    }

    private int length(String name) {
        System.out.println("length(): " + name);
        return name.length();
    }

    private String toUpper(String name) {
        System.out.println("toUpper(): " + name);
        return name.toUpperCase();
    }

    @Test
    public void reverse() {
        List<String> names = Arrays.asList("Brad", "Kate", "Kim", "Jack");

        System.out.println("java8");
        new LinkedList<>(names).descendingIterator().forEachRemaining(System.out::println);

        System.out.println("LSA");
        com.annimon.stream.Stream.of(new LinkedList<>(names).descendingIterator()).forEach(System.out::println);

        System.out.println("Streamsupport");
        java8.util.stream.RefStreams.of(new LinkedList<>(names).descendingIterator()).forEach(System.out::println);
    }

    @Test
    public void stringGrouping() {
        String str = "abcdbcdcdd";
        List<String> list = new ArrayList<>();
        for (int index = 0; index < str.length(); index++) {
            list.add(str.substring(index, index + 1));
        }
        Map<String, Long> countMap1 = list.stream().collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertThat(countMap1).hasSize(4);
        assertThat(countMap1.get("a")).isEqualTo(1);
        assertThat(countMap1.get("b")).isEqualTo(2);
        assertThat(countMap1.get("c")).isEqualTo(3);
        assertThat(countMap1.get("d")).isEqualTo(4);

        Map<String, Long> countMap2 = Stream.of(list).collect(com.annimon.stream.Collectors.groupingBy(
                t -> t,
                com.annimon.stream.Collectors.counting()
        ));
        assertThat(countMap2).isEqualTo(countMap1);

        Map<String, Long> countMap3 = StreamSupport.stream(list).collect(java8.util.stream.Collectors.groupingBy(
                Functions.identity(), java8.util.stream.Collectors.counting()));
        assertThat(countMap3).isEqualTo(countMap2);
    }
}