package com.suportflow.backend.repository;

import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCpfCnpj(String cpfCnpj);

  boolean existsByEmailOrCpfCnpj(String email, String cpfCnpj);

  Page<User> findByEmpresa(Empresa empresa, Pageable pageable);

  Optional<User> findByCpfCnpj(String cpfCnpj);

}