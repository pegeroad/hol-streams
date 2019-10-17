package com.speedment.example.solution;

import com.speedment.example.unit.Unit2Intermediate;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Unit2MyIntermediate implements Unit2Intermediate {

    @Override
    public Stream<String> wordsLongerThanThreeChars(Stream<String> stream) {
        return stream.filter(w -> w.length() > 3);
    }

    @Override
    public Stream<String> firstTwoWordsLongerThanThreeChars(Stream<String> stream) {
        return wordsLongerThanThreeChars(stream).limit(2);
    }

    @Override
    public Stream<String> firstDistinctTwoWordsLongerThanThreeCharsInAlphabeticOrder(Stream<String> stream) {
        return wordsLongerThanThreeChars(stream).distinct().limit(2).sorted();
    }

    @Override
    public IntStream lengthOfWords(Stream<String> stream) {
        return stream.mapToInt(String::length);
    }

    @Override
    public IntStream increasingSawtooth() {
        return IntStream.range(0, Integer.MAX_VALUE).flatMap(i -> IntStream.range(0, i));
    }

    @Override
    public Stream<String> strings(Stream<Object> stream) {
        return stream.filter(o -> (o instanceof String)).map(o -> (String) o);
    }
}
