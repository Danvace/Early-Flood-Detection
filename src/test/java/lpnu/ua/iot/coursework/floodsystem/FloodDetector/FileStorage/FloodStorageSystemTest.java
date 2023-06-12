package lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.FloodUtils;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.utility.FloodUtility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static lpnu.ua.iot.coursework.floodsystem.FloodDetector.FloodUtils.PATH_TO_FILE_1;
import static lpnu.ua.iot.coursework.floodsystem.FloodDetector.FloodUtils.floodDetectorHashMap;

class FloodStorageSystemTest {

    public static final String PATH_TO_TEST_FILES = "src/main/resources/testFiles";

    public static final String PATH_TO_EXPECTED_FILE = "src/main/resources/testFiles/expectedAfterWriting.csv";

    public static final String PATH_TO_EXPECTED_TO_DELETE_FILE = "src/main/resources/testFiles/expectedAfterDelete";

    public static final String PATH_TO_EXPECTED_TO_PUT_FILE = "src/main/resources/testFiles/expectedAfterPut.csv";

    public final FloodStorageSystem floodStorageSystem = new FloodStorageSystem();

    @BeforeEach
    void setUp() throws IOException {
        FloodUtils.deleteFiles();

    }

    @AfterAll
    public static void tearDown() throws IOException {
        FloodUtils.deleteFiles();
    }

    @Test
    void getLastId() throws IOException {
        FloodStorageSystem floodStorageSystem = new FloodStorageSystem();
        FloodUtils.writeToFile(PATH_TO_FILE_1);
        Integer lastId = floodStorageSystem.getLastId(PATH_TO_TEST_FILES);
        Assertions.assertEquals(5, lastId);
    }

    @Test
    void writeToCSV() throws IOException {

        FloodDetector floodDetector = new FloodDetector("Ужгород", 2.2, "40.7128° N, 74.0060° W", "2023-05-31");
        floodDetector.setId(6);
        FloodUtils.writeToFile(PATH_TO_FILE_1);
        floodStorageSystem.writeToCSV(floodDetector, PATH_TO_FILE_1);

        Path resultFile = new File(PATH_TO_FILE_1 + "FloodDetector-" + FloodUtility.getCurrentDate() + ".csv").toPath();
        Path expectedFile = new File(PATH_TO_EXPECTED_FILE).toPath();

        List<String> result = Files.readAllLines(resultFile);
        List<String> expected = Files.readAllLines(expectedFile);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void deleteFloodBy() throws IOException {
        FloodUtils.writeToFile(PATH_TO_FILE_1);
        floodStorageSystem.deleteFloodBy(3, PATH_TO_FILE_1);
        Path resultFile = new File(PATH_TO_FILE_1
                + "FloodDetector-"
                + FloodUtility.getCurrentDate()
                + ".csv").toPath();
        Path expectedFile = new File(PATH_TO_EXPECTED_TO_DELETE_FILE).toPath();

        List<String> result = Files.readAllLines(resultFile);
        List<String> expected = Files.readAllLines(expectedFile);

        Assertions.assertEquals(result, expected);

    }

    @Test
    void putFlood() throws IOException {

        FloodUtils.writeToFile(PATH_TO_FILE_1);

        FloodDetector floodDetector = new FloodDetector("Теслугів", 2.2, "40.7128° N, 74.0060° W", "2023-05-31");
        floodDetector.setId(4);
        floodStorageSystem.putFlood(4, floodDetector, PATH_TO_FILE_1);
        Path resultFile = new File(PATH_TO_FILE_1
                + "FloodDetector-"
                + FloodUtility.getCurrentDate()
                + ".csv").toPath();
        Path expectedFile = new File(PATH_TO_EXPECTED_TO_PUT_FILE).toPath();

        List<String> result = Files.readAllLines(resultFile);
        List<String> expected = Files.readAllLines(expectedFile);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void getFloodsFromCSV() throws IOException {
        FloodUtils.writeToFile(PATH_TO_FILE_1);
        Map<Integer, FloodDetector> floodDetectorMap = floodStorageSystem.getFloodsFromCSV(PATH_TO_FILE_1);
        Map<Integer, FloodDetector> floodExpected = floodDetectorHashMap();
        Assertions.assertEquals(floodExpected, floodDetectorMap);
    }

}