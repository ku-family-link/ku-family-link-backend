package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.Guardian;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

  boolean existsByEmail(String email);
  Optional<Guardian> findByEmail(String email);

  Optional<Guardian> findFirstByClientage(FitbitUser clientage);
  List<Guardian> findByClientage(FitbitUser clientage);

}
