package lpnu.ua.iot.coursework.floodsystem.FloodDetector.writter;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FloodWriter {

    public void writeToCSV(final FloodDetector floodDetector) throws IOException {
        String className = getClass().getSimpleName();
        String fileName = "src/main/resources/" + className + floodDetector.getDateOfMeasurement() + ".csv";

        boolean fileExists = Files.exists(Path.of(fileName));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            if (!fileExists) {
                writer.write(floodDetector.getHeaders());
                writer.newLine();
            }

            writer.write(floodDetector.toCSV());
            writer.newLine();
        }
    }
}
