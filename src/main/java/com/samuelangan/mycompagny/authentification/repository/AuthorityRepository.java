package com.samuelangan.mycompagny.authentification.repository;

import com.samuelangan.mycompagny.authentification.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,String> {
}
