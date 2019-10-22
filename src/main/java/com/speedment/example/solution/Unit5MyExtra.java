package com.speedment.example.solution;

import com.speedment.common.tuple.Tuple2;
import com.speedment.common.tuple.Tuple3;
import com.speedment.common.tuple.Tuples;
import com.speedment.example.domainmodel.sakila.sakila.sakila.actor.Actor;
import com.speedment.example.domainmodel.sakila.sakila.sakila.film.Film;
import com.speedment.example.domainmodel.sakila.sakila.sakila.film_actor.FilmActor;
import com.speedment.example.domainmodel.sakila.sakila.sakila.film_actor.FilmActorManager;
import com.speedment.example.unit.Unit5Extra;
import com.speedment.runtime.join.Join;
import com.speedment.runtime.join.JoinComponent;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

public final class Unit5MyExtra implements Unit5Extra {

    @Override
    public Map<Actor, Long> actorToFilmCount(JoinComponent joinComponent) {
        // This is a predefined JOIN which is given for the task at hand
        // This join can produce streams with Tuple3<FilmActor, Film, Actor> elements
        // The FilmActor can be extracted by invoking Tuple3::get0
        // The Film can be extracted by invoking Tuple3::get1
        // The Actor can be extracted by invoking Tuple3::get2
        Join<Tuple3<FilmActor, Film, Actor>> join = joinComponent
                .from(FilmActorManager.IDENTIFIER)
                .innerJoinOn(Film.FILM_ID).equal(FilmActor.FILM_ID)
                .innerJoinOn(Actor.ACTOR_ID).equal(FilmActor.ACTOR_ID)
                .build(Tuples::of);

        return join.stream().collect(groupingBy(Tuple3::get2, counting()));
    }

    @Override
    public Map<Actor, List<Film>> filmographies(JoinComponent joinComponent) {
        Join<Tuple2<Film, Actor>> join = getJoin(joinComponent); // Apply a custom constructor, discarding FilmActor

        return join.stream().collect(
                groupingBy(Tuple2::get1,
                        mapping(Tuple2::get0, toList()))
        );
    }

    @Override
    public Map<String, List<String>> filmographiesNames(JoinComponent joinComponent) {
        return getJoin(joinComponent).stream()
                .collect(
                        groupingBy(Tuple2.<Film, Actor>getter1().andThen(Actor::getLastName),
                                mapping(
                                        Tuple2.<Film, Actor>getter0().andThen(Film::getTitle),
                                        toList()
                                )
                        )
                );
    }

    @Override
    public Map<Actor, Map<String, Long>> pivot(JoinComponent joinComponent) {
        return getJoin(joinComponent).stream()
                .collect(
                        groupingBy(
                                Tuple2::get1,
                                groupingBy(
                                        tu -> tu.get0().getRating().orElse("UNKNOWN"),
                                        counting()
                                )
                        )
                );
    }

    @Override
    public LongStream factorials() {
        return IntStream
                .rangeClosed(1, 21)
                .mapToLong(
                        n ->
                                LongStream
                                        .range(1, n)
                                        .reduce(1,
                                                (a, b) -> a * b)
                );
    }

    @Override
    public Stream<BigInteger> bigFactorials() {
        AtomicReference<BigInteger> accumulator = new AtomicReference<>(BigInteger.valueOf(1));
        return Stream.concat(
                Stream.of(BigInteger.valueOf(1), BigInteger.valueOf(1)),
                IntStream.iterate(2, n -> n + 1)
                        .mapToObj(n ->
                                accumulator.accumulateAndGet(BigInteger.valueOf(n), BigInteger::multiply)
                        ));
    }

    private Join<Tuple2<Film, Actor>> getJoin(JoinComponent joinComponent) {
        return joinComponent
                .from(FilmActorManager.IDENTIFIER)
                .innerJoinOn(Film.FILM_ID).equal(FilmActor.FILM_ID)
                .innerJoinOn(Actor.ACTOR_ID).equal(FilmActor.ACTOR_ID)
                .build((fa, f, a) -> Tuples.of(f, a));
    }
}
