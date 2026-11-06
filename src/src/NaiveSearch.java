
import java.util.ArrayList;
import java.util.List;

public class NaiveSearch {

    public static class SearchResult {
        public final List<Integer> indices;
        public final long comparisons;

        public SearchResult(List<Integer> indices, long comparisons) {
            this.indices = indices;
            this.comparisons = comparisons;
        }
    }

    // Original method — keeps compatibility with existing code
    public static List<Integer> search(String text, String pattern) {
        return searchWithStats(text, pattern).indices;
    }

    // Search with character comparison counter
    public static SearchResult searchWithStats(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        long comparisons = 0;

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return new SearchResult(result, comparisons);
        }

        if (m > n) {
            return new SearchResult(result, comparisons);
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m) {
                comparisons++;

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }

                j++;
            }

            if (j == m) {
                result.add(i);
            }
        }

        return new SearchResult(result, comparisons);
    }
}
