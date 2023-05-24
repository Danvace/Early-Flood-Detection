package lpnu.ua.iot.coursework.floodsystem.FloodDetector.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class FloodDetector {

    public static final String HEADER = "id, pointOfMeasurement, levelOfWater, gps, dateOfMeasurement";
    @Id
    private Integer id;

    @NotEmpty(message = "the point of measurement is required")
    private String pointOfMeasurement;

    @Min(value = 0, message = "the water level should be positive")
    private double levelOfWater;

    @NotEmpty(message = "the gps is required")
    private String gps;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "the date of measurement should be in format yyyy-mm-dd")
    private String dateOfMeasurement;

    public FloodDetector(String pointOfMeasurement, double levelOfWater, String gps, String dateOfMeasurement) {
        this.pointOfMeasurement = pointOfMeasurement;
        this.levelOfWater = levelOfWater;
        this.gps = gps;
        this.dateOfMeasurement = dateOfMeasurement;
    }

    public String getHeaders() {
        return HEADER;
    }

    public String toCSV() {
        return id + ";" + pointOfMeasurement + ";" + levelOfWater + ";" + gps + ";" + dateOfMeasurement;
    }
}
