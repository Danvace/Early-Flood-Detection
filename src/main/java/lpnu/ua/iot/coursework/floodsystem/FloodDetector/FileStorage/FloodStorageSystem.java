package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
                    String[] values = lastLine.split(";", 2);
                    int id_applicant = Integer.parseInt(values[0].trim());
                    id_applicants.add(id_applicant);
                }
            }
        }

        return id_applicants.isEmpty() ? 0 : Collections.max(id_applicants);
    }

    public void writeToCSV(final @NotNull FloodDetector floodDetector) throws IOException {
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

    public Map<Integer, FloodDetector> getFloodsFromCSV() throws IOException {
        File[] files = getListOfFiles();
        Map<Integer, FloodDetector> floodDetectorMap = new HashMap<>();
        String line;
        if (files != null) {
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        FloodDetector flood = createObjectFromString(line);
                        floodDetectorMap.put(flood.getId(), flood);
                    }
                }
            }
        }
        return floodDetectorMap;
    }

    private File[] getListOfFiles() {
        File folder = new File(PATH_TO_FILES);
        return folder.listFiles();
    }

    private @NotNull FloodDetector createObjectFromString(final @NotNull String stringObject) {
        String[] arrayObject = stringObject.split(";");

        Integer id = Integer.parseInt(arrayObject[0].trim());
        String pointOfMeasurement = arrayObject[1].trim();
        double levelOfWater = Double.parseDouble(arrayObject[2].trim());
        String gps = arrayObject[3];
        String dateOfMeasurement = arrayObject[4];

        var floodDetector = new FloodDetector(pointOfMeasurement, levelOfWater, gps, dateOfMeasurement);
        floodDetector.setId(id);

        return floodDetector;
    }
}
