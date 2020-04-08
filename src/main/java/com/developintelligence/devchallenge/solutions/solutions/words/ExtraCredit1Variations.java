package com.developintelligence.devchallenge.solutions.solutions.words;

/**
 * @author whynot
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For extra extra credit:
 * Create a variation of challenge that allows the user to input the criteria for searching and
 * matching.  You might allow them to specify a way to select a certain part of each word
 * to search for, and what criteria to use for the search.
 *
 * For example, it should be possible plug into your code instructions to do the following:
 * For all words of length 6 in the dictionary that start with ‘x’
 *
 * Find from among them those where the 2nd thru 4th characters are the same as the
 * corresponding characters of the input string.
 *
 * Hint: Think Strategy pattern.  Look for those parts of your operation that vary.  These
 * are often places in the code where you make a decision or perform an operation.
 * Think of how you can hide those parts of the code behind abstractions, which the user
 * can then plug into your code.  java.util.Comparator is an example of this
 */

/**
 * This file contains several variations of the solutions to the Extra Credit
 * coding challenge.
 * Each is in a separate function with a long and unwieldy name that hopefully
 * is descriptive.
 * <p>
 * There is also a set of tests in the test directory, which may be an easier
 * way to run individual methods.  In this file you will have to fiddle with
 * main to run a particular method.
 */
public class ExtraCredit1Variations {
    static String[] input = {"PARTOOK",
            "TERSELY",
            "GUNROOM",
            "HELLISH",
            "LORELEI",
            "CARCASS",
            "GORDIAN",
            "LIGNITE",
            "PARTING"
    };

