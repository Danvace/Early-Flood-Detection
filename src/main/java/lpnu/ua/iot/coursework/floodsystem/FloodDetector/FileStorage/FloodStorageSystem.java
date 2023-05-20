package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class FloodStorageSystem {

    public static final String PATH_TO_FILES = "src/main/resources/floods/";

    public Integer getLastId() throws IOException {
        File directory = new File(PATH_TO_FILES);
        File[] files = directory.listFiles();

        List<Integer> id_applicants = new ArrayList<>();

        if (files != null) {
            for (File file : files) {

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    String lastLine = "";
                    while ((line = br.readLine()) != null) {
                        lastLine = line;
                    }
                    String[] values = lastLine.split(", ", 2);
                    int id_applicant = Integer.parseInt(values[0].trim());
                    id_applicants.add(id_applicant);
                }
            }
        }

        return id_applicants.isEmpty() ? 0 : Collections.max(id_applicants);
    }

    public void writeToCSV(final FloodDetector floodDetector) throws IOException {
        String fileName = PATH_TO_FILES +
                        floodDetector.getClass().getSimpleName() +
                        "-" +
                        floodDetector.getDateOfMeasurement() +
                        ".csv";

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
