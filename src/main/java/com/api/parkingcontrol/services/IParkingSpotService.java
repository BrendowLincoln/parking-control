package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IParkingSpotService {
    ParkingSpotModel save(ParkingSpotDto parkingSpotDto);
    List<ParkingSpotModel> getAll();
    ParkingSpotModel getById(UUID id);
    void delete(UUID id);
    ParkingSpotModel update(ParkingSpotDto parkingSpotDto);
}
