package com.blogpessoal.blogPessoal.repository;


import com.blogpessoal.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final
class UsuarioRepositoryTeste {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start() {

        usuarioRepository.deleteAll();
        usuarioRepository.save(new Usuario(0L, "João da Silva", "joaao@email.com", "1234han", "https://i.imgur.com/h4t8loa.jpg" ));
        usuarioRepository.save(new Usuario(0L, "Manuela Sue", "suemanu@email.com", "han2334", "https://i.imgur.com/NtyGneo.jpg" ));
        usuarioRepository.save(new Usuario(0L, "Miguel Aquino", "miguelaquino@email.com", "aqui123", "https://i.imgur.com/5m2p5Wb.jpg" ));
        usuarioRepository.save(new Usuario(0L, "Larissa Pazetti", "LarissaPazetti@email.com", "zetti3746", "https://i.imgur.com/FETvs20.jpg" ));

    }

    @Test
    @DisplayName("Retorna 1 usuario")
    public void deveRetornarUmUsuario() {

        Optional<Usuario> usuario = usuarioRepository.findByUsuario("miguelaquino@email.com");
        assertTrue(usuario.get().getUsuario().equals("miguelaquino@email.com"));
    }

    @Test
    @DisplayName("Retorna 3 usuarios")
    public void deveRertornarTresUsuarios() {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Aquino");
        assertEquals(3, listaDeUsuarios.size());
        assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
        assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela Sue"));
        assertTrue(listaDeUsuarios.get(2).getNome().equals("Miguel Aquino"));
    }

}
