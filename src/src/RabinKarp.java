
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {

    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007L;

    public static class SearchResult {
        public final List<Integer> indices;
        public final long verificationComparisons;

        public SearchResult(
                List<Integer> indices,
                long verificationComparisons
        ) {
            this.indices = indices;
            this.verificationComparisons = verificationComparisons;
        }
    }

    // Original method — keeps compatibility with existing code
    public static List<Integer> search(String text, String pattern) {
        return searchWithStats(text, pattern).indices;
    }

    public static SearchResult searchWithStats(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return new SearchResult(result, 0);
        }

        if (m > n) {
            return new SearchResult(result, 0);
        }

        long patternHash = 0;
        long windowHash = 0;
        long highestPower = 1;

        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % MOD;
        }

        // Calculate initial hashes
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }

        long verificationComparisons = 0;

        // Slide the window through the text
        for (int i = 0; i <= n - m; i++) {

            if (patternHash == windowHash) {
                boolean match = true;

                for (int j = 0; j < m; j++) {
                    verificationComparisons++;

                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    result.add(i);
                }
            }

            // Calculate hash for the next window
            if (i < n - m) {
                windowHash = (
                        windowHash
                                - text.charAt(i) * highestPower % MOD
                                + MOD
                ) % MOD;

                windowHash = (
                        windowHash * BASE + text.charAt(i + m)
                ) % MOD;
            }
        }

        return new SearchResult(result, verificationComparisons);
    }
}
