package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.StatusChamado;
import com.suportflow.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByCliente(Cliente cliente);
    Page<Chamado> findByCliente(Cliente cliente, Pageable pageable);

    List<Chamado> findByAtendente(User atendente);
    Page<Chamado> findByAtendente(User atendente, Pageable pageable);

    List<Chamado> findByStatus(StatusChamado status);
    Page<Chamado> findByStatus(StatusChamado status, Pageable pageable);

    List<Chamado> findByClienteAndStatus(Cliente cliente, StatusChamado status);
    Page<Chamado> findByClienteAndStatus(Cliente cliente, StatusChamado status, Pageable pageable);

    List<Chamado> findByDataAberturaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    Page<Chamado> findByDataAberturaBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);

    List<Chamado> findByDataFechamentoBetween(LocalDateTime start, LocalDateTime end);
    Page<Chamado> findByDataFechamentoBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Chamado> findByPrioridadeAndStatus(String prioridade, String status);
    Page<Chamado> findByPrioridadeAndStatus(String prioridade, String status, Pageable pageable);

    @Query("SELECT c FROM Chamado c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Chamado> findByTituloOrDescricaoContainingIgnoreCase(@Param("termo") String termo);

    @Query("SELECT c FROM Chamado c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Chamado> findByTituloOrDescricaoContainingIgnoreCase(@Param("termo") String termo, Pageable pageable);


    long countByStatus(StatusChamado status);

    @Query("SELECT c FROM Chamado c WHERE c.cliente = :cliente AND (LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Chamado> findByClienteAndTituloOrDescricaoContainingIgnoreCase(@Param("cliente") Cliente cliente, @Param("termo") String termo);

    @Query("SELECT c FROM Chamado c WHERE c.cliente = :cliente AND (LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Chamado> findByClienteAndTituloOrDescricaoContainingIgnoreCase(@Param("cliente") Cliente cliente, @Param("termo") String termo, Pageable pageable);

}