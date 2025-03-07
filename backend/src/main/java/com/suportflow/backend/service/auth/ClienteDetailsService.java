// ClienteDetailsService.java (NOVO) - Foca em CLIENTES
package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
public class ClienteDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com email: " + email));

        // Importante:  Clientes não têm permissões (roles) no seu modelo atual.
        // Se você adicionar permissões a Cliente no futuro, precisará carregá-las aqui,
        // similar ao que foi feito para User.  Por agora, retornamos uma lista vazia.

        // Cria um UserDetails a partir do Cliente.  Usamos um construtor customizado
        // que você precisará adicionar na classe Cliente (veja o passo 2).
        return new org.springframework.security.core.userdetails.User(
                cliente.getEmail(),
                cliente.getCpfCnpj(), // Usamos cpfCnpj como senha, importante ter o passwordEncoder.encode
                Collections.emptyList() // Sem authorities (permissões) por enquanto.
        );
    }
}