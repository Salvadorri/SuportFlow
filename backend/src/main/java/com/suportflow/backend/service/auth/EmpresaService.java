package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa saveEmpresa(Empresa empresa) {
        empresa.setDataCriacao(LocalDateTime.now());
        return empresaRepository.save(empresa);
    }

    public List<Empresa> findAllEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa findEmpresaById(Long id) {
        return empresaRepository.findById(id).orElse(null);
    }

    // Adicione outros métodos conforme necessário (update, delete, etc.)
}