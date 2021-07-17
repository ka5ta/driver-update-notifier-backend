package com.ka5ta.drivers.Controllers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Repositories.DriverRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping("/drivers")
    public List<Driver> allDrivers() {
        return IteratorUtils.toList(driverRepository.findAll().iterator());
    }
}
