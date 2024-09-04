package com.example.EmployeeService.controller;


import java.util.List;	
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.example.EmployeeService.dao.EmployeeServiceImplementation;
import com.example.EmployeeService.dto.ManagerDto;
import com.example.EmployeeService.dto.RepresentativeDto;
import com.example.EmployeeService.entity.Manager;
import com.example.EmployeeService.entity.Representative;
import com.example.EmployeeService.feignService.CustomerServiceFeignClient;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	
	@Autowired
	public EmployeeServiceImplementation empService;
	
	@Autowired
	private CustomerServiceFeignClient customerServiceFeignClient;
	
	@Autowired
	private RestTemplate restTemp;

	

	
	@PostMapping("/addManager")
	public ResponseEntity<Manager> addEmployee(@RequestBody Manager rep){
		 try {
	           
	            return new ResponseEntity<>(empService.addManager(rep), HttpStatus.OK);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(HttpStatus.CONFLICT);
	        }
		
		
	}
	
	@PostMapping("/addRepresentative")
	public ResponseEntity<?> addRepresentative(@RequestBody RepresentativeDto rep){
		return empService.addRepresentative(rep);
	}
	
	@GetMapping("/getManagers")
	public ResponseEntity<List<ManagerDto>> getAllManagers(){
		List<ManagerDto> managers = empService.getManagers();
		return new ResponseEntity<>(managers, HttpStatus.OK);
	}
	
	@GetMapping("/getRepresentatives")
	public ResponseEntity<?> getAllRepresentatives(){
		List<RepresentativeDto> reps = empService.getRepresentatives();
		return new ResponseEntity<>(reps, HttpStatus.OK);
	}
	
	
	@GetMapping("/getManager/{id}")
	public ResponseEntity<ManagerDto> getById(@PathVariable int id){
		  try {
	            ManagerDto manager = empService.getByManagerId(id);
	            return new ResponseEntity<>(manager, HttpStatus.OK);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	}
	
	@GetMapping("/getRepresentative/{id}")
	public ResponseEntity<RepresentativeDto> getRepById(@PathVariable int id){
		 try {
	            RepresentativeDto representative = empService.getByRepId(id);
	            return new ResponseEntity<>(representative, HttpStatus.OK);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	}
	
	@DeleteMapping("/deleteManager/{id}")
	public ResponseEntity<?> deleteManager(@PathVariable long id){
		String response = empService.deleteManager(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/deleteRepresentative/{id}")
	public ResponseEntity<?> deleteRepresentative(@PathVariable long id){
		String response = empService.deleteRepresentative(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@PutMapping("updateManager/{id}")
    public ResponseEntity<?> updateManager(@PathVariable long id, @RequestBody ManagerDto newData) {
		System.out.println(newData);
		return empService.updateManager(id, newData);
		
    }
	
	
	@PutMapping("updateRepresentative/{id}")
    public ResponseEntity<?> updateRepresentative(@PathVariable long id, @RequestBody RepresentativeDto newData) {
		System.out.println(newData);
		return empService.updateRepresentative(id, newData);
		
    }
	
	@PutMapping("promoteEmployee/{id}")
	public ResponseEntity<?> promoteEmployee(@PathVariable long id, @RequestBody RepresentativeDto newData) {
		System.out.println(newData);
		return empService.promoteEmployee(id, newData);
		
    }
	
	@PutMapping("depromoteEmployee/{id}")
	public ResponseEntity<String> depromoteEmployee(@PathVariable long id, @RequestBody ManagerDto newData) {
		
		return empService.depromoteEmployee(id, newData);
		
    }
		
	
//charts
	
    
    @GetMapping("/resolutionAverage/{managerId}")
    public ResponseEntity<Map<Long, Double>> getResolutionAverageByManagerId(@PathVariable Long managerId) {
        return empService.getResolutionAverageByManagerId(managerId);
    }

    @GetMapping("/statusCounts/{managerId}")
    public ResponseEntity<Map<String, Long>> getTicketCountsByStatus(@PathVariable Long managerId) {
        return empService.getTicketCountsByStatus(managerId);
    }

    @GetMapping("/averageResponseTime/{repId}")
    public ResponseEntity<Double> getAverageResponseTimeByRepId(@PathVariable Long repId) {
        return empService.getAverageResponseTimeByRepId(repId);
    }

    @GetMapping("/averageResolutionTime/{repId}")
    public ResponseEntity<Double> getAverageResolutionTimeByRepId(@PathVariable Long repId) {
        return empService.getAverageResolutionTimeByRepId(repId);
    }

    @GetMapping("/statusCountsForRep/{repId}")
    public ResponseEntity<Map<String, Long>> getTicketCountsByStatusForRep(@PathVariable Long repId) {
        return empService.getTicketCountsByStatusForRep(repId);
    }

    @GetMapping("/weeklyResponseTime/{repId}")
    public Map<String, Float> getAverageResponseTime(@PathVariable Long repId) {
        return (Map<String, Float>) empService.getAverageResponseTimeByRepId(repId);
    }

    @GetMapping("/weeklyResolutionTime/{repId}")
    public Map<String, Float> getAverageResolutionTime(@PathVariable Long repId) {
        return empService.getAverageResolutionTime(repId);
    }
    
    
    @GetMapping("/getRepByManagerId/{managerId}")
    public ResponseEntity<List<Representative>> getRepresentative(@PathVariable Long managerId){
    	return new ResponseEntity<>(empService.getRepsByManagerId(managerId), HttpStatus.OK);
    }
    
    
    
}
