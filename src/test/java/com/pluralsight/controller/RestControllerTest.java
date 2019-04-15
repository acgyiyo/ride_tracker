package com.pluralsight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.pluralsight.model.Ride;

import org.junit.Test;

public class RestControllerTest {

	/**
	 * prueba de insercción de código de un ride
	 */
	@Test(timeout = 5000)
	public void testCreateRide() {
		RestTemplate rt = new RestTemplate();

		Ride ride = new Ride();
		ride.setName("Great Excursion Ride");
		ride.setDuration(225);

		rt.postForObject("http://localhost:8080/ride_tracker/ride", ride, Ride.class);
	}

	@Test(timeout = 3000)
	public void testGetRides() {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<Ride>> ridesResponse = restTemplate.exchange("http://localhost:8080/ride_tracker/rides",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Ride>>() {
				});
		List<Ride> rides = ridesResponse.getBody();

		for (Ride ride : rides) {
			System.out.println("Ride name: " + ride.getName() + ", " + ride.getDuration());
		}
	}

	@Test(timeout = 5000)
	public void testGetRide() {
		RestTemplate resTemplate = new RestTemplate();

		Ride ride = resTemplate.getForObject("http://localhost:8080/ride_tracker/ride/1", Ride.class);
		System.out.print("Ride name: " + ride.getName());
	}

	@Test(timeout = 5000)
	public void testUpdateRide() {
		RestTemplate resTemplate = new RestTemplate();
		Ride ride = resTemplate.getForObject("http://localhost:8080/ride_tracker/ride/1", Ride.class);

		ride.setDuration(ride.getDuration() + 100);
		resTemplate.put("http://localhost:8080/ride_tracker/ride", ride);
		System.out.println("RIde name: " + ride.getName() + ", " + ride.getDuration());
	}

	@Test(timeout = 5000)
	public void testBatchUpdateRide() {
		RestTemplate resTemplate = new RestTemplate();
		List<Ride> rides = new ArrayList<>();

		Ride ride = new Ride();
		ride.setId(2);
		ride.setDuration(100);
		rides.add(ride);

		ride = new Ride();
		ride.setId(3);
		ride.setDuration(200);
		rides.add(ride);

		resTemplate.put("http://localhost:8080/ride_tracker/batch", rides);
	}

	@Test(timeout = 5000)
	public void testDelete() {
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.delete("http://localhost:8080/ride_tracker/deleteRide/4");
	}
}
