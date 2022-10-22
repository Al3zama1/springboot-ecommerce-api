package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("api/ecommerce/v1/employees")
@Validated
public class EmployeesController {

    private final EmployeesService employeesService;

    @Autowired
    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @PatchMapping("/ship-order/{orderNumber}")
    public ResponseEntity<Void> shipOrder(
            @Positive@PathVariable long orderNumber
    ) {
        employeesService.shipOrder(orderNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/deliver-order/{orderNumber}")
    public ResponseEntity<Void> deliverOrder(
            @Positive@PathVariable long orderNumber
    ) {
        employeesService.deliverOrder(orderNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
