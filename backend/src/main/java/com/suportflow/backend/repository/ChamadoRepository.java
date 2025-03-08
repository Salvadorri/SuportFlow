package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;
import com.suportflow.backend.model.User;
import com.suportflow.backend.model.CategoriaChamado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    // Find by ID (Optional, for safety)
    Optional<Chamado> findById(Long id);

    // Find by Cliente
    List<Chamado> findByCliente(Cliente cliente);
    Page<Chamado> findByCliente(Cliente cliente, Pageable pageable); // With pagination

    // Find by Atendente
    List<Chamado> findByAtendente(User atendente);
    Page<Chamado> findByAtendente(User atendente, Pageable pageable);

    // Find by Status
    List<Chamado> findByStatus(StatusChamado status);
    Page<Chamado> findByStatus(StatusChamado status, Pageable pageable);

    // Find by Cliente and Status
    List<Chamado> findByClienteAndStatus(Cliente cliente, StatusChamado status);
    Page<Chamado> findByClienteAndStatus(Cliente cliente, StatusChamado status, Pageable pageable);

    // Find by Data Abertura (Between)
    List<Chamado> findByDataAberturaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    Page<Chamado> findByDataAberturaBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);

    // Find by Data Fechamento (Between)
    List<Chamado> findByDataFechamentoBetween(LocalDateTime start, LocalDateTime end);
    Page<Chamado> findByDataFechamentoBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


    // Find by Prioridade and Status
    List<Chamado> findByPrioridadeAndStatus(PrioridadeChamado prioridade, StatusChamado status);
    Page<Chamado> findByPrioridadeAndStatus(PrioridadeChamado prioridade, StatusChamado status, Pageable pageable);

    // Find by Categoria
    List<Chamado> findByCategoria(CategoriaChamado categoria);
    Page<Chamado> findByCategoria(CategoriaChamado categoria, Pageable pageable);

    // Find by Categoria and Status
    List<Chamado> findByCategoriaAndStatus(CategoriaChamado categoria, StatusChamado status);
    Page<Chamado> findByCategoriaAndStatus(CategoriaChamado categoria, StatusChamado status, Pageable pageable);


    // Search (Titulo or Descricao)
    @Query("SELECT c FROM Chamado c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Chamado> searchByTituloOrDescricao(@Param("termo") String termo);
    @Query("SELECT c FROM Chamado c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Chamado> searchByTituloOrDescricao(@Param("termo") String termo, Pageable pageable);


    // Search by Cliente and (Titulo or Descricao)
    @Query("SELECT c FROM Chamado c WHERE c.cliente = :cliente AND (LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Chamado> searchByClienteAndTituloOrDescricao(@Param("cliente") Cliente cliente, @Param("termo") String termo);
    @Query("SELECT c FROM Chamado c WHERE c.cliente = :cliente AND (LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Chamado> searchByClienteAndTituloOrDescricao(@Param("cliente") Cliente cliente, @Param("termo") String termo, Pageable pageable);

    // Count by Status
    long countByStatus(StatusChamado status);

    // Count by Categoria
    long countByCategoria(CategoriaChamado categoria);

    // Find Chamados within a date range and with specific status
    @Query("SELECT c FROM Chamado c WHERE c.dataAbertura BETWEEN :startDate AND :endDate AND c.status = :status")
    List<Chamado> findByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") StatusChamado status);
    @Query("SELECT c FROM Chamado c WHERE c.dataAbertura BETWEEN :startDate AND :endDate AND c.status = :status")
    Page<Chamado> findByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") StatusChamado status, Pageable pageable);

}