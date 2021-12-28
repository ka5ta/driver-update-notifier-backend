package com.ka5ta.drivers.Controllers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class DriverController {


    @Autowired
    private DriverService driverService;


/*    @GetMapping("/all-drivers")
    public List<Driver> getDrivers(@RequestParam("supportUrl") String supportUrl) throws Exception {

        String asusLinkExample = "https://www.asus.com/Motherboards-Components/Motherboards/PRIME/PRIME-Z690-A-CSM/HelpDesk_Download/";
        String msiLinkExample = "https://www.msi.com/Motherboard/B450-TOMAHAWK-MAX/support";

        return driverService.getDriversForProduct(supportUrl);
    }*/

    @GetMapping("/drivers")
    public Product getProduct(@RequestParam("supportUrl") String supportUrl) throws Exception {

        String asusLinkExample = "https://www.asus.com/Motherboards-Components/Motherboards/PRIME/PRIME-Z690-A-CSM/HelpDesk_Download/";
        String msiLinkExample = "https://www.msi.com/Motherboard/B450-TOMAHAWK-MAX/support";

        return driverService.getProductFromLink(supportUrl);
    }
}
