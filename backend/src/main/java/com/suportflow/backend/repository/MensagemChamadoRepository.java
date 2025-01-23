package com.suportflow.backend.repository;

import com.suportflow.backend.model.MensagemChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemChamadoRepository extends JpaRepository<MensagemChamado, Long> {

    List<MensagemChamado> findByChamadoId(Long chamadoId);
    List<MensagemChamado> findByUsuarioId(Long usuarioId);
}