package com.blogpessoal.blogPessoal.controller;


import com.blogpessoal.blogPessoal.model.Usuario;
import com.blogpessoal.blogPessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioRepository.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id){
        return usuarioRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta)) // funcao lambida parecido com arrowfunction do js
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<Usuario>> getByTitulo(@PathVariable String usuario){
        return ResponseEntity.ok(usuarioRepository.findAllByUsuarioContainingIgnoreCase(usuario));
    }

    @PostMapping
    public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioRepository.save(usuario));
    }

    @PutMapping
    public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario){
        return usuarioRepository.findById(usuario.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                        .body(usuarioRepository.save(usuario)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if(usuario.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        usuarioRepository.deleteById(id);
    }
}
