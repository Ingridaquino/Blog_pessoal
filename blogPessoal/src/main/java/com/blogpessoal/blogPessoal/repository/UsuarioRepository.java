package com.blogpessoal.blogPessoal.repository;

import com.blogpessoal.blogPessoal.model.Usuario;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository {

    public List<Usuario> findAllByUsuarioContainingIgnoreCase(@Param("usuario") String usuario);
}
