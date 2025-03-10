package com.suportflow.backend.dto;

import com.suportflow.backend.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDTO {

    private Long id;
    private String nome;
    private String email;
    private String empresaNome; // Or an EmpresaDTO if you need more details
    private LocalDateTime dataCriacao;
    private Boolean ativo;
    private String telefone;
    private String cpfCnpj;
    private List<String> roles; // Added roles

    public UserDetailsDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        if (user.getEmpresa() != null) {
            this.empresaNome = user.getEmpresa().getNome();
        }
        this.dataCriacao = user.getDataCriacao();
        this.ativo = user.getAtivo();
        this.telefone = user.getTelefone();
        this.cpfCnpj = user.getCpfCnpj();
        // Map permissions to role names.  Crucially, handle nulls.
        this.roles = user.getPermissoes() != null
                ? user.getPermissoes().stream()
                .map(permissao -> permissao.getNome())
                .collect(Collectors.toList())
                : null;  // Or Collections.emptyList() if you prefer
    }
}