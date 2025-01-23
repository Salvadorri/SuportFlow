package com.suportflow.backend.repository;

import com.suportflow.backend.model.BaseConhecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseConhecimentoRepository extends JpaRepository<BaseConhecimento, Long> {

    List<BaseConhecimento> findByEmpresaId(Long empresaId);
    List<BaseConhecimento> findByTituloContainingIgnoreCase(String titulo);
    List<BaseConhecimento> findByPalavrasChaveContainingIgnoreCase(String palavraChave);
}