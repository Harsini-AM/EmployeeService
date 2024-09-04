package com.example.EmployeeService.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ManagerTests {

    @Test
    void testGetterAndSetter() {
        // Create a manager instance
        Manager manager = new Manager();

        // Set properties using setter methods
        long empId = 1L;
        String fName = "John";
        String lName = "Doe";
        long phoneNo = 1234567890L;
        String city = "Example City";
        String state = "Example State";
        String userName = "exampleUser";
        String password = "examplePassword";
        String domain = "exampleDomain";
        boolean passwordChanged = true;

        manager.setEmpId(empId);
        manager.setfName(fName);
        manager.setlName(lName);
        manager.setPhone_no(phoneNo);
        manager.setCity(city);
        manager.setState(state);
        manager.setUserName(userName);
        manager.setPassword(password);
        manager.setDomain(domain);
        manager.setPasswordChanged(passwordChanged);

        // Verify getter methods
        assertEquals(empId, manager.getEmpId());
        assertEquals(fName, manager.getfName());
        assertEquals(lName, manager.getlName());
        assertEquals(phoneNo, manager.getPhone_no());
        assertEquals(city, manager.getCity());
        assertEquals(state, manager.getState());
        assertEquals(userName, manager.getUserName());
        assertEquals(password, manager.getPassword());
        assertEquals(domain, manager.getDomain());
        assertEquals(passwordChanged, manager.getPasswordChanged());
    }

    @Test
    void testToString() {
        // Create a manager instance
        Manager manager = new Manager();
        manager.setEmpId(1L);
        manager.setfName("John");
        manager.setlName("Doe");
        manager.setPhone_no(1234567890L);
        manager.setCity("Example City");
        manager.setState("Example State");
        manager.setUserName("exampleUser");
        manager.setPassword("examplePassword");
        manager.setDomain("exampleDomain");
        manager.setPasswordChanged(true);

        // Verify toString() method
        String expectedToString = "Manager [EmpId=1, fName=John, lName=Doe, phone_no=1234567890, city=Example City, state=Example State, userName=exampleUser, password=examplePassword, domain=exampleDomain, PasswordChanged=true]";
        assertEquals(expectedToString, manager.toString());
    }

    @Test
    void testNoArgsConstructor() {
        // Test no-args constructor
        Manager manager = new Manager();
        assertNotNull(manager);
    }

    @Test
    void testAllArgsConstructor() {
        // Test all-args constructor
        long empId = 1L;
        String fName = "John";
        String lName = "Doe";
        long phoneNo = 1234567890L;
        String city = "Example City";
        String state = "Example State";
        String userName = "exampleUser";
        String password = "examplePassword";
        String domain = "exampleDomain";
        boolean passwordChanged = true;

        Manager manager = new Manager(empId, fName, lName, phoneNo, city, state, userName, password, domain, passwordChanged, new ArrayList<>());

        assertEquals(empId, manager.getEmpId());
        assertEquals(fName, manager.getfName());
        assertEquals(lName, manager.getlName());
        assertEquals(phoneNo, manager.getPhone_no());
        assertEquals(city, manager.getCity());
        assertEquals(state, manager.getState());
        assertEquals(userName, manager.getUserName());
        assertEquals(password, manager.getPassword());
        assertEquals(domain, manager.getDomain());
        assertEquals(passwordChanged, manager.getPasswordChanged());
    }

    @Test
    void testRepresentatives() {
        // Create a manager instance
        Manager manager = new Manager();

        // Create a list of representatives
        List<Representative> representatives = new ArrayList<>();
        Representative representative1 = new Representative();
        representatives.add(representative1);
        Representative representative2 = new Representative();
        representatives.add(representative2);

        // Set representatives using setter method
        manager.setRepresentatives(representatives);

        // Verify getter method for representatives
        assertEquals(representatives, manager.getRepresentatives());
    }
}

