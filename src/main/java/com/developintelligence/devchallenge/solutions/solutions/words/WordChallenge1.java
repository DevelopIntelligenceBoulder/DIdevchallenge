package com.developintelligence.devchallenge.solutions.solutions.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations.wordStreamFactory;


/**
 * This is one way to implement the solution.  There are more performant
 * ways shown in WordChallenge1Variations
 */
public class WordChallenge1 {


    private static Set<String> dictionary;

    protected static Set<String> getDictionary() {
        //From https://gist.github.com/wchargin/8927565
        //since /usr/share/dict/words didn't have a comprehensive list
        InputStream resourceAsStream =
                WordChallenge1.class.getResourceAsStream("/words");
        InputStreamReader inputStreamReader =
                new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        dictionary = bufferedReader
                .lines()
                .filter(w -> Character.isLowerCase(w.charAt(0)))
                .filter(w -> !w.endsWith("\'s"))
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        return dictionary;
    }

    //From: https://www.npr.org/2019/11/10/777941999/sunday-puzzle-7-letters
    public static void main(String[] args) throws IOException, URISyntaxException {
        Map<String, List<String>> result = doChallenge1();
//        Map<String, Set<String>> result = doChallenge1WithLargeInput();
        result.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }


    /**
     * In this variation for each input word we create a list of
     * all possible permutations of words with different first and
     * last letters.  From the words in each List we choose those
     * which are in our main word list.
     *
     * @return
     */
    public static Map<String, List<String>> doChallenge1() {


        List<String> words = Arrays.asList("PARTOOK", "TERSELY",
                "GUNROOM", "HELLISH", "LORELEI",
                "CARCASS", "GORDIAN", "LIGNITE",
                "PARTING");

        //We are going to use this to give us a Stream of characters 'A' to 'Z'
        Supplier<Stream<String>> alpha = () ->
                IntStream.range('A', 'Z')
                        .mapToObj(x -> String.valueOf((char) x));

        //Create a list with all permutations of this word with the
        //first and last characters changed.
        //Actually, create a list of Tuple2 objects, with
        //the original word and the permutation.
        Stream<Tuple2<String, String>> candidates = words
                .stream()
                .map(String::toUpperCase)
                .flatMap(orig -> wordPairCandidates(alpha, orig));

        Set<String> dictionary = getDictionary();

        //We don't want the word itself in the results
        Stream<Tuple2<String, String>> candidatesWithOriginalRemoved =
                candidates
                        .filter(t -> !t.getB().equals(t.getA()));

        //Make sure both the first and last letters of the matching word are
        //different
        Stream<Tuple2<String, String>> candidatesWithUniqueFirstLastLetters =
                candidatesWithOriginalRemoved
                        .filter(t -> {
                            return !hasSameFirstOrLastLetter(t.getA(), t.getB());
                        });

        //Finally, we check for a match in the dictionary
        Stream<Tuple2<String, String>> validWordCandidates =
                candidatesWithUniqueFirstLastLetters
                        .filter(t -> {
                            return dictionary.contains(t.getB());
                        });

        //And make our map
        Map<String, List<String>> validWordCandidateGroups =
                validWordCandidates
                        .collect(Collectors.groupingBy(Tuple2::getA,
                                        Collectors.mapping(Tuple2::getB, Collectors.toList())));


        return validWordCandidateGroups;
    }

