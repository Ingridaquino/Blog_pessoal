package com.blogpessoal.blogPessoal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity //Transformando a class em uma entidade
@Table(name = "tb_postagens") //Setando a class na tabela no mysql
public class Postagem {

    @Id //Informando que meu id debaixo é o id do mysql
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Q gere
    private Long id;

    @NotBlank(message = "O atributo título é obrigatório!")
    @Size(min = 3, max = 255, message = "O atributo título deve conter no mínimo 03 e no máximo 100 caracteres") //Setando a contidade de letras no titulo
    private String titulo;

    @NotBlank(message = "O atributo título é obrigatório!")
    @Size(min = 5, max = 1000, message = "O atributo título deve conter no mínimo 05 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp //Para atualizar a data de postagem na inrterface
    private LocalDateTime data;

    //Key Estrangeira’’
    @ManyToOne //muitas postagem para um tema
    @JsonIgnoreProperties("postagens") //para não entrar em loop, para que somente um traga o tema.
//    @JoinColumn(name = "postagem_id", nullable = true)
    private Tema tema;

    @ManyToOne
    @JsonIgnoreProperties("postagens")
    private Usuario usuario;

//    public Postagem(String titulo, String texto, LocalDateTime data, Tema tema){
//        this.titulo = titulo;
//        this.texto = texto;
//        this.data = data;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
