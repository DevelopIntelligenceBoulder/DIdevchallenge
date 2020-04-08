package com.developintelligence.devchallenge.solutions.solutions.words;

/**
 * @author whynot
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Here's a list of seven-letter words. For each one, change the
 * first and last letters — but only the first and last letters — to make a
 * new, uncapitalized seven-letter word. Both the first and last letters have to change.
 * Program your solution, and use Java functional programming and
 * not imperative programming style. You may need to download an external word
 * list, for example, https://gist.github.com/wchargin/8927565 , since /usr/share/dict/words
 * doesn't have a comprehensive list. As a hint, you may need to create your own
 * Pair or Tuple2. Be sure to print the answer in a nice format.
 * Ex. TOURISM --> NOURISH
 * 1. PARTOOK
 * 2. TERSELY
 * 3. GUNROOM
 * 4. HELLISH
 * 5. LORELEI
 * 6. CARCASS
 * 7. GORDIAN
 * 8. LIGNITE
 * 9. PARTING
 */

/**
 * This file contains several variations of the solutions to the problem.
 * Each is in a separate function with a long and unwieldy name that hopefully
 * is descriptive.
 * <p>
 * There is also a set of tests in the test directory, which may be an easier
 * way to run individual methods.  In this file you will have to fiddle with
 * main to run a particular method.
 */
