package com.suportflow.backend.repository;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Consulta para encontrar um cliente por email (retorna um Optional para tratar casos onde não encontra)
    Optional<Cliente> findByEmail(String email);

    // Consulta para encontrar clientes por parte do nome (ignorando maiúsculas/minúsculas)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Consulta para encontrar clientes por empresa
    List<Cliente> findByEmpresa(Empresa empresa);

    // Consulta para encontrar um cliente por CPF/CNPJ (retorna Optional)
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    
    // Consulta JPQL customizada para buscar clientes por parte do nome OU email (ignorando case)
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Cliente> findByNomeOrEmailContainingIgnoreCase(@Param("termo") String termo);

    // Consulta para verificar se existe um cliente com um determinado email
    boolean existsByEmail(String email);

    // Consulta para verificar se existe um cliente com um determinado CPF/CNPJ
    boolean existsByTelefone(String telefone);

    // Consulta para verificar se existe um cliente com um determinado CPF/CNPJ
    boolean existsByCpfCnpj(String cpfCnpj);

    // Consulta para encontrar clientes por empresa e parte do nome (ignorando maiúsculas/minúsculas)
    List<Cliente> findByEmpresaAndNomeContainingIgnoreCase(Empresa empresa, String nome);
    // Contar o número de clientes de uma determinada empresa
    long countByEmpresa(Empresa empresa);

}