package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByClienteId(Long clienteId);

    List<Chamado> findByFuncionarioId(Long funcionarioId);

    List<Chamado> findByEmpresaId(Long empresaId);

    List<Chamado> findByStatus(StatusChamado status);

    List<Chamado> findByPrioridade(String prioridade);

    List<Chamado> findByCategoria(String categoria);

    List<Chamado> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT c FROM Chamado c WHERE c.empresa.id = :empresaId AND c.funcionario IS NULL")
    List<Chamado> findChamadosNaoAtribuidosByEmpresaId(@Param("empresaId") Long empresaId);
}