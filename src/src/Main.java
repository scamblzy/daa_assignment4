
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        runDemo();

        System.out.println("\n=== ALGORITHM TESTS ===");
        runTests();

        System.out.println("\n=== BENCHMARK ===");
        Benchmark.runAll();
    }

    // Demonstration of algorithms
    private static void runDemo() {

        String first = "ABCBDAB";
        String second = "BDCABA";

        System.out.println("DAA Assignment 4");

        System.out.println("\n=== LCS ===");
        System.out.println(LCS.findLength(first, second));

        System.out.println("\n=== Edit Distance ===");
        System.out.println(EditDistance.calculate("kitten", "sitting"));

        System.out.println("\n=== String Matching ===");

        String text = "abababa";
        String pattern = "aba";

        System.out.println("Naive: " +
                NaiveSearch.search(text, pattern));

        System.out.println("KMP: " +
                KMP.search(text, pattern));

        System.out.println("Rabin-Karp: " +
                RabinKarp.search(text, pattern));

        System.out.println("\n=== Kasai LCP ===");

        String suffixText = "banana";
        int[] suffixArray = SuffixArray.build(suffixText);
        int[] lcp = KasaiLCP.build(suffixText, suffixArray);

        System.out.println("Suffix Array: " +
                Arrays.toString(suffixArray));

        System.out.println("LCP Array: " +
                Arrays.toString(lcp));
    }

    // Run all correctness tests
    private static void runTests() {

        testLCS();
        testEditDistance();
        testStringMatching();
        testKasai();

        System.out.println("\nAll tests passed!");
    }

    // LCS tests
    private static void testLCS() {

        check(
                LCS.findLength("ABCBDAB", "BDCABA") == 4,
                "LCS example"
        );

        check(
                LCS.findLength("hello", "hello") == 5,
                "LCS identical strings"
        );

        check(
                LCS.findLength("", "hello") == 0,
                "LCS empty string"
        );

        check(
                LCS.findLength("abc", "xyz") == 0,
                "LCS different strings"
        );

        System.out.println("LCS: PASSED");
    }

    // Edit Distance tests
    private static void testEditDistance() {

        check(
                EditDistance.calculate("kitten", "sitting") == 3,
                "Edit Distance example"
        );

        check(
                EditDistance.calculate("hello", "hello") == 0,
                "Edit Distance identical strings"
        );

        check(
                EditDistance.calculate("", "abc") == 3,
                "Edit Distance empty string"
        );

        check(
                EditDistance.calculate("cat", "cut") == 1,
                "Edit Distance substitution"
        );

        check(
                EditDistance.calculate("cat", "cats") == 1,
                "Edit Distance insertion"
        );

        check(
                EditDistance.calculate("cats", "cat") == 1,
                "Edit Distance deletion"
        );

        System.out.println("Edit Distance: PASSED");
    }

    // Naive, KMP and Rabin-Karp tests
    private static void testStringMatching() {

        testSearchCase(
                "abcxxabc",
                "abc",
                Arrays.asList(0, 5),
                "Pattern at beginning and end"
        );

        testSearchCase(
                "xxabcxx",
                "abc",
                Arrays.asList(2),
                "Pattern in the middle"
        );

        testSearchCase(
                "abababa",
                "aba",
                Arrays.asList(0, 2, 4),
                "Multiple occurrences"
        );

        testSearchCase(
                "abcdefgh",
                "xyz",
                Arrays.asList(),
                "Pattern absent"
        );

        testSearchCase(
                "abc",
                "abcdef",
                Arrays.asList(),
                "Pattern longer than text"
        );

        testSearchCase(
                "abc",
                "",
                Arrays.asList(0, 1, 2, 3),
                "Empty pattern"
        );

        System.out.println("Naive Search: PASSED");
        System.out.println("KMP: PASSED");
        System.out.println("Rabin-Karp: PASSED");
    }

    // Check all three string matching algorithms
    private static void testSearchCase(
            String text,
            String pattern,
            List<Integer> expected,
            String testName
    ) {

        List<Integer> naive =
                NaiveSearch.search(text, pattern);

        List<Integer> kmp =
                KMP.search(text, pattern);

        List<Integer> rabin =
                RabinKarp.search(text, pattern);

        check(
                naive.equals(expected),
                "Naive Search - " + testName
        );

        check(
                kmp.equals(expected),
                "KMP - " + testName
        );

        check(
                rabin.equals(expected),
                "Rabin-Karp - " + testName
        );
    }

    // Kasai LCP tests
    private static void testKasai() {

        String text = "banana";

        int[] expectedSuffixArray = {5, 3, 1, 0, 4, 2};
        int[] expectedLCP = {0, 1, 3, 0, 0, 2};

        int[] suffixArray = SuffixArray.build(text);

        int[] actualLCP =
                KasaiLCP.build(text, suffixArray);

        check(
                Arrays.equals(suffixArray, expectedSuffixArray),
                "Kasai suffix array"
        );

        check(
                Arrays.equals(actualLCP, expectedLCP),
                "Kasai LCP array"
        );

        System.out.println("Kasai LCP: PASSED");
    }

    // Common assertion method
    private static void check(boolean condition, String testName) {

        if (!condition) {
            throw new RuntimeException(
                    "TEST FAILED: " + testName
            );
        }
    }
}
