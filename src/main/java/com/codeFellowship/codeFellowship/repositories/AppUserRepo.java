package com.codeFellowship.codeFellowship.repositories;

import com.codeFellowship.codeFellowship.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
