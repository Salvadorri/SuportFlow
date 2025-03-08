package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.StatusChamado;
import com.suportflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    // Consulta para encontrar chamados por cliente
    List<Chamado> findByCliente(Cliente cliente);

    // Consulta para encontrar chamados por atendente (User)
    List<Chamado> findByAtendente(User atendente);

    // Consulta para encontrar chamados por status
    List<Chamado> findByStatus(StatusChamado status);

    //Consulta para buscar chamados por cliente e status
    List<Chamado> findByClienteAndStatus(Cliente cliente, StatusChamado status);

    // Consulta para encontrar chamados abertos em um determinado intervalo de tempo
    List<Chamado> findByDataAberturaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Consulta para encontrar chamados fechados em um determinado intervalo de tempo
    List<Chamado> findByDataFechamentoBetween(LocalDateTime start, LocalDateTime end);

    //Consulta para buscar chamados por prioridade e status
    List<Chamado> findByPrioridadeAndStatus(String prioridade, String status);

    // Consulta JPQL customizada para buscar chamados por título ou descrição (ignorando case)
    @Query("SELECT c FROM Chamado c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Chamado> findByTituloOrDescricaoContainingIgnoreCase(@Param("termo") String termo);

    //Contagem de chamados por status
    long countByStatus(StatusChamado status);

    // Consulta JPQL customizada para buscar chamados de um cliente específico, filtrando por título ou descrição.
    @Query("SELECT c FROM Chamado c WHERE c.cliente = :cliente AND (LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Chamado> findByClienteAndTituloOrDescricaoContainingIgnoreCase(@Param("cliente") Cliente cliente, @Param("termo") String termo);

}