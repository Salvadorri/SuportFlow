package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.ChatChamado;
import com.suportflow.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatChamadoRepository extends JpaRepository<ChatChamado, Long> {

    List<ChatChamado> findByChamado(Chamado chamado);
    Page<ChatChamado> findByChamado(Chamado chamado, Pageable pageable); // For pagination

    List<ChatChamado> findByUser(User user);
    Page<ChatChamado> findByUser(User user, Pageable pageable);

    List<ChatChamado> findByChamadoAndUser(Chamado chamado, User user);
    Page<ChatChamado> findByChamadoAndUser(Chamado chamado, User user, Pageable pageable);

    // Example: Find chat messages for a Chamado, ordered by date/time
    List<ChatChamado> findByChamadoOrderByDataHoraAsc(Chamado chamado);
    Page<ChatChamado> findByChamadoOrderByDataHoraAsc(Chamado chamado, Pageable pageable);

    List<ChatChamado> findByChamadoOrderByDataHoraDesc(Chamado chamado);
    Page<ChatChamado> findByChamadoOrderByDataHoraDesc(Chamado chamado, Pageable pageable);

}