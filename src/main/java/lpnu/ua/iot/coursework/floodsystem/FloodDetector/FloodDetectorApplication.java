package lpnu.ua.iot.coursework.floodsystem.FloodDetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FloodDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloodDetectorApplication.class, args);
    }

}
