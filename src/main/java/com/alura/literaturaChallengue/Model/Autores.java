package com.alura.literaturaChallengue.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Autores")
public class Autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String autor;
    private Integer fechaNacimento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();  // Inicializar la lista

    @Override
    public String toString() {
        String librosTitulos = libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", "));
        return "AUTORES" +
                "\nAutor : " + autor +
                "\nFecha de nacimiento : " + fechaNacimento +
                "\nFecha de fallecimiento : " + fechaFallecimiento +
                "\nLibros : " + librosTitulos;
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
        libro.setAutores(this);
    }
}