package com.example.EmployeeService.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RepresentativeTests {

    @Test
    void testGetterAndSetter() {
        // Create a representative instance
        Representative representative = new Representative();

        // Set properties using setter methods
        long empId = 1L;
        String fName = "John";
        String lName = "Doe";
        long phoneNo = 1234567890L;
        String city = "Example City";
        String state = "Example State";
        String userName = "exampleUser";
        String password = "examplePassword";
        String domain = "Example Domain";
        long numberOfTickets = 5L;
        boolean passwordChanged = true;

        representative.setEmpId(empId);
        representative.setfName(fName);
        representative.setlName(lName);
        representative.setPhone_no(phoneNo);
        representative.setCity(city);
        representative.setState(state);
        representative.setUserName(userName);
        representative.setPassword(password);
        representative.setDomain(domain);
        representative.setNumberOfTickets(numberOfTickets);
        representative.setPasswordChanged(passwordChanged);

        // Verify getter methods
        assertEquals(empId, representative.getEmpId());
        assertEquals(fName, representative.getfName());
        assertEquals(lName, representative.getlName());
        assertEquals(phoneNo, representative.getPhone_no());
        assertEquals(city, representative.getCity());
        assertEquals(state, representative.getState());
        assertEquals(userName, representative.getUserName());
        assertEquals(password, representative.getPassword());
        assertEquals(domain, representative.getDomain());
        assertEquals(passwordChanged, representative.getPasswordChanged());
    }

   
    @Test
    void testNoArgsConstructor() {
        // Test no-args constructor
        Representative representative = new Representative();
        assertNotNull(representative);
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
        String domain = "Example Domain";
        long numberOfTickets = 5L;
        boolean passwordChanged = true;

        Representative representative = new Representative(empId, fName, lName, phoneNo, city, state, userName, password, domain, numberOfTickets, passwordChanged, null);

        assertEquals(empId, representative.getEmpId());
        assertEquals(fName, representative.getfName());
        assertEquals(lName, representative.getlName());
        assertEquals(phoneNo, representative.getPhone_no());
        assertEquals(city, representative.getCity());
        assertEquals(state, representative.getState());
        assertEquals(userName, representative.getUserName());
        assertEquals(password, representative.getPassword());
        assertEquals(domain, representative.getDomain());
        assertEquals(numberOfTickets, representative.getNumberOfTickets());
        assertEquals(passwordChanged, representative.getPasswordChanged());
    }
}
