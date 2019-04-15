package com.pluralsight.repository;

import java.util.List;

import com.pluralsight.model.Ride;

public interface RideRepository {
	
	Ride createRideSimple(Ride ride);

	List<Ride> getRides();

	Ride createRideReturningKey(Ride ride);

	Ride createRideKeyHolder(Ride ride);

}