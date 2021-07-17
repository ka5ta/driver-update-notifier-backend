package com.ka5ta.drivers.Repositories;

import com.ka5ta.drivers.Entities.Driver;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverRepository extends CrudRepository<Driver,Long> {

    List<Driver> findByVersion (String version);

    Driver findById (long id);

}
