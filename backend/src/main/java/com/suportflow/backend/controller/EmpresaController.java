package com.suportflow.backend.controller;

import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.service.auth.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        Empresa novaEmpresa = empresaService.saveEmpresa(empresa);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.findAllEmpresas();
        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Empresa empresa = empresaService.findEmpresaById(id);
        if (empresa != null) {
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Adicione outros endpoints conforme necessário (PUT para atualizar, DELETE para excluir, etc.)
}