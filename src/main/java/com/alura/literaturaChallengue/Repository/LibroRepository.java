package com.alura.literaturaChallengue.Repository;

import com.alura.literaturaChallengue.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l")
    List<Libro> listarLibros();

    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> listarLibrosPorIdioma(@Param("idioma") String idioma);
}