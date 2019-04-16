package com.pluralsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pluralsight.model.Ride;
import com.pluralsight.service.RideService;
import com.pluralsight.util.ServiceError;

@Controller
public class RideController {

	@Autowired
	private RideService rideService;

	@RequestMapping(value = "/ride", method = RequestMethod.POST)
	public @ResponseBody Ride createRide(@RequestBody Ride ride) {
		return rideService.createRide(ride);
	}

	@RequestMapping(value = "/rides", method = RequestMethod.GET)
	public @ResponseBody List<Ride> getRides() {
		return rideService.getRides();
	}

	@GetMapping("/ride/{id}")
	public @ResponseBody Ride getRide(@PathVariable(value = "id") Integer id) {
		return rideService.getRide(id);
	}

	@PutMapping(value = "/ride")
	public @ResponseBody Ride updateRide(@RequestBody Ride ride) {
		return rideService.updateRide(ride);
	}

	@PutMapping(value = "/batch")
	public @ResponseBody Ride updateBatchRide(@RequestBody List<Ride> rides) {
		return rideService.updateBatchRide(rides);
	}

	@DeleteMapping(value = "/deleteRide/{id}")
	public Object deleteRide(@PathVariable(value = "id") Integer id) {
		rideService.deleteRide(id);
		return null;
	}

	@GetMapping("/testException")
	public @ResponseBody Object testException() {
//		throw new DataAccessException("testing exceptions") {
//		};
		throw new NullPointerException();
	}

	/**
	 * con la auyuda de la clase de serviceError se puede crear un manejador de exceciones, sin importar donde estallen 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ServiceError> handle(RuntimeException ex) {
		ServiceError se = new ServiceError(HttpStatus.OK.value(), ex.getMessage());
		System.err.println("Ocurrio un errorsito. -->> "+ ex.getMessage());
		System.err.println("Localized--->>>>"+ex.getLocalizedMessage());
		System.err.println("Cause2--->>>>"+ex.getCause());
		System.err.println("Class--->>>>"+ex.getClass());
		System.err.println("ex--->>>>"+ex);
		System.err.println("suppressed--->>>>"+ex.getSuppressed().toString());
		System.err.println("stack--->>>>"+ex.getStackTrace()[0]);
		return new ResponseEntity<>(se, HttpStatus.OK);
	}

}
