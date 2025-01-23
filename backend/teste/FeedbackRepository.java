package com.suportflow.backend.repository;

import com.suportflow.backend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByChamadoId(Long chamadoId);
    List<Feedback> findByClienteId(Long clienteId);
    List<Feedback> findByAvaliacao(int avaliacao);
}