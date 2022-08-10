package com.blogpessoal.blogPessoal.service;

import com.blogpessoal.blogPessoal.model.Usuario;
import com.blogpessoal.blogPessoal.model.UsuarioLogin;
import com.blogpessoal.blogPessoal.repository.UsuarioRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 *  A Classe UsuarioService implementa as regras de negócio do Recurso Usuario.
 *
 *  Regras de negócio são as particularidades das funcionalidades a serem
 *  implementadas no objeto, tais como:
 *
 *  1) O Usuário não pode estar duplicado no Banco de dados
 *  2) A senha do Usuario deve ser criptografada
 *
 *  Observe que toda a implementação dos metodos Cadastrar, Atualizar e
 *  Logar estão implmentadas na classe de serviço, enquanto a Classe
 *  Controller se limitará a checar a resposta da requisição.
 */

/**
 * A Anotação @Service indica que esta é uma Classe de Serviço, ou seja,
 * implementa todas regras de negócio do Recurso Usuário.
 */


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Usuário já existe!", null);

        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        return Optional.of(usuarioRepository.save(usuario));
    }


    public Optional<Usuario> atualizarUsuario(Usuario usuario) {

        if(usuarioRepository.findById(usuario.getId()).isPresent()) {

            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());

            if (buscaUsuario.isPresent()) {

                if (buscaUsuario.get().getId() != usuario.getId())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já existe!", null);
            }

            usuario.setSenha(criptografarSenha(usuario.getUsuario()));

            return Optional.of(usuarioRepository.save(usuario));
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado", null);
    }

    public Optional <UsuarioLogin> logarUsuario(

            Optional<UsuarioLogin> usuarioLogin) {

        Optional<Usuario> usuario = usuarioRepository
                .findByUsuario(usuarioLogin.get().getUsuario());

        if (usuario.isPresent()) {
            if (compararSenhas(usuarioLogin.get().getSenha(),usuario.get().getSenha())) {

                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
                usuarioLogin.get().setSenha(usuario.get().getSenha());
                return usuarioLogin;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaEncoder = encoder.encode(senha);
        return senhaEncoder;
    }
    private boolean compararSenhas(String senhaDigitada,
                                   String senhaBanco) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada, senhaBanco);
    }
    private String gerarBasicToken(String email, String password) {
        String estrutura = email + ":" + password;

        byte[] estruturaBase64 = Base64.encodeBase64(
                estrutura.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(estruturaBase64);
    }
}