package com.alura.literaturaChallengue.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String idioma;
    private Integer descargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autores autores;

    @Override
    public String toString() {
        return "Titulo : " + titulo +
                "\n Autor : " + autor +
                "\n Idioma : " + idioma +
                "\n Descargas : " + descargas;
    }

}