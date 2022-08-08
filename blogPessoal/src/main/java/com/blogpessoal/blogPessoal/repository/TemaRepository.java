package com.blogpessoal.blogPessoal.repository;

import com.blogpessoal.blogPessoal.model.Postagem;
import com.blogpessoal.blogPessoal.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {
    public List<Tema> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

}
