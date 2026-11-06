import java.util.Random;

public class StringGenerator {

    private static final String ALPHABET =
            "abcdefghijklmnopqrstuvwxyz";

    // Generate random string
    public static String randomString(int length, long seed) {

        Random random = new Random(seed);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(ALPHABET.length());

            result.append(ALPHABET.charAt(index));
        }

        return result.toString();
    }

    // Generate a pattern that exists in the text
    public static String presentPattern(
            String text,
            int patternLength,
            long seed
    ) {

        Random random = new Random(seed);

        int start = random.nextInt(
                text.length() - patternLength + 1
        );

        return text.substring(start, start + patternLength);
    }

    // Generate a pattern that does not exist in the text
    public static String absentPattern(int length) {

        return "{".repeat(length);
    }
}
