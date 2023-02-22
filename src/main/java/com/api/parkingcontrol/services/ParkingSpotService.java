package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.IParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService implements IParkingSpotService {

    final IParkingSpotRepository _parkingSpotRepository;

    public ParkingSpotService(IParkingSpotRepository parkingSpotRepository) {
        _parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotDto parkingSpotDto) {

        if(parkingSpotDto == null) {
            throw new IllegalArgumentException("The object is null.");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        validate(parkingSpotModel);

        return _parkingSpotRepository.save(parkingSpotModel);
    }

    @Transactional
    public void delete(UUID id) {
        var parkingSpot = _parkingSpotRepository.findById(id);

        if(!parkingSpot.isPresent()) {
            throw new IllegalArgumentException("There isn't a parking sport for this id!");
        }
        _parkingSpotRepository.delete(parkingSpot.get());
    }

    public ParkingSpotModel update(ParkingSpotDto updateParkingSpot) {

        var parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(updateParkingSpot, parkingSpotModel);
        parkingSpotModel.setId(updateParkingSpot.getId());
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return _parkingSpotRepository.save(parkingSpotModel);
    }

    public List<ParkingSpotModel> getAll() {
        return _parkingSpotRepository.findAll();
    }

    public ParkingSpotModel getById(UUID id) {
        var parkingSpot = _parkingSpotRepository.findById(id);

        if(!parkingSpot.isPresent()) {
            throw new IllegalArgumentException("There isn't a parking sport for this id!");
        }

        return  parkingSpot.get();
    }

    //region Private Methods
    public void validate(ParkingSpotModel parkingSpotModel) {
        if(_parkingSpotRepository.existsByLicensePlateCar(parkingSpotModel.getLicensePlateCar())) {
            throw new IllegalArgumentException("Conflict: License Plate Car is already in use!");
        }

        if(_parkingSpotRepository.existsByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber())) {
            throw new IllegalArgumentException("Conflict: Parking Spot is already in use!");
        }

        if(_parkingSpotRepository.existsByApartmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock())) {
            throw new IllegalArgumentException("Conflict: Parking Spot is already registered for this apartment!");
        }
   }
    //endregion
}
