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

    Optional<Cliente> findByEmail(String email);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByEmpresa(Empresa empresa);
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Cliente> findByNomeOrEmailContainingIgnoreCase(@Param("termo") String termo);
    boolean existsByEmail(String email);
    boolean existsByCpfCnpj(String cpfCnpj);
    boolean existsByTelefone(String telefone); // Added existsByTelefone
    List<Cliente> findByEmpresaAndNomeContainingIgnoreCase(Empresa empresa, String nome);
    long countByEmpresa(Empresa empresa);

}