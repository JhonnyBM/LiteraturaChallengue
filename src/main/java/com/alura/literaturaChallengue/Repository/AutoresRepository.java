package com.alura.literaturaChallengue.Repository;

import com.alura.literaturaChallengue.Model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoresRepository extends JpaRepository<Autores, Long> {
    Optional<Autores> findByAutor(String autor);

    @Query("SELECT a FROM Autores a")
    List<Autores> listarAutores();

    @Query("SELECT a FROM Autores a WHERE (a.fechaDeNacimento <= :fecha AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :fecha))")
    List<Autores> listarAutoresPorFecha(@Param("fecha") int fecha);

}
