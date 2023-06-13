package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.utility.FloodUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Component
public final class FloodStorageSystem {

    public static final String PATH_TO_FILES = "src/main/resources/floods/";

    public Integer getLastId(final String pathToFile) throws IOException {
        File[] files = getListOfFiles(pathToFile);

        List<Integer> idApplicants = new ArrayList<>();

        if (files != null) {
            for (File file : files) {

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8))) {

                    String line;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";", 2);
                        try {
                            int idApplicant = Integer.parseInt(values[0].trim());
                            idApplicants.add(idApplicant);
                        } catch (NumberFormatException e) {
                            System.out.println("problem with parsing");
                        }
                    }
                }
            }
        }

        return idApplicants.isEmpty() ? 0 : Collections.max(idApplicants);
    }

    public void writeToCSV(final @NotNull FloodDetector floodDetector, final String pathToFile) throws IOException {
        String fileName = pathToFile + floodDetector.getClass().getSimpleName() + "-" + FloodUtility.getCurrentDate() + ".csv";

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

    public void deleteFloodBy(final Integer id, final String pathToFile) throws IOException {
        File tempFile = getTempFile(id, pathToFile);
        if (tempFile == null) {
            return;
        }
        List<String> lines = getLinesToWrite(tempFile, id);
        String name = tempFile.getName();
        boolean deletionSuccessful = tempFile.delete();
        if (deletionSuccessful) {

            try (var bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(pathToFile + name), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
        } else {
            throw new RuntimeException("File was not deleted");
        }
    }

    public void putFlood(final Integer id, final FloodDetector floodDetector, final String pathToFile) throws IOException {
        File tempFile = getTempFile(id, pathToFile);
        if (tempFile == null) {
            return;
        }
        List<String> lines = getLinesToWrite(tempFile, id);
        String name = tempFile.getName();
        boolean deletingSuccessful = tempFile.delete();
        if (deletingSuccessful) {
            try (var bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(pathToFile + name), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedWriter.write(floodDetector.toCSV());
                bufferedWriter.newLine();
            }
        } else {
            throw new RuntimeException("File was not deleted");
        }
    }

    public Map<Integer, FloodDetector> getFloodsFromCSV(final String pathToFile) throws IOException {
        File[] files = getListOfFiles(pathToFile);
        Map<Integer, FloodDetector> floodDetectorMap = new HashMap<>();
        String line;
        if (files != null) {
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8))) {

                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        FloodDetector flood = FloodUtility.createObjectFromString(line);
                        floodDetectorMap.put(flood.getId(), flood);
                    }
                }
            }
        }
        return floodDetectorMap;
    }

    private File getTempFile(final Integer id, final String pathToFile) throws IOException {
        File[] files = getListOfFiles(pathToFile);
        return getFileForChanging(files, id);
    }

    private List<String> getLinesToWrite(final File file, final Integer id) throws IOException {
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

    private File getFileForChanging(final File[] files, final Integer id) throws IOException {
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

    private File[] getListOfFiles(final String pathToFile) {
        File folder = new File(pathToFile);
        LocalDate currentDate = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(currentDate);

        File[] allFiles = folder.listFiles();
        if (allFiles == null) {
            return null;
        }

        int startPosition = 14;
        int endPosition = 24;

        return Arrays.stream(allFiles).filter(file -> {
            String fileName = file.getName();
            if (fileName.matches("FloodDetector-\\d{4}-\\d{2}-\\d{2}\\.csv")) {
                String fileDateStr = fileName.substring(startPosition, endPosition);
                LocalDate fileDate = LocalDate.parse(fileDateStr);
                YearMonth fileYearMonth = YearMonth.from(fileDate);
                return fileYearMonth.equals(currentYearMonth);
            }
            return false;
        }).toArray(File[]::new);
    }

}
