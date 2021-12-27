package com.ka5ta.drivers.Repositories;

import com.ka5ta.drivers.Entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findBySupportLink (String supportLink);
    Product getById (Long id);

}
