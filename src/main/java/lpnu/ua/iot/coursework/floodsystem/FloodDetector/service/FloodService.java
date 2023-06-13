package lpnu.ua.iot.coursework.floodsystem.FloodDetector.service;

import lombok.Getter;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.FileStorage.FloodStorageSystem;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
        this.nextAvailable = new AtomicInteger(floodStorageSystem.getLastId(FloodStorageSystem.PATH_TO_FILES));
        this.floodDetectorMap = floodStorageSystem.getFloodsFromCSV(FloodStorageSystem.PATH_TO_FILES);
    }

    public Map<Integer, FloodDetector> getMap() {
        return new HashMap<>(floodDetectorMap);
    }

    public List<FloodDetector> getAllFloods() {
        return new LinkedList<>(floodDetectorMap.values());
    }

    public FloodDetector getFlood(final Integer id) {
        return floodDetectorMap.get(id);
    }

    public void postFlood(final @NotNull FloodDetector floodDetector) throws IOException {
        floodDetector.setId(nextAvailable.incrementAndGet());
        floodDetectorMap.put(floodDetector.getId(), floodDetector);
        floodStorageSystem.writeToCSV(floodDetector, FloodStorageSystem.PATH_TO_FILES);
    }

    public FloodDetector putFlood(final Integer id, final @NotNull FloodDetector floodDetector) throws IOException {
        floodDetector.setId(id);
        floodDetectorMap.replace(id, floodDetector);
        floodStorageSystem.putFlood(id, floodDetector, FloodStorageSystem.PATH_TO_FILES);
        return floodDetectorMap.replace(id, floodDetector);
    }

    public void deleteFlood(final Integer id) throws IOException {
        floodDetectorMap.remove(id);
        floodStorageSystem.deleteFloodBy(id, FloodStorageSystem.PATH_TO_FILES);
    }
}
