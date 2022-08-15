package com.blogpessoal.blogPessoal.controller;

import com.blogpessoal.blogPessoal.model.Usuario;
import com.blogpessoal.blogPessoal.repository.UsuarioRepository;
import com.blogpessoal.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

    /**
     * Injeta um objeto da Classe TestRestTemplate, responsável por fazer requisições HTTP (semelhante ao Postman)
     */
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){

        /**
         * Apaga todos os registros do banco de dados antes de iniciar os testes
         */
        usuarioRepository.deleteAll();
    }


    @Test
    @Order(1)
    @DisplayName("Cadastrar um Usuário")
    public void deveCriarUmUsuario() {

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Klinton Lee", "klinton2018@email.com", "ami2938", "https://i.imgur.com/h4t8loa.jpg"));

        ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
        assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
        assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());

    }

    @Test
    @Order(2)
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void naoDeveDuplicarUsuario() {

        /**
         * Cria um objeto da Classe Usuario e insere dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
         */
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

        /**
         * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será
         * enviada pelo Objeto da Classe TestRestTemplate.
         *
         * Na requisição HTTP será enviada a URL do recurso (/usuarios/cadastrar), o verbo (POST), a entidade
         * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (Usuario).
         */
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        /**
         * Verifica se a requisição retornou o Status Code CREATED (201)
         * Se for verdadeira, o teste passa, se não, o teste falha.
         */
        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());

        /**
         * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição
         * é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no Corpo da Resposta
         * Se for verdadeiro, o teste passa, senão o teste falha.
         */
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());

        /**
         * Verifica se o Atributo Usuario do Objeto da Classe Usuario retornado no Corpo da Requisição
         * é igual ao Atributo Usuario do Objeto da Classe Usuario Retornado no Corpo da Resposta
         * Se for verdadeiro, o teste passa, senão o teste falha.
         */
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }


    //Método 04 - Listar todos os Usuários
    @Test
    @Order(3)
    @DisplayName("Alterar um Usuário")
    public void deveAtualizarUmUsuario() {

        Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
                "Anna Clara", "annaclara@email.com", "2734644", "https://i.imgur.com/h4t8loa.jpg"));

        Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
                "Sofia Gliceiro", "sofiaagliceiro@email.com", "3843744", "https://i.imgur.com/h4t8loa.jpg");

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
        assertEquals(usuarioUpdate.getFoto(), resposta.getBody().getFoto());
        assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @Order(4)
    @DisplayName("Listar todos os Usuários")
    public void deveMostrarTodosUsuarios() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Pietro Lima", "pietroLima@email.com", "2238774", "https://i.imgur.com/h4t8loa.jpg"));

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Eva Rosa", "evarosa@email.com", "3847444", "https://i.imgur.com/h4t8loa.jpg"));


        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/all", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }


}
