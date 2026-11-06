
import java.util.ArrayList;
import java.util.List;

public class KMP {

    public static class SearchResult {
        public final List<Integer> indices;
        public final long searchComparisons;
        public final long preprocessingComparisons;

        public SearchResult(
                List<Integer> indices,
                long searchComparisons,
                long preprocessingComparisons
        ) {
            this.indices = indices;
            this.searchComparisons = searchComparisons;
            this.preprocessingComparisons = preprocessingComparisons;
        }
    }

    // Original method — keeps compatibility with existing code
    public static List<Integer> search(String text, String pattern) {
        return searchWithStats(text, pattern).indices;
    }

    // KMP search with comparison counters
    public static SearchResult searchWithStats(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        // Empty pattern matches every position
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }

            return new SearchResult(result, 0, 0);
        }

        // Pattern is longer than text
        if (m > n) {
            return new SearchResult(result, 0, 0);
        }

        // Build LPS array
        long preprocessingComparisons = 0;

        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            preprocessingComparisons++;

            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        // Search text using KMP
        long searchComparisons = 0;

        int textIndex = 0;
        int patternIndex = 0;

        while (textIndex < n) {
            searchComparisons++;

            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++;
                patternIndex++;

                if (patternIndex == m) {
                    result.add(textIndex - patternIndex);
                    patternIndex = lps[patternIndex - 1];
                }
            } else {
                if (patternIndex != 0) {
                    patternIndex = lps[patternIndex - 1];
                } else {
                    textIndex++;
                }
            }
        }

        return new SearchResult(
                result,
                searchComparisons,
                preprocessingComparisons
        );
    }
}
