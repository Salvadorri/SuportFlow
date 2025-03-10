// backend/src/main/java/com/suportflow/backend/repository/ChatMensagemRepository.java
package com.suportflow.backend.repository;

import com.suportflow.backend.model.ChatMensagem;
import com.suportflow.backend.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMensagemRepository extends JpaRepository<ChatMensagem, Long> {

    List<ChatMensagem> findByChamado(Chamado chamado);
}