public class WordChallenge1Variations {
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
        Map<String, List<String>> mli = mapOfWordAndListOfMatches();
        mli.forEach((k, v) -> System.out.println(k + ": " + v));
//        System.out.println("***********");
//        mapOfWordAndListOfMatchesWithCaching();
//        System.out.println("***********");
//        mapOfWordAndFirstMatchWithCaching();
//        System.out.println("***********");
//        mapOfWordAndListOfMatchesTheOldWay();
//        mapOfWordAndFirstMatchToo();
//        System.out.println("***********");
//        System.out.println("***********");
//        mapOfWordAndListOfMatchesFromWordsFirst();

    }



    /**
     * @param word
     * @return
     */
    public static boolean isOk(String word) {
        return Character.isLowerCase(word.charAt(0)) && word.length() == 7 && !word.contains("'");

    }

    /**
     * We iterate over the main word list and look for matches by
     * streaming our input list each time looking for a match.
     * Seems kind of wasteful, but see below.
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatches() throws URISyntaxException, IOException {
        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                //No apostrophes and 7 letters only
                .filter(word -> word.length() == 7 && !word.contains("'"))
                .map(word -> {
                    String foundWord = Arrays.stream(input)
                            //.map(s1 -> s1.substring(1, 5))
                            .filter(s2 -> {
                                String tmp1 = s2.substring(1, 6);
                                String tmp2 = word.substring(1, 6);
                                return tmp1.equalsIgnoreCase(tmp2);
                            })
                            .findFirst().orElse("Not Found");

                    return new Tuple2<>(word, foundWord);
                }).filter(pair -> !pair.getB().equals("Not Found")
                        && !pair.getA().equalsIgnoreCase(pair.getB()))
                //.collect(Collectors.toSet());
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        Collectors.mapping(p -> p.getA(), Collectors.toList())));

//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }


    /**
     * Make a cache of substring => List of the input words.  We then
     * iterate over the main word list and look for matches in our cached map.
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithCaching() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase()));

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                //Is this an acceptable word?
                .filter(WordChallenge1Variations::isOk)
                //We need the flatMap here because we return a Stream<String> from the
                //Function.  flatMap will send the contents of that Stream downstream
                //before it goes back upstream for the next word.
                // i.e. it will *flatten* the Stream
                .flatMap(word -> {
                    String lcWord = word.toLowerCase();
                    String tmp2 = lcWord.substring(1, 6);
                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
                                .filter(s -> {
                                    String lcs = s.toLowerCase();
                                    //First and last letters should be different
                                    return lcWord.charAt(0) != lcs.charAt(0)
                                            && lcWord.charAt(lcWord.length() - 1) !=
                                                        lcs.charAt(lcs.length() - 1);
                                })
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") &&
                        !pair.getA().equalsIgnoreCase(pair.getB()))
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        TreeMap::new,
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
     * Do the same thing in parallel, for fun and profit.
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithCachingParallel() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase()));

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                //Here is the parallel magic
                .parallel()
                .filter(WordChallenge1Variations::isOk)
                .flatMap(word -> {
                    String lcWord = word.toLowerCase();
                    String substr = lcWord.substring(1, 6);
                    List<String> orig = subStringtoListOfWords.get(substr);
                    if (orig != null) {
                        return orig.stream()
                                .filter(s -> {
                                    String lcs = s.toLowerCase();
                                    return lcWord.charAt(0) != lcs.charAt(0)
                                            && lcWord.charAt(lcWord.length() - 1) != lcs.charAt(lcs.length() - 1);
                                })
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") && !pair.getA().equalsIgnoreCase(pair.getB()))
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getB(),
                        TreeMap::new,
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
     * Try with ConcurrentHasMap to see if it makes a difference from plain old parallel.
     * On my machine, with this particular input, it doesn't seem to make any difference.
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesWithCachingParallelConcurrent() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase()));

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> wordMap = Files.lines(Paths.get(url.toURI()))
                .parallel()
                .filter(WordChallenge1Variations::isOk)
                .flatMap(word -> {
                    String tmp2 = word.substring(1, 6).toLowerCase();
                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
                                .filter(s -> {
                                    String lcs = s.toLowerCase();
                                    return word.charAt(0) != lcs.charAt(0)
                                            && word.charAt(word.length() - 1) != lcs.charAt(lcs.length() - 1);
                                })
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") && !pair.getA().equalsIgnoreCase(pair.getB()))
                .collect(Collectors.groupingByConcurrent((Tuple2<String, String> pair) -> pair.getB(),
                        ConcurrentHashMap::new,
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
     * Here we do it with good old for loops. No Streams or Lambdas
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesTheOldWay() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = new HashMap<>();
        for (String w : input) {
            List<String> list = subStringtoListOfWords.computeIfAbsent(w.substring(1, 6).toLowerCase(),
                    s -> new ArrayList<>());
            list.add(w);
        }

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");

        Map<String, List<String>> wordMap = new TreeMap<>();
        BufferedReader reader = Files.newBufferedReader(Paths.get(url.toURI()));
        String word;
        while ((word = reader.readLine()) != null) {
            if (isOk(word)) {
                String tmp2 = word.substring(1, 6).toLowerCase();

                List<String> orig = subStringtoListOfWords.get(tmp2);
                if (orig != null) {
                    for (String s : orig) {
                        String lcs = s.toLowerCase();
                        if (word.charAt(0) != lcs.charAt(0)
                                && word.charAt(word.length() - 1) != lcs.charAt(lcs.length() - 1)
                                && !word.equalsIgnoreCase(lcs)) {
                            List<String> finalList = wordMap.computeIfAbsent(s, p -> new ArrayList<>());
                            finalList.add(word);
                        }
                    }
                }
            }
        }

        //add any not found without lambdas
//        for (Map.Entry<String, List<String>> entry : subStringtoListOfWords.entrySet()) {
//            for (String s : entry.getValue()) {
//                wordMap.computeIfAbsent(s, s2 -> new ArrayList<>());
//            }
//        }

//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> new ArrayList<>());
//            });
//        });
//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }

    /**
     * Return only the first match, if any.
     * Make a cache of substring => List of the input words.  We then
     * iterate over the main word list and look for matches in our cached map.
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, String> mapOfWordAndFirstMatchWithCaching() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase()));

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, String> wordMap = Files.lines(Paths.get(url.toURI()))
                .filter(WordChallenge1Variations::isOk)
                .flatMap(word -> {
                    String tmp2 = word.substring(1, 6).toLowerCase();
                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
                                .filter(s -> {
                                    String lcs = s.toLowerCase();
                                    return word.charAt(0) != lcs.charAt(0)
                                            && word.charAt(word.length() - 1) != lcs.charAt(lcs.length() - 1);
                                })
                                .map(o -> new Tuple2<>(word, o));
                    } else {
                        return Stream.of(new Tuple2<>(word, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") &&
                        !pair.getA().equalsIgnoreCase(pair.getB()))
                //here we are using the 3 argument version of toMap, and simply
                //throwing away every other entry for a match but the first.
                //See below for a variation.
                .collect(Collectors.toMap(pair -> pair.getB(),
                        (pair -> pair.getA()), (pair1, pair2) -> pair1));

        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> "Not Found");
//            });
//        });
//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }

    /**
     * A variation for finding the first match, where we only send the first match
     * from any found matches using findFirst.
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, String> mapOfWordAndFirstMatchToo() throws URISyntaxException, IOException {
        //cache substring => List of matching words (could be more than one)
        Map<String, List<String>> subStringtoListOfWords = Arrays.stream(input)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase()));

        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, String> wordMap = Files.lines(Paths.get(url.toURI()))
                .filter(WordChallenge1Variations::isOk)
                .map(word -> {
                    String tmp2 = word.substring(1, 6).toLowerCase();
                    List<String> orig = subStringtoListOfWords.get(tmp2);
                    if (orig != null) {
                        //return only the first match, if any
                        return orig.stream()
                                .filter(s -> word.charAt(0) != s.charAt(0)
                                        && word.charAt(word.length() - 1) != s.charAt(s.length() - 1))
                                .map(o -> new Tuple2<>(word, o))
                                .findFirst()
                                .orElse(new Tuple2<>(word, "Not Found"));
                    } else {
                        return new Tuple2<>(word, "Not Found");

                    }
                }).filter(pair -> !pair.getB().equals("Not Found") &&
                        !pair.getA().equalsIgnoreCase(pair.getB()))
                .collect(Collectors.toMap(pair -> pair.getB(),
                        (pair -> pair.getA()), (pair1, pair2) -> pair1));


        //add any not found
//        subStringtoListOfWords.forEach((k, v) -> {
//            v.forEach(s -> {
//                wordMap.computeIfAbsent(s, s2 -> "Not Found");
//            });
//        });

//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }

    /**
     * Here we make a map of substring to List of words in the dictionary, i.e.
     * we are working "backwards" from the previous approaches.
     * <p>
     * We then iterate over our input to do the work.
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, List<String>> mapOfWordAndListOfMatchesFromWordsFirst() throws
            URISyntaxException, IOException {
        //cache words and substrings, but this time we cache the
        //dictionary.
        URL url = WordChallenge1Variations.class.getClassLoader().getResource("words");
        Map<String, List<String>> subStrings = Files.lines(Paths.get(url.toURI()))
                .filter(WordChallenge1Variations::isOk)
                .collect(Collectors.groupingBy(w -> w.substring(1, 6).toLowerCase(),
                        Collectors.toList()));

        Map<String, List<String>> wordMap = Arrays.stream(input)
                .flatMap(input -> {
                    String tmp2 = input.substring(1, 6).toLowerCase();

                    List<String> orig = subStrings.get(tmp2);
                    if (orig != null) {
                        return orig.stream()
                                .filter(s -> {
                                    String lcWord = input.toLowerCase();
                                    return lcWord.charAt(0) != s.charAt(0)
                                            && lcWord.charAt(lcWord.length() - 1) !=
                                                        s.charAt(s.length() - 1);
                                })
                                .map(o -> new Tuple2<>(input, o));
                    } else {
                        return Stream.of(new Tuple2<>(input, "Not Found"));
                    }
                }).filter(pair -> !pair.getB().equals("Not Found") &&
                        !pair.getA().equalsIgnoreCase(pair.getB()))
                .collect(Collectors.groupingBy((Tuple2<String, String> pair) -> pair.getA(),
                        Collectors.mapping(p -> p.getB(), Collectors.toList())));

//        wordMap.forEach((k, v) -> System.out.println(k + ": " + v));

        return wordMap;
    }

    /**
     * Make a Stream of words from lines of text in a file.
     */
    public static Stream<String> wordStreamFactory(String fileName) throws URISyntaxException, IOException {
        URL url = WordChallenge1Variations.class.getClassLoader().getResource(fileName);
        Stream<String> result = Files.lines(Paths.get(url.toURI()))
                .flatMap(s -> Arrays.stream(s.split("\\W")))
                .filter(s -> !s.matches("\\s*"));

        return result;
    }
}
