package com.example.EmployeeService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class ServletInitializerTest {

	@Test
    void testConfigure() {
        // Create an instance of ServletInitializer
        ServletInitializer servletInitializer = new ServletInitializer();
        
        // Call the configure method
        SpringApplicationBuilder builder = servletInitializer.configure(new SpringApplicationBuilder());
        
        // Check that the builder is not null
        assertNotNull(builder)
	}

}
