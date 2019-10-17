package com.speedment.example.solution;

import com.speedment.example.unit.Unit1Create;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Unit1MyCreate implements Unit1Create {

    @Override
    public Stream<String> newStreamOfAToC() {
        return Stream.of("B", "C", "A").sorted();
    }

    @Override
    public IntStream newIntStreamOfOneToSeven() {
        return IntStream.rangeClosed(1, 7);
    }

    @Override
    public Stream<String> from(String[] array) {
        return Arrays.stream(array);
    }

    @Override
    public Stream<String> from(Collection<String> collection) {
        return collection.stream();
    }

    @Override
    public IntStream from(String s) {
        return s.chars();
    }

    @Override
    public IntStream infiniteAlternating() {
        return IntStream.iterate(1, i -> i * (-1));
    }

    @Override
    public IntStream infiniteRandomInts(Random rnd) {
        return rnd.ints();
    }

    @Override
    public Stream<String> linesFromPoemTxtFile() throws IOException {
        return Files.lines(Paths.get(FILE_NAME));
    }
}
