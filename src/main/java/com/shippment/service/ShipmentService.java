package com.shippment.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shippment.model.ShipmentStatus;

@Service
public class ShipmentService {
	
	/**
	 * Get shipment cost, package type based on status
	 * @param respList
	 * @return
	 */
	public List<ShipmentStatus> getShipmentStatus(List<Map<String, String>> respList ) {
		
		List<ShipmentStatus> shipList = new ArrayList<>();
		Map<String, ShipmentStatus> resultMap = new HashMap<String, ShipmentStatus>();
		DecimalFormat df = new DecimalFormat("$###.##");
		for(Map<String, String> result : respList) {
			
			String deliveryStatus = result.get("delivery_status");
			Double cost = Double.parseDouble(String.valueOf(result.get("cost")));
			String packageType = result.get("package_type");
			String id = result.get("id");
			String emailAddress = getAddress(id);
			if(resultMap.containsKey(deliveryStatus)) {
				ShipmentStatus shipment = resultMap.get(deliveryStatus);
				shipment.setValue(shipment.getValue() + cost);
				shipment.getPackageType().add(packageType);
				shipment.setCost(df.format(shipment.getValue()));
				shipment.getEmailAddress().add(emailAddress);
			}else {
				Set<String> al = new HashSet<>();
				Set<String> address = new HashSet<>();
				al.add(packageType);
				address.add(emailAddress);
				ShipmentStatus shipment = new ShipmentStatus(deliveryStatus, cost, al, address);
				shipment.setCost(df.format(shipment.getValue()));
				resultMap.put(deliveryStatus, shipment);
			}
		}
		shipList = resultMap.values().stream().collect(Collectors.toList());
		return shipList;
		
	}

	/**
	 * Get Email address based on id
	 * @param id
	 * @return
	 */
	private String getAddress(String id) {
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity requestEntity = new HttpEntity(getHeaders());
		String url = "https://smb-shippers.vercel.app/api/shipments/"+id;
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String email = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			JsonNode data = mapper.readTree(response.getBody());
			email = data.path("email").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return email;
	}

	/**
	 * Get shipment data from the api
	 * @return
	 */
	public ResponseEntity<String> getShipmentData() {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> requestEntity = new HttpEntity(getHeaders());
		String url = "https://smb-shippers.vercel.app/api/shipments";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response;
	}
	
	/**
	 * Get header info
	 * @return
	 */
	private HttpHeaders getHeaders() {

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("x-api-key", "X7pIfSfrmkQzyugj06FI");

		return headers;
	}

}
