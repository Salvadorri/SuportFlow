package com.suportflow.backend.repository;

import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.ChatMensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMensagemRepository extends JpaRepository<ChatMensagem, Long> {

    List<ChatMensagem> findByChamado(Chamado chamado);
    Page<ChatMensagem> findByChamado(Chamado chamado, Pageable pageable);
}