# DAA Assignment 4 — Text Analysis: Dynamic Programming and String Algorithms

## 1. Introduction

This project implements and evaluates several algorithms for text analysis as part of the Design and Analysis of Algorithms (DAA) course.

The assignment focuses on two main areas:

- Dynamic Programming: Longest Common Subsequence (LCS) and Edit Distance.
- String Algorithms: Naive String Matching, Knuth-Morris-Pratt (KMP), Rabin-Karp, and Kasai's LCP algorithm.

The main objectives are to understand how these algorithms work, analyze their time and space complexities, verify their correctness, and compare their performance through experiments.

## 2. Implemented Algorithms

### 2.1 Longest Common Subsequence (LCS)

LCS finds the length of the longest subsequence shared by two strings. Characters must appear in the same relative order, but they do not need to be consecutive.

The implementation uses a two-dimensional Dynamic Programming table.

For two strings X and Y:

- If the current characters match, the value is taken from the diagonal cell plus one.
- Otherwise, the maximum of the upper and left cells is selected.

**Complexity:**
- Time: O(n × m)
- Space: O(n × m)

The implementation returns the length of the LCS.

### 2.2 Edit Distance (Levenshtein Distance)

Edit Distance calculates the minimum number of operations required to transform one string into another.

The allowed operations are:

- Insertion
- Deletion
- Substitution

A Dynamic Programming table stores the minimum cost of transforming prefixes of the two strings.

If the current characters are equal, no additional operation is required. Otherwise, the algorithm selects the minimum cost among insertion, deletion, and substitution, adding one.

**Complexity:**
- Time: O(n × m)
- Space: O(n × m)

### 2.3 Naive String Matching

The Naive algorithm checks the pattern at every possible starting position in the text.

For each position, it compares characters one by one until a mismatch occurs or the entire pattern matches.

All matching starting positions are returned.

**Complexity:**
- Worst-case time: O(n × m)
- Best-case time: O(n)
- Auxiliary space: O(1), excluding the result list.

### 2.4 Knuth-Morris-Pratt (KMP)

KMP improves string matching by avoiding unnecessary comparisons.

Before searching, it constructs an LPS (Longest Prefix Suffix) array.

The LPS array stores the length of the longest proper prefix that is also a suffix for each pattern prefix.

When a mismatch occurs, KMP uses the LPS array to determine how far the pattern can shift without repeating previous comparisons.

**Complexity:**
- LPS preprocessing: O(m)
- Searching: O(n)
- Total time: O(n + m)
- Auxiliary space: O(m)

The implementation separately counts character comparisons during preprocessing and searching.

### 2.5 Rabin-Karp

Rabin-Karp uses hashing to find pattern occurrences.

The algorithm calculates:
- The hash of the pattern.
- The hash of the current text window.

When the hashes match, the algorithm verifies the actual characters to avoid false matches caused by hash collisions.

A rolling hash updates the current window hash efficiently when moving to the next position.

**Complexity:**
- Expected time: O(n + m)
- Worst-case time: O(n × m)
- Auxiliary space: O(1), excluding the result list.

The implementation uses:
- Base: 256
- Modulus: 1,000,000,007

The comparison counter records character comparisons performed during hash-match verification. Hash calculations and hash comparisons are not included in this counter.

### 2.6 Kasai's LCP Algorithm

Kasai's algorithm constructs the LCP (Longest Common Prefix) array using a text and an already constructed suffix array.

For each suffix, it calculates the length of the longest common prefix with the previous suffix in lexicographical order.

The implementation uses a rank array to identify the previous suffix efficiently and reuses the previously calculated common-prefix length.

The LCP convention used in this project is:

- LCP[0] = 0.
- LCP[i] stores the common-prefix length of suffixes SA[i] and SA[i-1].

**Complexity:**
- Time: O(n)
- Auxiliary space: O(n)

Suffix array construction is not required by the assignment. A prefix-doubling helper is used to generate suffix arrays for experiments, and suffix array construction is excluded from the timed Kasai measurements.

## 3. Complexity Comparison

| Algorithm | Time Complexity | Space Complexity |
|---|---|---|
| LCS | O(n × m) | O(n × m) |
| Edit Distance | O(n × m) | O(n × m) |
| Naive Search | O(n × m) worst case | O(1) auxiliary |
| KMP | O(n + m) | O(m) |
| Rabin-Karp | O(n + m) expected; O(n × m) worst case | O(1) auxiliary |
| Kasai LCP | O(n) | O(n) |

Here, n and m represent the lengths of the text and pattern or the two input strings, depending on the algorithm.

## 4. Implementation Details

The project is implemented in Java.

Standard Java libraries are used for collections, arrays, file handling, and timing.

The algorithms are implemented manually without relying on built-in string-search implementations.

The project contains the following source files:

| File | Responsibility |
|---|---|
| Main.java | Demonstration, correctness tests, and benchmark execution |
| LCS.java | Longest Common Subsequence |
| EditDistance.java | Levenshtein Distance |
| NaiveSearch.java | Naive String Matching |
| KMP.java | KMP and LPS preprocessing |
| RabinKarp.java | Rabin-Karp with rolling hash |
| KasaiLCP.java | LCP array construction |
| SuffixArray.java | Suffix array construction for demonstration |
| Benchmark.java | Performance experiments |
| StringGenerator.java | Reproducible test data generation |
| ResultWriter.java | CSV output generation |

## 5. Correctness Testing

Correctness tests are implemented directly in Main.java.

The tests compare actual algorithm results with expected values. If a test fails, the program throws a RuntimeException identifying the failed test.

### LCS Tests

- Example strings: expected LCS length of 4.
- Identical strings.
- Empty string.
- Completely different strings.

### Edit Distance Tests

- "kitten" to "sitting": expected distance 3.
- Identical strings.
- Empty string.
- Substitution.
- Insertion.
- Deletion.

### String Matching Tests

All three string-matching algorithms are checked against the same expected results.

Test cases include:

- Pattern at the beginning and end.
- Pattern in the middle.
- Multiple occurrences.
- Pattern absent from the text.
- Pattern longer than the text.
- Empty pattern.

The algorithms must return identical matching indices.

### Kasai Tests

The string "banana" is used with a manually verified suffix array.

Expected Suffix Array:

[5, 3, 1, 0, 4, 2]

Expected LCP Array:

[0, 1, 3, 0, 0, 2]

All correctness tests passed during the final project run.

## 6. Experimental Setup

All performance experiments use Java's System.nanoTime() method.

Each timed experiment is executed five times, and the average execution time is recorded.

Random strings are generated using a fixed alphabet:

abcdefghijklmnopqrstuvwxyz

Fixed random seeds are used to ensure reproducibility.

### 6.1 Dynamic Programming Experiments

Both LCS and Edit Distance