    /**
     * In this variation we are going to try our algorithm on a larger data set to see how
     * it scales.
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Map<String, Set<String>> doChallenge1WithLargeInput() throws IOException, URISyntaxException {

        List<String> words = Arrays.asList("PARTOOK", "TERSELY",
                "GUNROOM", "HELLISH", "LORELEI",
                "CARCASS", "GORDIAN", "LIGNITE",
                "PARTING");

        Supplier<Stream<String>> alpha = () ->
                IntStream.range('A', 'Z')
                        .mapToObj(x -> String.valueOf((char) x));


        //Our input will come from the words in PrideAndPrejudice.txt
        Stream<Tuple2<String, String>> candidates = wordStreamFactory("PrideAndPrejudice.txt")
                .filter(s -> s.length() == 7)
                .filter(w -> Character.isLowerCase(w.charAt(0)))
                .filter(w -> !w.endsWith("\'s"))
                .map(String::toUpperCase)
                .flatMap(orig -> wordPairCandidates(alpha, orig));

        Set<String> dictionary = getDictionary();

        Stream<Tuple2<String, String>> candidatesWithOriginalRemoved =
                candidates.filter(t -> !t.getB().equals(t.getA()));

        Stream<Tuple2<String, String>> candidatesWithUniqueFirstLastLetters =
                candidatesWithOriginalRemoved.filter(t -> {
                    return !hasSameFirstOrLastLetter(t.getA(), t.getB());
                });

        Stream<Tuple2<String, String>> validWordCandidates =
                candidatesWithUniqueFirstLastLetters
                        .filter(t -> dictionary.contains(t.getB()));

        Map<String, Set<String>> validWordCandidateGroups =
                validWordCandidates
                        .collect(
                                Collectors.groupingBy(Tuple2::getA,
                                        TreeMap::new,
                                        Collectors.mapping(Tuple2::getB, Collectors.toSet())));


        return validWordCandidateGroups;
    }

    /**
     * Try large input in parallel.
     *
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Map<String, Set<String>> doChallenge1WithLargeInputParallel() throws IOException, URISyntaxException {

        List<String> words = Arrays.asList("PARTOOK", "TERSELY",
                "GUNROOM", "HELLISH", "LORELEI",
                "CARCASS", "GORDIAN", "LIGNITE",
                "PARTING");

        Supplier<Stream<String>> alpha = () ->
                IntStream.range('A', 'Z')
                        .parallel()
                        .mapToObj(x -> String.valueOf((char) x));

//        Stream<Tuple2<String, String>> candidates = words
//                .stream()
//                .map(String::toUpperCase)
//                .flatMap(orig -> wordPairCandidates(alpha, orig));

        Stream<Tuple2<String, String>> candidates = wordStreamFactory("PrideAndPrejudice.txt")
                .parallel()
                .filter(s -> s.length() == 7)
                .filter(w -> Character.isLowerCase(w.charAt(0)))
                .filter(w -> !w.endsWith("\'s"))
                .map(String::toUpperCase)
                .flatMap(orig -> wordPairCandidates(alpha, orig));

        Set<String> dictionary = getDictionary();

        Stream<Tuple2<String, String>> candidatesWithOriginalRemoved =
                candidates.filter(t -> !t.getB().equals(t.getA()));

        Stream<Tuple2<String, String>> candidatesWithUniqueFirstLastLetters =
                candidatesWithOriginalRemoved.filter(t -> {
                    return !hasSameFirstOrLastLetter(t.getA(), t.getB());
                });

        Stream<Tuple2<String, String>> validWordCandidates =
                candidatesWithUniqueFirstLastLetters
                        .filter(t -> dictionary.contains(t.getB()));

        Map<String, Set<String>> validWordCandidateGroups =
                validWordCandidates
                        .collect(
                                Collectors.groupingBy(Tuple2::getA,
                                        TreeMap::new,
                                        Collectors.mapping(Tuple2::getB, Collectors.toSet())));


        return validWordCandidateGroups;
    }

    /**
     * Create a Stream of  tuples for the given word.  with the word, and and the word permutation
     * TOURISM will give a stream of Tuple2 where A will be TOURISM and
     * B will range over AOURISA, AOURISB, AOURISC, ... BOURISA, BOURISB, ... ZOURISZ
     * @param alpha
     * @param orig
     * @return
     */
    private static Stream<Tuple2<String, String>> wordPairCandidates
            (Supplier<Stream<String>> alpha, String orig) {
        String chopped = orig.substring(1, orig.length() - 1);
        return
                alpha.get().flatMap(c1 ->
                        alpha.get().map(c2 -> {
                            Tuple2<String, String> t = new Tuple2<>(orig, c1 + chopped + c2);
                            return t;
                        }));
    }

    private static boolean hasSameFirstOrLastLetter(String a, String b) {
        return a.charAt(0) == b.charAt(0) ||
                a.charAt(a.length() - 1) == b.charAt(b.length() - 1);
    }

}
