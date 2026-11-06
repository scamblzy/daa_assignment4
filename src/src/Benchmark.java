
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Benchmark {

    private static final int REPEATS = 5;

    public static void runAll() throws IOException {
        runDPExperiments();
        runMatchingExperiments();
        runKasaiExperiments();

        System.out.println("All experiments completed!");
    }

    // LCS and Edit Distance experiments
    private static void runDPExperiments() throws IOException {

        String file = "results/lcs_edit_distance.csv";

        ResultWriter.writeHeader(
                file,
                "algorithm,size,average_time_ns"
        );

        int[] sizes = {100, 500, 1000};

        for (int size : sizes) {

            String s1 = StringGenerator.randomString(size, 42);
            String s2 = StringGenerator.randomString(size, 43);

            long lcsTime = averageTime(
                    () -> LCS.findLength(s1, s2)
            );

            long editTime = averageTime(
                    () -> EditDistance.calculate(s1, s2)
            );

            ResultWriter.writeLine(
                    file,
                    "LCS," + size + "," + lcsTime
            );

            ResultWriter.writeLine(
                    file,
                    "EditDistance," + size + "," + editTime
            );

            System.out.println(
                    "DP size: " + size
                            + " | LCS: " + lcsTime
                            + " | Edit Distance: " + editTime
            );
        }
    }

    // Naive, KMP and Rabin-Karp experiments
    private static void runMatchingExperiments()
            throws IOException {

        String file = "results/string_matching.csv";

        ResultWriter.writeHeader(
                file,
                "algorithm,text_length,pattern_length,present,"
                        + "average_time_ns,search_comparisons,"
                        + "preprocessing_comparisons"
        );

        int[] sizes = {10000, 50000, 100000};

        for (int size : sizes) {

            String text = StringGenerator.randomString(size, 42);

            for (boolean present : new boolean[]{true, false}) {

                String pattern;

                if (present) {
                    pattern = StringGenerator.presentPattern(
                            text, 10, 77
                    );
                } else {
                    pattern = StringGenerator.absentPattern(10);
                }

                // Correctness check
                List<Integer> naive =
                        NaiveSearch.search(text, pattern);

                List<Integer> kmp =
                        KMP.search(text, pattern);

                List<Integer> rabinKarp =
                        RabinKarp.search(text, pattern);

                if (!naive.equals(kmp)
                        || !naive.equals(rabinKarp)) {

                    throw new RuntimeException(
                            "Algorithms returned different results!"
                    );
                }

                // Measure execution time
                long naiveTime = averageTime(
                        () -> NaiveSearch.search(text, pattern)
                );

                long kmpTime = averageTime(
                        () -> KMP.search(text, pattern)
                );

                long rabinTime = averageTime(
                        () -> RabinKarp.search(text, pattern)
                );

                // Collect comparison statistics separately
                NaiveSearch.SearchResult naiveStats =
                        NaiveSearch.searchWithStats(text, pattern);

                KMP.SearchResult kmpStats =
                        KMP.searchWithStats(text, pattern);

                RabinKarp.SearchResult rabinStats =
                        RabinKarp.searchWithStats(text, pattern);

                ResultWriter.writeLine(
                        file,
                        "Naive," + size + ","
                                + pattern.length() + ","
                                + present + "," + naiveTime + ","
                                + naiveStats.comparisons + ",0"
                );

                ResultWriter.writeLine(
                        file,
                        "KMP," + size + ","
                                + pattern.length() + ","
                                + present + "," + kmpTime + ","
                                + kmpStats.searchComparisons + ","
                                + kmpStats.preprocessingComparisons
                );

                ResultWriter.writeLine(
                        file,
                        "RabinKarp," + size + ","
                                + pattern.length() + ","
                                + present + "," + rabinTime + ","
                                + rabinStats.verificationComparisons
                                + ",0"
                );

                System.out.println(
                        "Matching size: " + size
                                + " | Present: " + present
                                + " | Naive comparisons: "
                                + naiveStats.comparisons
                                + " | KMP comparisons: "
                                + kmpStats.searchComparisons
                                + " | Rabin-Karp comparisons: "
                                + rabinStats.verificationComparisons
                );
            }
        }
    }

    // Kasai experiments
    private static void runKasaiExperiments()
            throws IOException {

        String file = "results/kasai.csv";

        ResultWriter.writeHeader(
                file,
                "algorithm,size,average_time_ns"
        );

        int[] sizes = {1000, 5000, 10000};

        for (int size : sizes) {

            String text = StringGenerator.randomString(size, 42);

            // Suffix array construction is outside timed region
            int[] suffixArray = buildSuffixArray(text);

            long kasaiTime = averageTime(
                    () -> KasaiLCP.build(text, suffixArray)
            );

            ResultWriter.writeLine(
                    file,
                    "Kasai," + size + "," + kasaiTime
            );

            System.out.println(
                    "Kasai size: " + size
                            + " | Time: " + kasaiTime + " ns"
            );
        }
    }

    // Measure average execution time
    private static long averageTime(Runnable task) {

        // Warm-up
        task.run();

        long totalTime = 0;

        for (int i = 0; i < REPEATS; i++) {

            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();

            totalTime += end - start;
        }

        return totalTime / REPEATS;
    }

    // Prefix-doubling suffix array construction
    private static int[] buildSuffixArray(String text) {

        int n = text.length();

        if (n == 0) {
            return new int[0];
        }

        Integer[] order = new Integer[n];
        int[] rank = new int[n];
        int[] nextRank = new int[n];

        for (int i = 0; i < n; i++) {
            order[i] = i;
            rank[i] = text.charAt(i);
        }

        for (int width = 1; width < n; width *= 2) {

            final int[] currentRank = rank;
            final int w = width;

            Arrays.sort(order, (a, b) -> {

                if (currentRank[a] != currentRank[b]) {
                    return Integer.compare(
                            currentRank[a], currentRank[b]
                    );
                }

                int rankA = a + w < n
                        ? currentRank[a + w] : -1;

                int rankB = b + w < n
                        ? currentRank[b + w] : -1;

                return Integer.compare(rankA, rankB);
            });

            nextRank[order[0]] = 0;

            for (int i = 1; i < n; i++) {

                int a = order[i - 1];
                int b = order[i];

                boolean different =
                        currentRank[a] != currentRank[b]
                                || (a + w < n
                                ? currentRank[a + w] : -1)
                                != (b + w < n
                                ? currentRank[b + w] : -1);

                nextRank[b] = nextRank[a]
                        + (different ? 1 : 0);
            }

            System.arraycopy(
                    nextRank, 0, rank, 0, n
            );

            if (rank[order[n - 1]] == n - 1) {
                break;
            }

            if (width > n / 2) {
                break;
            }
        }

        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            result[i] = order[i];
        }

        return result;
    }
}
