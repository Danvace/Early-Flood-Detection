package lpnu.ua.iot.coursework.floodsystem.FloodDetector.service;

import lombok.Getter;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage.FloodStorageSystem;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Getter
public class FloodService {

    private final Map<Integer, FloodDetector> floodDetectorMap;

    private final FloodStorageSystem floodStorageSystem;

    private final AtomicInteger nextAvailable;


    @Autowired
    public FloodService(final @NotNull FloodStorageSystem floodStorageSystem) throws IOException {
        this.floodStorageSystem = floodStorageSystem;
        this.nextAvailable = new AtomicInteger(floodStorageSystem.getLastId());
        this.floodDetectorMap = floodStorageSystem.getFloodsFromCSV();
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

    public void postFlood(final @NotNull FloodDetector floodDetector) throws IOException {
        floodDetector.setId(nextAvailable.incrementAndGet());
        floodDetectorMap.put(floodDetector.getId(), floodDetector);
        floodStorageSystem.writeToCSV(floodDetector);
    }

    public FloodDetector putFlood(final Integer id, final @NotNull FloodDetector floodDetector) {
        floodDetector.setId(id);
        floodDetectorMap.replace(id, floodDetector);
        return floodDetectorMap.replace(id, floodDetector);
    }

    public void deleteFlood(final Integer id) {
        floodDetectorMap.remove(id);
    }
}
