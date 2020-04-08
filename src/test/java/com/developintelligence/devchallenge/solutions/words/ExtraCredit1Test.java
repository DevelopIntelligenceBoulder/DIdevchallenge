package com.developintelligence.devchallenge.solutions.words;

import com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations.mapOfWordAndListOfMatchesTheOldWay;
import static com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations.mapOfWordAndListOfMatchesWithCaching;
import static com.developintelligence.devchallenge.solutions.solutions.words.ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFunc;
import static com.developintelligence.devchallenge.solutions.solutions.words.ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInput;
import static com.developintelligence.devchallenge.solutions.solutions.words.ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallel;
import static com.developintelligence.devchallenge.solutions.solutions.words.ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallelConcurrent;

/**
 * @author whynot
 */
public class ExtraCredit1Test {


    @Test
    public void testmapOfWordAndListOfMatchesWithPredFunc() throws IOException, URISyntaxException {
        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
        BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0)
                && orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));

        Map<String, List<String>> result = mapOfWordAndListOfMatchesWithPredFunc(iwa1, subStringCreator, matcherPred);
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
    }

    @Test
    public void testmapOfWordAndListOfMatchesWithPredFuncAndLargeInput() throws IOException, URISyntaxException {
        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> Character.isLowerCase(word.charAt(0)) && word.length() == 7 && !word.contains("'");
        BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0)
                && orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));

        Map<String, Set<String>> result =
                mapOfWordAndListOfMatchesWithPredFuncAndLargeInput(iwa1, subStringCreator, matcherPred);
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(130, result.size());
    }

    @Test
    public void testmapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallel() throws IOException, URISyntaxException {
        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> Character.isLowerCase(word.charAt(0)) && word.length() == 7 && !word.contains("'");
        BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0)
                && orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
        Map<String, List<String>> result =
                mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallel(iwa1, subStringCreator, matcherPred);
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(130, result.size());
    }

    @Test
    public void testmapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallelConcurrent() throws IOException, URISyntaxException {
        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> Character.isLowerCase(word.charAt(0)) && word.length() == 7 && !word.contains("'");
        BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0)
                && orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
        Map<String, List<String>> result =
                mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallelConcurrent(iwa1, subStringCreator, matcherPred);
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(130, result.size());
    }

//
//}

    public static void main(String[] args) throws URISyntaxException, IOException {
//        mapOfWordAndListOfMatches();
        System.out.println("***********");
        mapOfWordAndListOfMatchesWithCaching();
//        System.out.println("***********");
//        mapOfWordAndFirstMatchWithCaching();
        System.out.println("***********");
        mapOfWordAndListOfMatchesTheOldWay();
//        mapOfWordAndFirstMatchToo();
        System.out.println("***********");
//        System.out.println("***********");
//        mapOfWordAndListOfMatchesFromWordsFirst();

        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> word.length() == 9 && !word.contains("'");
        BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0) &&
                orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
//        mapOfWordAndListOfMatchesWithPredFuncAndLargeInput(iwa1, subStringCreator);
    }
}
