package lpnu.ua.iot.coursework.floodsystem.FloodDetector.controller;

import jakarta.validation.Valid;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.models.FloodDetector;
import lpnu.ua.iot.coursework.floodsystem.FloodDetector.service.FloodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "flood")
public class FloodController {
    private final FloodService floodService;

    @Autowired
    public FloodController(final FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping
    public ResponseEntity<List<FloodDetector>> getAll() {
        return ResponseEntity.ok(floodService.getAllFloods());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FloodDetector> get(@PathVariable final Integer id) {
        FloodDetector floodDetector = floodService.getFlood(id);
        if (floodDetector == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(floodDetector);
    }

    @PostMapping
    public ResponseEntity<FloodDetector> post(@Valid @RequestBody final FloodDetector floodDetector, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body((FloodDetector) errors);
        }
        floodService.postFlood(floodDetector);
        return ResponseEntity.status(HttpStatus.CREATED).body(floodDetector);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<FloodDetector> put(@Valid @RequestBody final FloodDetector floodDetector, @PathVariable final Integer id) throws IOException {
        floodService.putFlood(id, floodDetector);
        if (floodService.putFlood(id, floodDetector) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(floodDetector);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<FloodDetector> delete(@PathVariable final Integer id) throws IOException {
        if (!floodService.getMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        floodService.deleteFlood(id);
        return ResponseEntity.ok().build();
    }

}
