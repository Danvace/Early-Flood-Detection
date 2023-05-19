package lpnu.ua.iot.coursework.floodsystem.FloodDetector.service;

import lombok.Getter;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.reader.CSVReader;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.writter.FloodWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class FloodService {

    private final Map<Integer, FloodDetector> floodDetectorMap = new HashMap<>();

    private Integer nextAvailable = 1;

    private final FloodWriter floodWriter;

    @Autowired
    public FloodService(FloodWriter floodWriter, CSVReader csvReader) {
        this.floodWriter = floodWriter;
    }

    public Map<Integer, FloodDetector> getMap() {
        return new HashMap<>(floodDetectorMap);
    }

    public List<FloodDetector> getAllFloods() {
        return floodDetectorMap.values().stream().toList();
    }

    public FloodDetector getFlood(Integer id) {
        return floodDetectorMap.get(id);
    }

    public void postFlood(final FloodDetector floodDetector) throws IOException {
        floodDetector.setId(nextAvailable);
        floodDetectorMap.put(nextAvailable++, floodDetector);
        floodWriter.writeToCSV(floodDetector);
    }

    public FloodDetector putFlood(final Integer id, final FloodDetector floodDetector) {
        floodDetector.setId(id);
        floodDetectorMap.replace(id, floodDetector);
        return floodDetectorMap.replace(id, floodDetector);
    }

    public void deleteFlood(final Integer id) {
        floodDetectorMap.remove(id);
    }
}
