package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.IParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ParkingSpot")
public class ParkingSpotController {
    private final IParkingSpotService _parkingSpotService;

    public ParkingSpotController(IParkingSpotService parkingSpotService) {
        this._parkingSpotService = parkingSpotService;
    }

    //region Queries
    @GetMapping("/GetAll")
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots() {
        var results = _parkingSpotService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") UUID id) {
        try {
            var result = _parkingSpotService.getById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(result);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
    //endregion

    //region Commands
    @PostMapping("/SaveParkingSpot")
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto dto) {
       try {
           var result = this._parkingSpotService.save(dto);
           return ResponseEntity.status(HttpStatus.CREATED).body(result);
       } catch (Exception exception) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
       }
    }

    @PutMapping("/UpdateParkingSpot")
    public ResponseEntity<Object> updateParkingSpot(@RequestBody ParkingSpotDto updateParkingSpotDto) {
        try {
            var result = _parkingSpotService.update(updateParkingSpotDto);
                return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        try {
            _parkingSpotService.delete(id);
            return ResponseEntity.ok("Parking Spot deleted successfully.");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
    //endregion
}
