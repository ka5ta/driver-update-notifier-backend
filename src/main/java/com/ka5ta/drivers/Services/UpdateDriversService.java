package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.DriversApplication;

import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.DriverRepository;
import com.ka5ta.drivers.Repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UpdateDriversService {

    private static final Logger log = LoggerFactory.getLogger(UpdateDriversService.class);

    @Autowired
    private DriverService driverService;
    @Autowired
    private ProductRepository productRepository;

//private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //todo schedule update drivers
    //@Scheduled(cron = "0 */5 * ? * *")
    private void runUpdateDrivers() {
        updateDrivers();
        log.info("Drivers are now updated");
    }

    private void updateDrivers() {
        List<String> downloadLinks = productRepository.getDownloadLinks();
        downloadLinks.forEach(link -> {
            try {
                Product productFromLink = driverService.getProductFromLink(link);
                log.info(productFromLink.getName() + " was updated");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
