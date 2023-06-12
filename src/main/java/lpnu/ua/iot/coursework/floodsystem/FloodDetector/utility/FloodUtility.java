package lpnu.ua.iot.coursework.floodsystem.FloodDetector.utility;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FloodUtility {
    public static FloodDetector createObjectFromString(final String stringObject) {
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

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
