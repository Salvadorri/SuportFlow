// src/main/java/com/suportflow/backend/controller/ClienteController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasRole('USER')") // Only users can get all clients
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDTO> clienteDTOs = clientes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clienteDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or (hasRole('CLIENTE') and #id == authentication.principal.username)")  // Users can get any client; Clients can get their own
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.findById(id);

            // Check if the authenticated user is a client and if they are accessing their own data
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                // Ensure principal.username is the *email*, not ID.  Comparison should be to cliente.getEmail()
                if (!cliente.getEmail().equals(authentication.getName())) { // Use getName() to get the email
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Or UNAUTHORIZED, but FORBIDDEN is more appropriate here
                }
            }
            ClienteDTO clienteDTO = convertToDto(cliente);
            return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.save(cliente);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or (hasRole('CLIENTE') and #id == authentication.principal.username)") // Users can update any; Clients can update their own
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {

             // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Cliente clienteExistente = clienteService.findById(id);

              // Check if it's a CLIENT and if they are modifying their own account
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
              if (!clienteExistente.getEmail().equals(authentication.getName())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }


            Cliente clienteAtualizadoResult = clienteService.update(id, clienteAtualizado);
            return new ResponseEntity<>(clienteAtualizadoResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')") // Only users can delete clients
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private ClienteDTO convertToDto(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setEmail(cliente.getEmail());
        return clienteDTO;
    }
}