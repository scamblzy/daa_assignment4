public class KasaiLCP {

    public static int[] build(String text, int[] suffixArray) {

        int n = text.length();

        int[] rank = new int[n];
        int[] lcp = new int[n];

        // Build inverse suffix array
        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int commonLength = 0;

        // Calculate LCP values
        for (int i = 0; i < n; i++) {

            int r = rank[i];

            if (r == 0) {
                commonLength = 0;
                continue;
            }

            int j = suffixArray[r - 1];

            while (i + commonLength < n &&
                    j + commonLength < n &&
                    text.charAt(i + commonLength) ==
                            text.charAt(j + commonLength)) {

                commonLength++;
            }

            lcp[r] = commonLength;

            if (commonLength > 0) {
                commonLength--;
            }
        }

        return lcp;
    }
}
