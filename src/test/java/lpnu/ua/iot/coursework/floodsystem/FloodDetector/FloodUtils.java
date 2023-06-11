package lpnu.ua.iot.coursework.floodsystem.FloodDetector;


import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.utility.FloodUtility;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloodUtils {

    public static final String PATH_TO_FILE_1 = "src/main/resources/testFiles/";

    public static List<String> linesToFirstTestFile1() {
        var lines = new ArrayList<String>();
        lines.add("id, pointOfMeasurement, levelOfWater, gps, dateOfMeasurement");
        lines.add("1;Радивилів;2.2;40.7128° N, 74.0060° W;2023-05-31");
        lines.add("2;Львів;2.2;40.7128° N, 74.0060° W;2023-05-31");
        lines.add("3;Рівне;2.2;40.7128° N, 74.0060° W;2023-05-31");
        lines.add("4;Київ;2.2;40.7128° N, 74.0060° W;2023-05-31");
        lines.add("5;Ужгород;2.2;40.7128° N, 74.0060° W;2023-05-31");
        return lines;
    }


    public static void writeToFile(final String pathToFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(pathToFile +
                                "FloodDetector" +
                                "-" +
                                FloodUtility.getCurrentDate() +
                                ".csv", true), StandardCharsets.UTF_8))) {

            for (String line : linesToFirstTestFile1()) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void deleteFiles() throws IOException {
        Files.deleteIfExists(Path.of(PATH_TO_FILE_1 + "FloodDetector-" + FloodUtility.getCurrentDate() + ".csv"));
    }

    public static HashMap<Integer, FloodDetector> floodDetectorHashMap(){
        var floodDetector1 = new FloodDetector("Радивилів",2.2,"40.7128° N, 74.0060° W","2023-05-31");
        floodDetector1.setId(1);
        var floodDetector2 = new FloodDetector("Львів",2.2,"40.7128° N, 74.0060° W","2023-05-31");
        floodDetector2.setId(2);
        var floodDetector3 = new FloodDetector("Рівне",2.2,"40.7128° N, 74.0060° W","2023-05-31");
        floodDetector3.setId(3);
        var floodDetector4 = new FloodDetector("Київ",2.2,"40.7128° N, 74.0060° W","2023-05-31");
        floodDetector4.setId(4);
        var floodDetector5 = new FloodDetector("Ужгород",2.2,"40.7128° N, 74.0060° W","2023-05-31");
        floodDetector5.setId(5);
        HashMap<Integer,FloodDetector> floodDetectorHashMap = new HashMap<>();
        floodDetectorHashMap.put(1,floodDetector1);
        floodDetectorHashMap.put(2,floodDetector2);
        floodDetectorHashMap.put(3,floodDetector3);
        floodDetectorHashMap.put(4,floodDetector4);
        floodDetectorHashMap.put(5,floodDetector5);
        return floodDetectorHashMap;
    }

}
