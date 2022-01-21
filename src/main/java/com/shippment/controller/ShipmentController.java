package com.shippment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shippment.model.ShipmentStatus;
import com.shippment.service.ShipmentService;

@RestController
public class ShipmentController {
	
	@Autowired
	ShipmentService shipmentService;
	
	@GetMapping("/shipment-report")
	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getShippmentStatusNew() {

		try {

			ResponseEntity<String> responseData = shipmentService.getShipmentData(); //getShipment data from api
			ObjectMapper mapper = new ObjectMapper();
			
			List<Map<String, String>> respList = mapper.readValue(responseData.getBody(), List.class); //read data from the response
			List<ShipmentStatus> shipList = shipmentService.getShipmentStatus(respList);
			return new ResponseEntity<>(shipList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Problem fetching data, Please try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