    public static void main(String[] args) throws URISyntaxException, IOException {

        Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
        Predicate<String> isWordApplicable = WordChallenge1Variations::isOk;
        Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
        BiPredicate<String, String> postMatchPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0) &&
                orig.charAt(orig.length() - 1) != dictWord.charAt(dictWord.length() - 1));
        Map<String, Set<String>> mli =
                mapOfWordAndListOfMatchesWithPredFuncAndLargeInput(iwa1, subStringCreator, postMatchPred);

        mli.forEach((k, v) -> System.out.println(k + ": " + v));
    }



    /**
     * @param word
     * @return
     */
    public static boolean isOk(String word) {
        return Character.isLowerCase(word.charAt(0)) && word.length() == 7 && !word.contains("'");

    }


    /**
     * Here we pass in 3  arguments:
     * isWordApplicable: Predicate - is used to filter out unacceptable words.
     * e.g. we only want to work with input words of length > 7 that start with 'x'.
     * e.g.  pred = (s) -> s.length() > 7 and s.startsWith('x')
     *
     * subStringCreator: Function - is applied when we need to get the part of the input and dictionary
     * words that we are going to use for matching.  e.g. give me a substring of index 3 through 8
     * converted to lowerCase.
     * e.g. func = (s) -> s.substring(3, 8).toLowerCase()
     *
     * postMatchPredicate: BiPredicate - is applied at the point when we have some candidate
     * matches, and we can do a last minute filtering based on both values
     * e.g. accept only if ending characters and starting characters of the two strings are different.
     * e.g. pred = (s) -> orig.charAt(0) != s.charAt(0)
     *                  && orig.charAt(orig.length() -1) != s.charAt(s.length() -1)
     *
     * @param isWordApplicable  Predicate to decide if a word in the input stream is acceptable
     * @param subStringCreator  How to create the substring of the word we are going to match against
     * @param postMatchPredicate Predicate called with original and matched string when a match is found
     * @return    A map of input word to List of matches
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithPredFunc(
            Predicate<String> isWordApplicable,
            Function<String, String> subStringCreator,
            BiPredicate<String, String> postMatchPredicate) throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one).
        //Use the Function
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(subStringCreator));

        URL url = ExtraCredit1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                //Use the Predicate
                .filter(isWordApplicable)
                //flatMap here because the function returns a Stream.
                .flatMap(word -> {
                    //String tmp2 = word.substring(1, 6).toLowerCase();
                    //Use the Function again, on the other String
                    String tmp2 = subStringCreator.apply(word);

                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
                                //final predicate get's used here
                                .filter(s -> postMatchPredicate.test(s, word))
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found"))
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        Collectors.mapping(p -> p.getA(), Collectors.toList())));

        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> new ArrayList<>());
//            });
//        });
//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }

    /**
     * Here we are going to feed in some larger input to see what happens.
     * @param isWordApplicable
     * @param subStringCreator
     * @param postMatchPredicate
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, Set<String>> mapOfWordAndListOfMatchesWithPredFuncAndLargeInput(
            Predicate<String> isWordApplicable,
            Function<String, String> subStringCreator,
            BiPredicate<String, String> postMatchPredicate) throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one).
        //Use the Predicate and Function
        Map<String, List<String>> subStringtoListOfWords = wordStreamFactory("PrideAndPrejudice.txt")
                .filter(isWordApplicable)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(subStringCreator));

        URL url = ExtraCredit1Variations.class.getClassLoader().getResource("words");
        Map<String, Set<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                //Use the Predicate
                .filter(isWordApplicable)
                .flatMap(word -> {
                    //String tmp2 = word.substring(1, 6).toLowerCase();
                    //Use the Function again, on the other String
                    String tmp2 = subStringCreator.apply(word);

                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
//                                .filter(s -> word.charAt(0) != s.charAt(0) && word.charAt(word.length() -1) != s.charAt(s.length() - 1))
                                .filter(s -> postMatchPredicate.test(s, word))
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found"))
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        TreeMap::new,
                        Collectors.mapping(p -> p.getA(), Collectors.toSet())));

        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> new HashSet<>());
//            });
//        });

//        wordMap.forEach((x, y) -> System.out.println(x + ": " + y));

        return wordMap;
    }

    /**
     * Make it parallel.
     *
     * @param isWordApplicable
     * @param subStringCreator
     * @param postMatchPredicate
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallel(
            Predicate<String> isWordApplicable,
            Function<String, String> subStringCreator,
            BiPredicate<String, String> postMatchPredicate) throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one).
        //Use the Predicate and Function
        Map<String, List<String>> subStringtoListOfWords = wordStreamFactory("PrideAndPrejudice.txt")
                .parallel()
                .filter(isWordApplicable)
                .collect(Collectors.groupingBy(subStringCreator));

        URL url = ExtraCredit1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                .parallel()
                //Use the Predicate
                .filter(isWordApplicable)
                .flatMap(word -> {
                    //String tmp2 = word.substring(1, 6).toLowerCase();
                    //Use the Function again, on the other String
                    String tmp2 = subStringCreator.apply(word);

                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
//                                .filter(s -> word.charAt(0) != s.charAt(0) && word.charAt(word.length() -1) != s.charAt(s.length() - 1))
                                .filter(s -> postMatchPredicate.test(s, word))
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") )
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        TreeMap::new,
                        Collectors.mapping(p -> p.getA(), Collectors.toList())));

        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> new ArrayList<>());
//            });
//        });

//        wordMap.forEach((x, y) -> System.out.println(x + ": " + y));

        return wordMap;
    }

    /**
     * Let's see if using a ConcurrentHashMap makes a difference.
     * It does not for this input and my machine.
     * @param isWordApplicable
     * @param subStringCreator
     * @param postMatchPredicate
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallelConcurrent(
            Predicate<String> isWordApplicable,
            Function<String, String> subStringCreator,
            BiPredicate<String, String> postMatchPredicate) throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one).
        //Use the Predicate and Function
        Map<String, List<String>> subStringtoListOfWords = wordStreamFactory("PrideAndPrejudice.txt")
                .parallel()
                .filter(isWordApplicable)
                .collect(Collectors.groupingBy(subStringCreator));

        URL url = ExtraCredit1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                .parallel()
                //Use the Predicate
                .filter(isWordApplicable)
                .flatMap(word -> {
                    //String tmp2 = word.substring(1, 6).toLowerCase();
                    //Use the Function again, on the other String
                    String tmp2 = subStringCreator.apply(word);

                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
//                                .filter(s -> word.charAt(0) != s.charAt(0) && word.charAt(word.length() -1) != s.charAt(s.length() - 1))
                                .filter(s -> postMatchPredicate.test(s, word))
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found"))
                .collect(Collectors.groupingByConcurrent((Tuple2<String, String> pair) -> pair.getB(),
                        ConcurrentHashMap::new,
                        Collectors.mapping(p -> p.getA(), Collectors.toList())));

        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> new ArrayList<>());
//            });
//        });

//        wordMap.forEach((x, y) -> System.out.println(x + ": " + y));

        return wordMap;
    }


    /**
     * Make a Stream of words from a lines of text
     */
    public static Stream<String> wordStreamFactory(String fileName) throws URISyntaxException, IOException {
        URL url = ExtraCredit1Variations.class.getClassLoader().getResource(fileName);
        Stream<String> result = Files.lines(Paths.get(url.toURI()))
                .flatMap(s -> Arrays.stream(s.split("\\W")))
                .filter(s -> !s.matches("\\s*"));

        return result;
    }
}
