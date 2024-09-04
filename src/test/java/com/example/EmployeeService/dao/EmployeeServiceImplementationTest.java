package com.example.EmployeeService.dao;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.EmployeeService.dto.ManagerDto;
import com.example.EmployeeService.dto.RepresentativeDto;
import com.example.EmployeeService.entity.Manager;
import com.example.EmployeeService.entity.Representative;
import com.example.EmployeeService.exception.DuplicateEntryException;
import com.example.EmployeeService.exception.ManagerNotFoundException;
import com.example.EmployeeService.feignService.CustomerServiceFeignClient;
import com.example.EmployeeService.repo.EmployeeServiceRepo;
import com.example.EmployeeService.repo.RepresentativeRepo;
import com.example.EmployeeService.service.PasswordService;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplementationTest {

    @Mock
    private EmployeeServiceRepo repo;

    @Mock
    private RepresentativeRepo repRepo;

    @InjectMocks
    private EmployeeServiceImplementation empService;
    private final PasswordService passwordService = mock(PasswordService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    
    private final CustomerServiceFeignClient customerServiceFeignClient = mock(CustomerServiceFeignClient.class);
    
   

    @Test
    void testGetManagers() {
       
        List<ManagerDto> managerList = new ArrayList<>();
      
        when(repo.getManagers()).thenReturn(managerList);

        
        List<ManagerDto> result = empService.getManagers();

       
        assertEquals(managerList, result);
    }

    @Test
    void testGetRepresentatives() {
       
        List<RepresentativeDto> representativeList = new ArrayList<>();
      
        when(repRepo.getRepresentatives()).thenReturn(representativeList);

        List<RepresentativeDto> result = empService.getRepresentatives();

        assertEquals(representativeList, result);
    }
    
    
    @Test
    void testGetByRepId_RepFound() {
        
        RepresentativeDto representativeDto = new RepresentativeDto();
        when(repRepo.getRepresentativeById(anyLong())).thenReturn(representativeDto);

      
        RepresentativeDto result = empService.getByRepId(1L);

        
        assertEquals(representativeDto, result);
    }
    
    
    @Test
    void testGetByRepId_RepNotFound() {
        // Mock the behavior of repRepo.getRepresentativeById() to return an empty optional
        when(repRepo.getRepresentativeById(anyLong())).thenReturn(null);

        // Assert that the EmployeeNotFoundException is thrown
        assertThrows(RuntimeException.class, () -> {
            empService.getByRepId(1L);
        });
    }
    
    
    @Test
    void testGetByManagerId_ManagerFound() {
       
        ManagerDto managerDto = new ManagerDto();
        when(repo.getManagerById(anyLong())).thenReturn(managerDto);

      
        ManagerDto result = empService.getByManagerId(1L);

       
        assertEquals(managerDto, result);
    }

    @Test
    void testGetByManagerId_ManagerNotFound() {
        
        when(repo.getManagerById(anyLong())).thenReturn(null);

        
        assertThrows(ManagerNotFoundException.class, () -> {
            empService.getByManagerId(1L);
        });
    }
    
    
    @Test
    void testPromoteEmployee() {
        
        Optional<Representative> representative = Optional.of(new Representative());
        when(repRepo.findByEmpId(anyLong())).thenReturn(representative);

       
        RepresentativeDto representativeDto = new RepresentativeDto();
        ResponseEntity<?> response = empService.promoteEmployee(1L, representativeDto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    
    
    @Test
    void testUpdateManager() {
        
        Optional<Manager> manager = Optional.of(new Manager());
        when(repo.findByEmpId(anyLong())).thenReturn(manager);

        
        ManagerDto managerDto = new ManagerDto();
        managerDto.setFirstName("John");
        managerDto.setLastName("Doe");
        managerDto.setPhone_no(1234567890L);
        managerDto.setCity("New City");
        managerDto.setState("New State");
        ResponseEntity<?> response = empService.updateManager(1L, managerDto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateRepresentative() {
        
        Optional<Representative> representative = Optional.of(new Representative());
        when(repRepo.findById(anyLong())).thenReturn(representative);

        
        RepresentativeDto representativeDto = new RepresentativeDto();
        representativeDto.setfName("John");
        representativeDto.setlName("Doe");
        representativeDto.setPhone_no(1234567890L);
        representativeDto.setCity("New City");
        representativeDto.setState("New State");
        ResponseEntity<?> response = empService.updateRepresentative(1L, representativeDto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    

    @Test
    void testDeleteManager() {
        
        Optional<Manager> manager = Optional.of(new Manager());
        when(repo.findByEmpId(anyLong())).thenReturn(manager);

        
        String result = empService.deleteManager(10L);

       
        assertEquals("Manager removed successfully", result);
    }

    @Test
    void testDeleteRepresentative() {
       
        Optional<Representative> representative = Optional.of(new Representative());
        when(repRepo.findByEmpId(anyLong())).thenReturn(representative);

        String result = empService.deleteRepresentative(1L);

       
        assertEquals("Representative removed successfully", result);
    }

    
//    @Test
//    void testGetResponseAverageByManagerId() {
//       
//        Map<Long, Double> responseData = new HashMap<>();
//        responseData.put(1L, 10.0);
//        
//        when(customerServiceFeignClient.getResponseAverageByManagerId(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//       
//        ResponseEntity<Map<Long, Double>> response = empService.getResponseAverageByManagerId(1L);
//        
//  
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//    
//    @Test
//    void testGetResolutionAverageByManagerId() {
//        Map<Long, Double> responseData = new HashMap<>();
//        responseData.put(1L, 20.0);
//        
//        when(customerServiceFeignClient.getResolutionAverageByManagerId(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//        ResponseEntity<Map<Long, Double>> response = empService.getResolutionAverageByManagerId(1L);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//
//    @Test
//    void testGetTicketCountsByStatus() {
//        Map<String, Long> responseData = new HashMap<>();
//        responseData.put("OPEN", 5L);
//        
//        when(customerServiceFeignClient.getTicketCountsByStatus(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//        ResponseEntity<Map<String, Long>> response = empService.getTicketCountsByStatus(1L);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//
//    @Test
//    void testGetAverageResponseTimeByRepId() {
//        double responseData = 30.0;
//        
//        when(customerServiceFeignClient.getAverageResponseTimeByRepId(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//        ResponseEntity<Double> response = empService.getAverageResponseTimeByRepId(1L);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//
//    @Test
//    void testGetAverageResolutionTimeByRepId() {
//        double responseData = 15.0;
//        
//        when(customerServiceFeignClient.getAverageResolutionTimeByRepId(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//        ResponseEntity<Double> response = empService.getAverageResolutionTimeByRepId(1L);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//
//    @Test
//    void testGetTicketCountsByStatusForRep() {
//        Map<String, Long> responseData = new HashMap<>();
//        responseData.put("OPEN", 3L);
//        
//        when(customerServiceFeignClient.getTicketCountsByStatusForRep(1L)).thenReturn(ResponseEntity.ok(responseData));
//        
//        ResponseEntity<Map<String, Long>> response = empService.getTicketCountsByStatusForRep(1L);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseData, response.getBody());
//    }
//
//    @Test
//    void testGetAverageResolutionTime() {
//        Map<String, Float> responseData = new HashMap<>();
//        responseData.put("MONDAY", 10.0f);
//        
//        when(customerServiceFeignClient.getAverageResolutionTime(1L)).thenReturn(responseData);
//        
//        Map<String, Float> result = empService.getAverageResolutionTime(1L);
//        
//        assertEquals(responseData, result);
//    }
//
//    @Test
//    void testGetRepsByManagerId() {
//        
//        Manager manager = new Manager();
//        Representative rep1 = new Representative();
//        rep1.setEmpId(1L);
//        Representative rep2 = new Representative();
//        rep2.setEmpId(2L);
//        manager.setRepresentatives(List.of(rep1, rep2));
//
//     
//        when(repo.findByEmpId(1L)).thenReturn(Optional.of(manager));
//        
//      
//        List<Representative> reps = empService.getRepsByManagerId(1L);
//        
//      
//        assertNotNull(reps);
//        assertEquals(2, reps.size());
//        assertEquals(1L, reps.get(0).getEmpId());
//        assertEquals(2L, reps.get(1).getEmpId());
//    }
    
    @Test
    void testAddManager() {
        // Set up test data
        Manager manager = new Manager();
        manager.setUserName("testUser");
        manager.setPhone_no(1234567890L);

        // Mock password generation and encoding
        when(passwordService.generatePassword()).thenReturn("password");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Mock repository behavior
        when(repRepo.existsByUserName(anyString())).thenReturn(false);
        when(repRepo.existsByPhoneNoCustom(anyLong())).thenReturn(false);

        // Call the service method
        Manager result = empService.addManager(manager);

        
        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(repo, times(1)).save(manager);
    }
 
    

  
    @Test
    void testAddRepresentativeSuccess() {
        // Arrange
        RepresentativeDto repDto = new RepresentativeDto();
        repDto.setfName("John");
        repDto.setlName("Doe");
        repDto.setCity("New York");
        repDto.setState("NY");
        repDto.setDomain("domain");
        repDto.setManagerId(1L);
        repDto.setUsername("johndoe");
        repDto.setNo_of_tickets(10);
        repDto.setPhone_no(1234567890L);

        Manager manager = new Manager();
        manager.setEmpId(1L);

        when(repo.findByEmpId(1L)).thenReturn(Optional.of(manager));
        when(passwordService.generatePassword()).thenReturn("generatedPassword");
        when(passwordEncoder.encode("generatedPassword")).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = empService.addRepresentative(repDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rep added", response.getBody());
    }
    
    @Test
    void testAddRepresentativeManagerNotFound() {
        // Arrange
        RepresentativeDto repDto = new RepresentativeDto();
        repDto.setfName("John");
        repDto.setlName("Doe");
        repDto.setCity("New York");
        repDto.setState("NY");
        repDto.setDomain("domain");
        repDto.setManagerId(1L);
        repDto.setUsername("johndoe");
        repDto.setNo_of_tickets(10);
        repDto.setPhone_no(1234567890L);

        when(repo.findByEmpId(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = empService.addRepresentative(repDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Existing Manager required!", response.getBody());
    }

    
    @Test
    void testAddRepresentativeException() {
        // Arrange
        RepresentativeDto repDto = new RepresentativeDto();
        repDto.setfName("John");
        repDto.setlName("Doe");
        repDto.setCity("New York");
        repDto.setState("NY");
        repDto.setDomain("domain");
        repDto.setManagerId(1L);
        repDto.setUsername("johndoe");
        repDto.setNo_of_tickets(10);
        repDto.setPhone_no(1234567890L);

        when(repo.findByEmpId(1L)).thenThrow(new RuntimeException("Error"));

        // Act and Assert
        try {
            empService.addRepresentative(repDto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Failed to add representative", e.getMessage());
        }
    }
    

  
}

