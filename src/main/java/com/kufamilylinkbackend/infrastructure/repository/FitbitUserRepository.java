package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitbitUserRepository extends JpaRepository<FitbitUser, String> {

}
