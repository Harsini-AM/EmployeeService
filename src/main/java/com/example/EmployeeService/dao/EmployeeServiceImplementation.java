package com.example.EmployeeService.dao;
import java.util.ArrayList;
import java.util.List;		
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.example.EmployeeService.service.PasswordService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;

import com.example.EmployeeService.entity.Representative;

import com.example.EmployeeService.dto.RepresentativeDto;


import com.example.EmployeeService.entity.Manager;
import com.example.EmployeeService.exception.DuplicateEntryException;
import com.example.EmployeeService.exception.EmployeeNotFoundException;
import com.example.EmployeeService.exception.ManagerNotFoundException;
import com.example.EmployeeService.feignService.CustomerServiceFeignClient;
import com.example.EmployeeService.repo.EmployeeServiceRepo;
import com.example.EmployeeService.repo.RepresentativeRepo;
import com.example.EmployeeService.dto.ManagerDto;

@Service
public class EmployeeServiceImplementation{
	

	@Autowired
	private EmployeeServiceRepo repo;
	
	@Autowired
	private RepresentativeRepo repRepo;
	
	@Autowired
	private CustomerServiceFeignClient customerServiceFeignClient;

	@Autowired
	private PasswordService passwordService;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	

	
	public ManagerDto getByManagerId(long id) {
        try {
            ManagerDto manager = repo.getManagerById(id);
            if (manager == null) {
                throw new ManagerNotFoundException("Manager not found with ID: " + id);
            }
            return manager;
        } catch (ManagerNotFoundException e) {
            throw e; // rethrow custom exceptions to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching the manager", e);
        }
    }
	
	
	public RepresentativeDto getByRepId(long id) {
        try {
            RepresentativeDto rep = repRepo.getRepresentativeById(id);
            if (rep == null) {
                throw new EmployeeNotFoundException("Representative not found with ID: " + id);
            }
            return rep;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching the representative", e);
        }
    }
	
	
	public Manager addManager(Manager manager) {
	    try {
	        String userPassword = passwordService.generatePassword();
	        String password = passwordEncoder.encode(userPassword);
	        String username = manager.getUserName();
	        manager.setPassword(password);
	        System.out.println("changed emp " + manager);
	        
	        if (repRepo.existsByUserName(manager.getUserName()) || repRepo.existsByPhoneNoCustom(manager.getPhone_no())) {
	            throw new DuplicateEntryException("Duplicate entry detected for manager: " + manager.getUserName() + " or phone number: " + manager.getPhone_no());
	        }

	        repo.save(manager);

	       
	        return manager;
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to add manager", e);
	    }
	}

	
	
