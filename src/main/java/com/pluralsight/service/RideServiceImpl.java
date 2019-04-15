package com.pluralsight.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.RideRepository;

@Service("rideService")
public class RideServiceImpl implements RideService {

	@Autowired
	private RideRepository rideRepository;

	@Override
	public List<Ride> getRides() {
		return rideRepository.getRides();
	}

	@Override
	public Ride createRide(Ride ride) {
		return rideRepository.createRideSimple(ride);
	}

	@Override
	public Ride getRide(Integer id) {
		return rideRepository.getRide(id);
	}

	@Override
	public Ride updateRide(Ride ride) {
		return rideRepository.updateRide(ride);
	}

	@Override
	public Ride updateBatchRide(List<Ride> rides) {
		List<Object[]> updates = new ArrayList<>();

		for (Ride ride : rides) {
			Object[] temp = { ride.getDuration(), new Date(), ride.getId() };
			updates.add(temp);

		}
		rideRepository.updateBatchRide(updates);
		
		return null;
	}
	
	@Override
	public Object deleteRide(Integer id) {
		rideRepository.deleteRide(id);
		return null;
	}
}
