package com.suportflow.backend.repository;

import com.suportflow.backend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importante

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> { // Long se o ID for Long
    Optional<Empresa> findByNome(String nome); // Retorna Optional<Empresa>
    boolean existsByNome(String nome); // Boa prática
}