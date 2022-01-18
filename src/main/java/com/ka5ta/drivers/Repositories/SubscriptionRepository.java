package com.ka5ta.drivers.Repositories;

import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<EmailProfile, Long> {

    EmailProfile findByEmail(String email);

/*    @Query("SELECT p FROM EmailProfile p WHERE p.products = :product")*/
    List<EmailProfile> findByProducts (Product product);

}
