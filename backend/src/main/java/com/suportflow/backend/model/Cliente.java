// java/com/suportflow/backend/model/Cliente.java
package com.suportflow.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "clientes")
@EntityListeners(AuditingEntityListener.class) // Enable auditing for created date
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Email(message = "O email deve ser válido")
    @Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
    @Column(name = "email")
    private String email;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    @Column(name = "telefone")
    private String telefone;

    @Size(max = 20, message = "O CPF/CNPJ deve ter no máximo 20 caracteres")
    @Column(name = "cpf_cnpj", unique = true)
    private String cpfCnpj;

    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chamado> chamados;

    // Construtores (importante ter um construtor padrão sem argumentos para JPA)
    public Cliente() {}

    // Getters e Setters (obrigatório para JPA)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}