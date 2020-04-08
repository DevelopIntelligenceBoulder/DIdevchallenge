package com.developintelligence.devchallenge.solutions.words;

import com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1;
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
import static com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations.mapOfWordAndListOfMatchesWithCachingParallel;
import static com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations.mapOfWordAndListOfMatchesWithCachingParallelConcurrent;

/**
 * @author whynot
 */
public class Challenge1Test {

    @Test
    public void testmapOfWordAndListOfMatchesWithCaching() throws IOException, URISyntaxException {
        Map<String, List<String>> result = mapOfWordAndListOfMatchesWithCaching();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }

    @Test
    public void testmapOfWordAndListOfMatchesWithCachingParallel() throws IOException, URISyntaxException {
        Map<String, List<String>> result = mapOfWordAndListOfMatchesWithCachingParallel();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }

    @Test
    public void testmapOfWordAndListOfMatchesWithCachingParallelConcurrent() throws IOException, URISyntaxException {
        Map<String, List<String>> result = mapOfWordAndListOfMatchesWithCachingParallelConcurrent();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }

    @Test
    public void testmapOfWordAndFirstMatchWithCachingTheOldWay() throws IOException, URISyntaxException {
        Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesTheOldWay();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }

    @Test
    public void testmapOfWordAndFirstMatchWithCaching() throws IOException, URISyntaxException {
        Map<String, String> result = WordChallenge1Variations.mapOfWordAndFirstMatchWithCaching();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS"));
        assertEquals("martini", result.get("PARTING"));
    }

    @Test
    public void testmapOfWordAndFirstMatchToo() throws IOException, URISyntaxException {
        Map<String, String> result = WordChallenge1Variations.mapOfWordAndFirstMatchToo();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS"));
        assertEquals("carting", result.get("PARTING"));
    }

    @Test
    public void testmapOfWordAndListOfMatchesFromWordsFirst() throws IOException, URISyntaxException {
        Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesFromWordsFirst();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("sarcasm", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }


    @Test
    public void testChallenge1() throws IOException, URISyntaxException {
        Map<String, List<String>> result = WordChallenge1.doChallenge1();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(9, result.size());
        assertEquals("SARCASM", result.get("CARCASS").get(0));
        assertEquals(2, result.get("PARTING").size());
    }

    @Test
    public void testChallenge1WithLargeInput() throws IOException, URISyntaxException {
        Map<String, Set<String>> result = WordChallenge1.doChallenge1WithLargeInput();
//        result.forEach((k, v) -> System.out.println(k + ": " + v));
        assertEquals(130, result.size());
    }

    @Test
    public void testChallenge1WithLargeInputParallel() throws IOException, URISyntaxException {
        Map<String, Set<String>> result = WordChallenge1.doChallenge1WithLargeInputParallel();
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
