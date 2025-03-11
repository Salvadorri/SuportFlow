package com.suportflow.backend.repository;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    List<Cliente> findByEmpresa(Empresa empresa);
    Page<Cliente> findByEmpresa(Empresa empresa, Pageable pageable);

    Optional<Cliente> findByCpfCnpj(String cpfCnpj);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Cliente> findByNomeOrEmailContainingIgnoreCase(@Param("termo") String termo);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Cliente> findByNomeOrEmailContainingIgnoreCase(@Param("termo") String termo, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByCpfCnpj(String cpfCnpj);

    boolean existsByTelefone(String telefone);

    List<Cliente> findByEmpresaAndNomeContainingIgnoreCase(Empresa empresa, String nome);

    Page<Cliente> findByEmpresaAndNomeContainingIgnoreCase(Empresa empresa, String nome, Pageable pageable);

    long countByEmpresa(Empresa empresa);

    Optional<Cliente> findByEmailAndEmpresa(String email, Empresa empresa);

    Optional<Cliente> findByCpfCnpjAndEmpresa(String cpfCnpj, Empresa empresa);
}