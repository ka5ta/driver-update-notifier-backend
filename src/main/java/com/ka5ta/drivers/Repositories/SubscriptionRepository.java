package com.ka5ta.drivers.Repositories;

import com.ka5ta.drivers.Entities.EmailProfile;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<EmailProfile, Long> {

    EmailProfile findByEmail(String email);
}
