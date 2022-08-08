package com.blogpessoal.blogPessoal.controller;


import com.blogpessoal.blogPessoal.model.Postagem;
import com.blogpessoal.blogPessoal.repository.PostagemRepository;
import com.blogpessoal.blogPessoal.repository.TemaRepository;
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
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController{

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Postagem>> getAll(){
        return ResponseEntity.ok(postagemRepository.findAll());
    }

    @GetMapping("/{id}")
    //Fazendo uma buscar por id ==> PathVariable seria a validação
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {
        //acessando a postagem pelo id, mapeando a busca para saber se existe o id senao ele volta uma mensagem de erro se não existir
        return postagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List <Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
        if (temaRepository.existsById(postagem.getTema().getId()))
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postagemRepository.save(postagem));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
       if (postagemRepository.existsById(postagem.getId())) {
           if (temaRepository.existsById(postagem.getTema().getId()))
               return ResponseEntity.status(HttpStatus.CREATED)
                       .body(postagemRepository.save(postagem));
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }

       return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Optional<Postagem> postagem = postagemRepository.findById(id);

        if(postagem.isEmpty())
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);

        postagemRepository.deleteById(id);
    }


}
