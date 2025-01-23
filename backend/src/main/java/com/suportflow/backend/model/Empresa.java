package com.suportflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empresa_id")
    private Long id;

    @Column(name = "nome", unique = true, nullable = false)
    private String nome;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "cnpj", unique = true, length = 14) // Adicionando o campo CNPJ
    private String cnpj;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "empresa")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa")
    private List<Chamado> chamados;

    @OneToMany(mappedBy = "empresa")
    private List<BaseConhecimento> artigos;

    @OneToMany(mappedBy = "empresa")
    private List<Cliente> clientes;
}