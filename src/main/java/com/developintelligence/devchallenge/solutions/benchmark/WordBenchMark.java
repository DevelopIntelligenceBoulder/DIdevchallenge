/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.developintelligence.devchallenge.solutions.benchmark;

import com.developintelligence.devchallenge.solutions.solutions.words.ExtraCredit1Variations;
import com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1;
import com.developintelligence.devchallenge.solutions.solutions.words.WordChallenge1Variations;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;


@BenchmarkMode(Mode.AverageTime)
//@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class WordBenchMark {

	@Setup(Level.Trial)
	public void setup() {
		System.out.print("Did setup ");
	}

	@TearDown(Level.Trial)
	public void tearDown() {
		System.out.print("Did shutdown ");
	}

	@Benchmark
	public void testMapOfWordAndListOfMatches(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatches();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListOfMatchesWithCache(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesWithCaching();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListOfMatchesWithCacheParallel(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesWithCachingParallel();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListOfMatchesWithCacheParallelConcurrent(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesWithCachingParallelConcurrent();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListOfMatchesTheOldWay(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesTheOldWay();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListWithFuncPred(Blackhole sink) throws IOException, URISyntaxException {
		Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
		Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
		BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0)
				&& orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
		Map<String, List<String>> result =
				ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFunc(iwa1, subStringCreator, matcherPred);

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListWithFuncPredAndLargeInput(Blackhole sink) throws IOException, URISyntaxException {
		Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
		Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
		BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0) &&
				orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
		Map<String, Set<String>> result =
				ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInput(iwa1, subStringCreator, matcherPred);

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListWithFuncPredAndLargeInputParallel(Blackhole sink) throws IOException, URISyntaxException {
		Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
		Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
		BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0) &&
				orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
		Map<String, List<String>> result =
				ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallel(iwa1, subStringCreator, matcherPred);

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListWithFuncPredAndLargeInputParallelConcurrent(Blackhole sink) throws IOException, URISyntaxException {
		Function<String, String> subStringCreator = (s) -> s.substring(1, 6).toLowerCase();
		Predicate<String> iwa1 = (word) -> word.length() == 7 && !word.contains("'");
		BiPredicate<String, String> matcherPred = ((orig, dictWord) -> orig.charAt(0) != dictWord.charAt(0) &&
				orig.charAt(orig.length() -1) != dictWord.charAt(dictWord.length() - 1));
		Map<String, List<String>> result =
				ExtraCredit1Variations.mapOfWordAndListOfMatchesWithPredFuncAndLargeInputParallelConcurrent(iwa1, subStringCreator, matcherPred);

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndListOfMatchesFromWordsFirst(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1Variations.mapOfWordAndListOfMatchesFromWordsFirst();

		sink.consume(result);
	}


	@Benchmark
	public void testMapOfWordAndFirstMatchWithCaching(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, String> result = WordChallenge1Variations.mapOfWordAndFirstMatchWithCaching();

		sink.consume(result);
	}

	@Benchmark
	public void testMapOfWordAndFirstMatchToo(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, String> result = WordChallenge1Variations.mapOfWordAndFirstMatchToo();

		sink.consume(result);
	}

	@Benchmark
	public void testWordChallenge1(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, List<String>> result = WordChallenge1.doChallenge1();
		sink.consume(result);
	}

	@Benchmark
	public void testWordChallenge1WithLargeInput(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, Set<String>> result = WordChallenge1.doChallenge1WithLargeInput();

		sink.consume(result);
	}

	@Benchmark
	public void testWordChallenge1WithLargeInputParallel(Blackhole sink) throws IOException, URISyntaxException {
		Map<String, Set<String>> result = WordChallenge1.doChallenge1WithLargeInputParallel();

		sink.consume(result);
	}

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder()
				.include(WordBenchMark.class.getSimpleName())
				.forks(1)
				//.jvmArgs("-Xms3048m", "-Xmx3048m")
				//.jvmArgs("-XX:+PrintCompilation", "-verbose:gc")
				.build();

		new Runner(opt).run();
	}

}
