package com.suportflow.backend.repository;

import com.suportflow.backend.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importante

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> { // Long se o ID for Long

    Optional<Permissao> findByNome(String nome); // Retorna Optional<Permissao>
    boolean existsByNome(String nome); //Verifica se existe
}