package com.example.Manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.example.EmployeeService.ServletInitializer;

import static org.junit.jupiter.api.Assertions.*;
 
public class ServletInitializerTest {
 
    @Test
    public void testConfigure() {
        // Arrange
        ServletInitializer servletInitializer = new ServletInitializer();
 
        // Act
        SpringApplicationBuilder builder = servletInitializer.configure(new SpringApplicationBuilder());
 
        // Assert
        assertNotNull(builder);
    }
}

