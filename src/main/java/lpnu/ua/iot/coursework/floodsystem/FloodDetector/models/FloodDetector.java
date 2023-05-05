package lpnu.ua.iot.coursework.floodsystem.FloodDetector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloodDetector {

    public static String HEADER = "id, pointOfMeasurement, levelOfWater, gps, dateOfMeasurement";

    private int id;

    private String pointOfMeasurement;

    private double levelOfWater;

    private String gps;

    private String dateOfMeasurement;

    String getHeaders() {
        return HEADER;
    }

    String toCSV() {
        return id + ", " + pointOfMeasurement + ", " + levelOfWater + ", " + gps + ", " + dateOfMeasurement;
    }
}
