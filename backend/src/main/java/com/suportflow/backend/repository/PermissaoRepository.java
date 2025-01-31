package com.suportflow.backend.repository;

import com.suportflow.backend.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Permissao findByNome(String nome);
}