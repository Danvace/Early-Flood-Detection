package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.DateGetter.DateGetter;
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
        File[] files = getListOfFiles();

        List<Integer> id_applicants = new ArrayList<>();

        if (files != null) {
            for (File file : files) {

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";", 2);
                        try {
                            int id_applicant = Integer.parseInt(values[0].trim());
                            id_applicants.add(id_applicant);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }

        return id_applicants.isEmpty() ? 0 : Collections.max(id_applicants);
    }

    public void writeToCSV(final @NotNull FloodDetector floodDetector) throws IOException {
        String fileName = PATH_TO_FILES +
                floodDetector.getClass().getSimpleName() +
                "-" +
                DateGetter.getCurrentDate() +
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

    public void deleteFloodBy(final Integer id) throws IOException {
        File tempFile = getTempFile(id);
        if (tempFile == null) {
            return;
        }
        List<String> lines = getLinesToWrite(tempFile, id);
        String name = tempFile.getName();
        tempFile.delete();
        try (var bufferedWriter = new BufferedWriter(new FileWriter(PATH_TO_FILES + name))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    public void putFlood(final Integer id, final FloodDetector floodDetector) throws IOException {
        File tempFile = getTempFile(id);
        if (tempFile == null) {
            return;
        }
        List<String> lines = getLinesToWrite(tempFile, id);
        String name = tempFile.getName();
        tempFile.delete();
        try (var bufferedWriter = new BufferedWriter(new FileWriter(PATH_TO_FILES + name))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.write(floodDetector.toCSV());
            bufferedWriter.newLine();
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

    public File getTempFile(final Integer id) throws IOException {
        File[] files = getListOfFiles();
        return getFileForChanging(files, id);
    }

    public List<String> getLinesToWrite(final File file, final Integer id) throws IOException {
        List<String> lines = new ArrayList<>();
        try (var bufferedReader = new BufferedReader(new FileReader(file))) {

            String header = bufferedReader.readLine();
            lines.add(header);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String[] parts = currentLine.split(";", 2);
                int curId = Integer.parseInt(parts[0]);

                if (curId == id) {
                    continue;
                }
                lines.add(currentLine);
            }

        }
        return lines;
    }

    public File getFileForChanging(File[] files, final Integer id) throws IOException {
        File tempFile = null;
        if (files != null) {
            for (File file : files) {
                try (var bufferedReader = new BufferedReader(new FileReader(file))) {
                    bufferedReader.readLine();
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        String[] parts = currentLine.split(";", 2);
                        int curId = Integer.parseInt(parts[0]);

                        if (curId == id) {
                            tempFile = file;
                            break;
                        }
                    }
                }
            }
        }
        return tempFile;
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
