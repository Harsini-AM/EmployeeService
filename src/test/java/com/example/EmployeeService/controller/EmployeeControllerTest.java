

package com.example.EmployeeService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.EmployeeService.EmployeeApplication;
import com.example.EmployeeService.dao.EmployeeServiceImplementation;
import com.example.EmployeeService.dto.ManagerDto;
import com.example.EmployeeService.dto.RepresentativeDto;
import com.example.EmployeeService.entity.Manager;
import com.example.EmployeeService.entity.Representative;
import com.example.EmployeeService.exception.DuplicateEntryException;
import com.example.EmployeeService.repo.EmployeeServiceRepo;
import com.example.EmployeeService.repo.RepresentativeRepo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmployeeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private EmployeeServiceImplementation empService;

    @Mock
    private RepresentativeRepo repo;

    @Mock
    private EmployeeServiceRepo managerRepo;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddManager() {
        String uniqueUserName = "user" + UUID.randomUUID().toString() + "@LIT.com";
        long mobileNumber = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
        Manager man = new Manager(1, "Raji", uniqueUserName, mobileNumber, "Chennai", "TN", uniqueUserName, "153819r66", "network", false, null);
        
        when(empService.addManager(man)).thenReturn(man);
        
        ResponseEntity<?> response = employeeController.addEmployee(man);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //verify(empService, times(1)).addManager(man);
    }

    @Test
    public void testAddManagerIfAlreadyPresent() {
        Manager manager = new Manager();
        manager.setUserName("User1@LIT.com");
        manager.setPassword("password");
        manager.setfName("FirstName");
        manager.setlName("LastName");
        manager.setCity("City");
        manager.setState("State");
        manager.setPhone_no(1234567890L);
        manager.setDomain("network");
        
        when(empService.addManager(any(Manager.class))).thenThrow(new DuplicateEntryException("Manager already exists"));

        ResponseEntity<?> response = employeeController.addEmployee(manager);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
    }

    @Test
    public void testAddRepresentative() {
        RepresentativeDto rep = new RepresentativeDto();
        String uniqueUserName = "user" + UUID.randomUUID().toString() + "@LIT.com";
        long randomNumber = 100000 + new Random().nextInt(900000);
        
        rep.setUserName(uniqueUserName);
        rep.setPassword("password");
        rep.setfName("FirstName");
        rep.setlName("LastName");
        rep.setCity("City");
        rep.setState("State");
        rep.setPhone_no(randomNumber);
        rep.setDomain("network");
        rep.setManagerId(117L);

        when(empService.addRepresentative(rep)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        
        ResponseEntity<?> response = employeeController.addRepresentative(rep);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(empService, times(1)).addRepresentative(rep);
    }

    @Test
    public void testAddRepresentativeIfUserNameExists() {
        RepresentativeDto rep = new RepresentativeDto();
        long randomNumber = 100000 + new Random().nextInt(900000);
        
        rep.setUserName("User6@LIT.com");
        rep.setPassword("password");
        rep.setfName("FirstName");
        rep.setlName("LastName");
        rep.setCity("City");
        rep.setState("State");
        rep.setPhone_no(randomNumber);
        rep.setDomain("network");
        rep.setManagerId(117L);

        when(empService.addRepresentative(rep)).thenReturn(new ResponseEntity<>(HttpStatus.CONFLICT));
        
        ResponseEntity<?> response = employeeController.addRepresentative(rep);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(empService, times(1)).addRepresentative(rep);
    }

    @Test
    public void testGetAllManagers() {
        List<ManagerDto> managers = new ArrayList<>();
        when(empService.getManagers()).thenReturn(managers);
        
        ResponseEntity<List<ManagerDto>> response = employeeController.getAllManagers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(empService, times(1)).getManagers();
    }

    @Test
    public void testGetAllRepresentatives() {
        List<RepresentativeDto> representatives = new ArrayList<>();
        when(empService.getRepresentatives()).thenReturn(representatives);
        
        ResponseEntity<List<RepresentativeDto>> response = (ResponseEntity<List<RepresentativeDto>>) employeeController.getAllRepresentatives();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(empService, times(1)).getRepresentatives();
    }

    @Test
    void testGetManagerById() {
        ManagerDto manager = new ManagerDto();
        when(empService.getByManagerId(117)).thenReturn(manager);
        
        ResponseEntity<ManagerDto> response = employeeController.getById(117);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(empService, times(1)).getByManagerId(117);
    }

//    @Test
//    void testGetManagerByIdIfNotPresent() {
//    	when(empService.getByRepId(1)).thenThrow(new RuntimeException("Representative not found"));
//
//        ResponseEntity<?> response = employeeController.getById(1);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }

    @Test
    void testGetRepresentativeById() {
        RepresentativeDto representative = new RepresentativeDto();
        when(empService.getByRepId(43)).thenReturn(representative);
        
        ResponseEntity<RepresentativeDto> response = employeeController.getRepById(43);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(empService, times(1)).getByRepId(43);
    }

    @Test
    void testGetRepresentativeByIdIfNotPresent() {
        when(empService.getByRepId(19)).thenThrow(new RuntimeException("Representative not found"));

        ResponseEntity<?> response = employeeController.getRepById(19);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateManager() {
        ManagerDto updateRequest = new ManagerDto(117, "harsini@LIT.com","Harsini","A M","Madurai","TN",0, "1234567893","harsini123");
        updateRequest.setfName("test@test.com");

        when(empService.updateManager(77, updateRequest)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        
        ResponseEntity<?> response = employeeController.updateManager(77, updateRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(empService, times(1)).updateManager(77, updateRequest);
    }

    @Test
    void updateRepresentative() {
        RepresentativeDto updateRequest = new RepresentativeDto();
        updateRequest.setfName("test@test.com");

        when(empService.updateRepresentative(36, updateRequest)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        
        ResponseEntity<?> response = employeeController.updateRepresentative(36, updateRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(empService, times(1)).updateRepresentative(36, updateRequest);
    }

    @Test
    void getListOfRepresentativeByManagerId() {
        List<Representative> representatives = new ArrayList<>();
        when(empService.getRepsByManagerId(117L)).thenReturn(representatives);
        
        ResponseEntity<List<Representative>> response = employeeController.getRepresentative(117L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(empService, times(1)).getRepsByManagerId(117L);
    }

    @Test
    void testGetResolutionAverageByManagerId() {
        Map<Long, Double> result = new HashMap<>();
        result.put(14L, 90.0);

        when(empService.getResolutionAverageByManagerId(26L)).thenReturn(new ResponseEntity<>(result, HttpStatus.OK));
        
        ResponseEntity<?> response = employeeController.getResolutionAverageByManagerId(26L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result, response.getBody());
        verify(empService, times(1)).getResolutionAverageByManagerId(26L);
    }

    @Test
    void testGetTicketCountsByStatus() {
        Map<String, Long> result = new HashMap<>();
        result.put("OPEN", 10L);
        result.put("CLOSED", 5L);

        when(empService.getTicketCountsByStatus(26L)).thenReturn(new ResponseEntity<>(result, HttpStatus.OK));
        
        ResponseEntity<?> response = employeeController.getTicketCountsByStatus(26L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result, response.getBody());
        verify(empService, times(1)).getTicketCountsByStatus(26L);
    }
    
    @Test
    void testDeleteManager() {
    	   Optional<Manager> manager = Optional.of(new Manager());
           when(empService.deleteManager(anyLong())).thenReturn("Manager removed successfully");

           
           String result = empService.deleteManager(10L);

          
           assertEquals("Manager removed successfully", result);
    }
    
    @Test
    public void testGetAverageResponseTimeByRepId() {
        // Given
        Long repId = 1L;
        Double expectedAverageResponseTime = 123.45;
        when(empService.getAverageResponseTimeByRepId(repId)).thenReturn(ResponseEntity.ok(expectedAverageResponseTime));

        // When
        ResponseEntity<Double> response = employeeController.getAverageResponseTimeByRepId(repId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAverageResponseTime, response.getBody());
    }
    
    
    @Test
    public void testGetAverageResolutionTimeByRepId() {
        // Given
        Long repId = 1L;
        Double expectedAverageResolutionTime = 234.56;
        when(empService.getAverageResolutionTimeByRepId(repId)).thenReturn(ResponseEntity.ok(expectedAverageResolutionTime));

        // When
        ResponseEntity<Double> response = employeeController.getAverageResolutionTimeByRepId(repId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAverageResolutionTime, response.getBody());
    }
    
    @Test
    public void testGetTicketCountsByStatusForRep() {
        // Given
        Long repId = 1L;
        Map<String, Long> expectedTicketCounts = new HashMap<>();
        expectedTicketCounts.put("open", 10L);
        expectedTicketCounts.put("closed", 20L);
        when(empService.getTicketCountsByStatusForRep(repId)).thenReturn(ResponseEntity.ok(expectedTicketCounts));

        // When
        ResponseEntity<Map<String, Long>> response = employeeController.getTicketCountsByStatusForRep(repId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTicketCounts, response.getBody());
    }
    
    @Test
    public void testGetAverageResolutionTime() {
        // Given
        Long repId = 1L;
        Map<String, Float> expectedAverageResolutionTime = new HashMap<>();
        expectedAverageResolutionTime.put("Monday", 345.67f);
        expectedAverageResolutionTime.put("Tuesday", 456.78f);
        when(empService.getAverageResolutionTime(repId)).thenReturn(expectedAverageResolutionTime);

        // When
        Map<String, Float> response = employeeController.getAverageResolutionTime(repId);

        // Then
        assertEquals(expectedAverageResolutionTime, response);
    }
    
    @Test
    public void testDepromoteEmployeeSuccess() {
        long id = 1L;
        ManagerDto newData = new ManagerDto();
        newData.setfName("John");
        newData.setlName("Doe");
        newData.setCity("New York");
        newData.setState("NY");
        newData.setDomain("IT");
        newData.setUserName("johndoe");
        newData.setPassword("password");
        newData.setPhone_no(1234567890L);
        newData.setManagerId(100L); // New manager's ID

        when(empService.depromoteEmployee(id, newData)).thenReturn(new ResponseEntity<>("Depromotion successful!", HttpStatus.OK));

        ResponseEntity<?> response = employeeController.depromoteEmployee(id, newData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Depromotion successful!", response.getBody());
    }
    

}

