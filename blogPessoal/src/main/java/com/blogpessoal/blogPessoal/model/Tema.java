package com.blogpessoal.blogPessoal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_temas")
public class Tema {

    @Id //Informando que meu id debaixo é o id do mysql
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Q gere
    private Long id;

    @NotBlank(message = "O atributo título é obrigatório!")
    @Size(min = 3, max = 255, message = "O atributo título deve conter no mínimo 03 e no máximo 100 caracteres") //Setando a contidade de letras no titulo
    private String descricao;

    //Key Estrangeira (configuração de volta)
    @OneToMany(mappedBy = "tema", cascade = CascadeType.REMOVE) // mappedBy ==> faz o mapiamento para validar
    @JsonIgnoreProperties("tema")
    private List<Postagem>postagem;


    public Long getId() {
        return id;
    };

    public void setId(Long id) {

        this.id = id;
    }

    public String getDescricao() {

        return descricao;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;
    }
}
