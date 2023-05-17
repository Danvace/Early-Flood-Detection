package lpnu.ua.iot.coursework.floodsystem.FloodDetector.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FloodDetector {

    public static String HEADER = "id, pointOfMeasurement, levelOfWater, gps, dateOfMeasurement";

    private int id;

    private String pointOfMeasurement;

    private double levelOfWater;

    private String gps;
    //    @Pattern("/^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$/gm")
    private String dateOfMeasurement;

    public FloodDetector(String pointOfMeasurement, double levelOfWater, String gps, String dateOfMeasurement) {
        this.pointOfMeasurement = pointOfMeasurement;
        this.levelOfWater = levelOfWater;
        this.gps = gps;
        this.dateOfMeasurement = dateOfMeasurement;
    }

    String getHeaders() {
        return HEADER;
    }

    String toCSV() {
        return id + ", " + pointOfMeasurement + ", " + levelOfWater + ", " + gps + ", " + dateOfMeasurement;
    }
}
