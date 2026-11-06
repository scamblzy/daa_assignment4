import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ResultWriter {

    // Create file and write CSV header
    public static void writeHeader(
            String fileName,
            String header
    ) throws IOException {

        Path path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        Files.writeString(
                path,
                header + System.lineSeparator()
        );
    }

    // Append experiment result
    public static void writeLine(
            String fileName,
            String line
    ) throws IOException {

        Path path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {

            writer.write(line);
            writer.newLine();
        }
    }
}
