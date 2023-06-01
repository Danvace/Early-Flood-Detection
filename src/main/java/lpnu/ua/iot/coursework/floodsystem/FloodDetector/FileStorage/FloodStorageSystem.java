package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.DateGetter.DateGetter;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.creator.CreateFlood;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8))) {

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

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {

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
        boolean deletionSuccessful = tempFile.delete();
        if (deletionSuccessful) {

            try (var bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(PATH_TO_FILES + name), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
        }else {
            throw new RuntimeException("File was not deleted");
        }
    }

    public void putFlood(final Integer id, final FloodDetector floodDetector) throws IOException {
        File tempFile = getTempFile(id);
        if (tempFile == null) {
            return;
        }
        List<String> lines = getLinesToWrite(tempFile, id);
        String name = tempFile.getName();
        boolean deletingSuccessful =  tempFile.delete();
        if(deletingSuccessful) {
            try (var bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(PATH_TO_FILES + name), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedWriter.write(floodDetector.toCSV());
                bufferedWriter.newLine();
            }
        }else {
            throw new RuntimeException("File was not deleted");
        }
    }

    public Map<Integer, FloodDetector> getFloodsFromCSV() throws IOException {
        File[] files = getListOfFiles();
        Map<Integer, FloodDetector> floodDetectorMap = new HashMap<>();
        String line;
        if (files != null) {
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8))) {

                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        FloodDetector flood = CreateFlood.createObjectFromString(line);
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
        try (var bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

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
                try (var bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8))) {
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

    private File[] getListOfFiles() { // змінити логіку для отримання файлів , які створені в цьому місяці
        File folder = new File(PATH_TO_FILES);
        return folder.listFiles();
    }
}
