package com.suportflow.backend.repository;

import com.suportflow.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEmpresaId(Long empresaId);
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}