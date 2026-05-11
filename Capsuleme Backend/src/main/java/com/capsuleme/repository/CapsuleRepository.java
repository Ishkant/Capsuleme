package com.capsuleme.repository;

import com.capsuleme.model.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {

    List<Capsule> findByUserEmail(String userEmail);

    List<Capsule> findByUnlockDateLessThanEqualAndUnlockedFalse(LocalDate date);
}
