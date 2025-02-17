package com.uk.uk.repository;

import com.uk.uk.entity.AuthEntity;
import com.uk.uk.entity.PricingInsightsDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends CrudRepository<AuthEntity, Long> {
    AuthEntity findByUserEmail(String userEmail);

    @Query(value = "SELECT * FROM AuthEntity where userEmail=?1 and userPrivilege='admin' ", nativeQuery = true)
    AuthEntity isAdminUserByEmailId(String emailId);
}
