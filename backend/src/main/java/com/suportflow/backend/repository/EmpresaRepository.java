package com.suportflow.backend.repository;

import com.suportflow.backend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByNome(String nome);
    Optional<Empresa> findByEmail(String email);
    Optional<Empresa> findByCnpj(String cnpj);

    boolean existsByNome(String nome);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}