	public ResponseEntity<?> addRepresentative(RepresentativeDto rep) {
	    try {
	        String userPassword = passwordService.generatePassword();
	        String password = passwordEncoder.encode(userPassword);
	        String username = rep.getUsername();
	        Optional<Manager> manager = repo.findByEmpId(rep.getManagerId());

	        

	        if (manager.isPresent()) {
	            rep.setPassword(password);
	            System.out.println("changed emp " + rep);
	            Representative newRep = new Representative();
	            newRep.setfName(rep.getfName());
	            newRep.setlName(rep.getlName());
	            newRep.setCity(rep.getCity());
	            newRep.setState(rep.getState());
	            newRep.setDomain(rep.getDomain());
	            newRep.setManager(manager.get());
	            newRep.setUsername(rep.getUsername());
	            newRep.setPassword(password);
	            newRep.setNumberOfTickets(rep.getNo_of_tickets());
	            newRep.setPhone_no(rep.getPhone_no());

	            repRepo.save(newRep);

	            return new ResponseEntity<>("Rep added", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Existing Manager required!", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to add representative", e);
	    }
	}

	

	
	public List<ManagerDto> getManagers() {
	  
	        return repo.getManagers();
	    
	}


	
	
	public ResponseEntity<?> updateManager(long id, ManagerDto managerDto) {
	    Manager update = null;
	    System.out.println("in service " + managerDto);
	    
	    try {
	        Optional<Manager> managerOptional = repo.findByEmpId(id); // Assuming `repo.findById` is the correct method to find a Manager by ID

	        if(managerOptional.isPresent()) {
	            update = managerOptional.get();
	            
	            // Update the manager's fields with the new values from managerDto
	            update.setfName(managerDto.getFirstName());
	            update.setlName(managerDto.getLastName());
	            update.setDomain(managerDto.getDomain());
	            update.setPhone_no(managerDto.getPhone_no());
	            update.setCity(managerDto.getCity());
	            update.setState(managerDto.getState());

	            repo.save(update);
	            
	            System.out.println("updated value " + update);
	            
	            return new ResponseEntity<>(update, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Manager not found", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to update manager: " + e.getMessage());
	    }
	}


	
	
	public ResponseEntity<?> updateRepresentative(long id, RepresentativeDto newData) {
	    Representative update = null;
	    
	    try {
	        Optional<Representative> repOptional = repRepo.findById(id); // Assuming `repRepo.findById` is the correct method to find a Representative by ID
	        
	        if (repOptional.isPresent()) {
	            update = repOptional.get();
	            
	            // Update the representative's fields with the new values from newData
	            update.setfName(newData.getfName());
	            update.setlName(newData.getlName());
	            update.setDomain(newData.getDomain());
	            update.setPhone_no(newData.getPhone_no());
	            update.setCity(newData.getCity());
	            update.setState(newData.getState());
	            update.setUserName(newData.getUserName()); 
	            update.setPassword(newData.getPassword());
	            update.setNumberOfTickets(newData.getNo_of_tickets());
	            Optional<Manager> newManager = repo.findByEmpId(newData.getManagerId());
	            if(newManager.isPresent()) update.setManager(newManager.get());
	            
	            
	            // Save the updated representative back to the repository
	            repRepo.save(update);
	            
	            System.out.println("updated value " + update);
	            
	            return new ResponseEntity<>(update, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Representative not found", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to update representative: " + e.getMessage());
	    }
	}
		
	public List<RepresentativeDto> getRepresentatives() {
	    
	        return repRepo.getRepresentatives();
	    
	}


		
	
	public String deleteManager(long id) {
	    try {
	        Optional<Manager> managerOptional = repo.findByEmpId(id);

	        if (managerOptional.isPresent()) {
	            Manager manager = managerOptional.get();
	            List<Representative> representatives = manager.getRepresentatives();

	            // Check if representatives list is null, initialize it if necessary
	            if (representatives == null) {
	                representatives = new ArrayList<>();
	                manager.setRepresentatives(representatives);
	            }

	            if (!representatives.isEmpty()) {
	                throw new Exception("Cannot delete manager with id " + id + " because they have associated representatives.");
	            }
	            repo.deleteById(id);
	            return "Manager removed successfully";
	        } else {
	            throw new ManagerNotFoundException("Manager with id " + id + " not found.");
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to delete manager", e);
	    }
	}

	public String deleteRepresentative(long id) {
	    try {
	        Optional<Representative> rep = repRepo.findByEmpId(id);

	        if (rep.isPresent()) {
	            repRepo.deleteById(id);
	            return "Representative removed successfully";
	        } else {
	            throw new EmployeeNotFoundException("Representative with id " + id + " not found.");
	        }
	    } catch (EmployeeNotFoundException e) {
	        throw e; // Re-throw custom exceptions to be handled by the global exception handler
	    } 
	}


	
		public ResponseEntity<?> promoteEmployee(long id, RepresentativeDto data) {
		    try {
		    	System.out.println("Promotion");
		        Optional<Representative> repOptional = repRepo.findByEmpId(id);
		        
		        if (repOptional.isPresent()) {
		            Representative rep = repOptional.get();

		            // Create a new manager using the data from the representative
		            Manager manager = new Manager(
		                rep.getEmpId(),
		                data.getfName(),
		                data.getlName(),
		                data.getPhone_no(),
		                data.getCity(),
		                data.getState(),
		                data.getUserName(),
		                data.getPassword(),
		                data.getDomain(),
		                false,
		                null 
		            );

		            // Save the new manager to the manager repository
		            repo.save(manager);
		            repRepo.delete(rep);

		            return new ResponseEntity<>("Representative promoted to Manager successfully", HttpStatus.OK);
		        } 
		    }
		    catch (Exception e) {
		        throw new RuntimeException("Failed to promote representative: " + e.getMessage());
		    }
			return null;
		}


		@Transactional
		public ResponseEntity<String> depromoteEmployee(long id, ManagerDto managerDto) {
		    
		        Optional<Manager> managerOpt = repo.findByEmpId(id);

		       

		        Manager manager = managerOpt.get();

		       
		        // Delete the manager
		        repo.deleteByEmpId(id);

		        // Create a new representative
		        Representative newRep = new Representative();
		        newRep.setfName(managerDto.getfName());
		        newRep.setlName(managerDto.getlName());
		        newRep.setCity(managerDto.getCity());
		        newRep.setState(managerDto.getState());
		        newRep.setDomain(managerDto.getDomain());
		        newRep.setUserName(managerDto.getUserName());
		        newRep.setPassword(managerDto.getPassword());
		        newRep.setPhone_no(managerDto.getPhone_no());
		        newRep.setPasswordChanged(false);

		        // Assign to a new manager if specified
		        Optional<Manager> newManagerOpt = repo.findByEmpId(managerDto.getManagerId());
		        
		        newRep.setManager(newManagerOpt.get());

		        repRepo.save(newRep);

		        return new ResponseEntity<>("Depromotion successful!", HttpStatus.OK);
		    
		}

		
		
	
	    
	   //chart data - data from customer
		public ResponseEntity<Map<Long, Double>> getResponseAverageByManagerId(Long managerId) {
	        
	            return customerServiceFeignClient.getResponseAverageByManagerId(managerId);
	        
	    }

	    public ResponseEntity<Map<Long, Double>> getResolutionAverageByManagerId(Long managerId) {
	       
	            return customerServiceFeignClient.getResolutionAverageByManagerId(managerId);
	  
	    }

	    public ResponseEntity<Map<String, Long>> getTicketCountsByStatus(Long managerId) {
	      
	            return customerServiceFeignClient.getTicketCountsByStatus(managerId);
	        
	    }

	    public ResponseEntity<Double> getAverageResponseTimeByRepId(Long repId) {
	       
	            ResponseEntity<Double> double1 =  customerServiceFeignClient.getAverageResponseTimeByRepId(repId);
	            System.out.println(double1.getBody());
	            return double1;
	          
			  
		}

	    public ResponseEntity<Double> getAverageResolutionTimeByRepId(Long repId) {
	       
	            return customerServiceFeignClient.getAverageResolutionTimeByRepId(repId);
	       
	    }

	    public ResponseEntity<Map<String, Long>> getTicketCountsByStatusForRep(Long repId) {
	        
	            return customerServiceFeignClient.getTicketCountsByStatusForRep(repId);
	        
	    }

	    

	    public Map<String, Float> getAverageResolutionTime(Long repId) {
	      
	            return customerServiceFeignClient.getAverageResolutionTime(repId);
	    
	    }

	    public List<Representative> getRepsByManagerId(Long managerId) {
	        try {
	            Optional<Manager> manager = repo.findByEmpId(managerId);

	            if (manager.isPresent()) {
	                return manager.get().getRepresentatives();
	            } 
	        }  catch (Exception e) {
	            throw new RuntimeException("Failed to get representatives by manager ID: " + managerId, e);
	        }
			return null;
	    }
	    
	  
}