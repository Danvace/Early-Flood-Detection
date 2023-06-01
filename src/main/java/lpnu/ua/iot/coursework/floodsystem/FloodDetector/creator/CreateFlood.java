package lpnu.ua.iot.coursework.floodsystem.FloodDetector.creator;

import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.jetbrains.annotations.NotNull;

public class CreateFlood {
    public static @NotNull FloodDetector createObjectFromString(final @NotNull String stringObject) {
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
