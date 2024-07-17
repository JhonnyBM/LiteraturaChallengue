package com.alura.literaturaChallengue.Principal;

import com.alura.literaturaChallengue.Controller.ConsumoAPI;
import com.alura.literaturaChallengue.Model.Autores;
import com.alura.literaturaChallengue.Model.Book;
import com.alura.literaturaChallengue.Model.Libro;
import com.alura.literaturaChallengue.Repository.AutoresRepository;
import com.alura.literaturaChallengue.Repository.LibroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class Principal {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutoresRepository autoresRepository;

    private final Scanner scanner = new Scanner(System.in);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void mostrarMenu() throws JsonProcessingException {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1- Buscar Libro por titulo
                    2- Listar Libros registrados
                    3- Listar Autores registrados
                    4- Listar Autores vivos en determinado año
                    5- Listar Libros por idioma

                    0- Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosPorFecha();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private void buscarLibro() throws JsonProcessingException {
        ConsumoAPI consumoApi = new ConsumoAPI();
        System.out.println("Escribe el nombre del libro que desees buscar: ");
        var nombreLibro = scanner.nextLine();
        String url_Base = "https://gutendex.com/books?search=";
        var json = consumoApi.obtenerDatos(url_Base + nombreLibro.replace(" ", "%20"));

        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode resultsNode = rootNode.path("results");

        // Buscar primer libro
        if (!resultsNode.isEmpty()) {
            JsonNode firstResult = resultsNode.get(0);

            String title = firstResult.path("title").asText();
            List<String> authors = StreamSupport.stream(firstResult.path("authors").spliterator(), false)
                    .map(authorNode -> authorNode.path("name").asText())
                    .collect(Collectors.toList());
            List<String> languages = StreamSupport.stream(firstResult.path("languages").spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
            int downloadCount = firstResult.path("download_count").asInt();

            Book firstBook = new Book(title, authors, languages, downloadCount);

            Autores autor = null;
            // Buscar primer autor
            if (!authors.isEmpty()) {
                String authorName = authors.get(0);
                JsonNode authorNode = firstResult.path("authors").get(0);

                int birthYear = authorNode.path("birth_year").asInt(0);
                int deathYear = authorNode.path("death_year").asInt(0);

                Optional<Autores> optionalAutor = autoresRepository.findByAutor(authorName);

                if (optionalAutor.isPresent()) {
                    autor = optionalAutor.get();
                } else {
                    autor = Autores.builder()
                            .autor(authorName)
                            .fechaNacimento(birthYear != 0 ? birthYear : null)
                            .fechaFallecimiento(deathYear != 0 ? deathYear : null)
                            .libros(new ArrayList<>()) // Inicializar la lista de libros
                            .build();
                    autoresRepository.save(autor);
                }
            }

            if (autor != null) {
                Libro entityLibro = Libro.builder()
                        .titulo(firstBook.getTitle())
                        .autor(autor.getAutor())
                        .idioma(String.join(", ", firstBook.getLanguages()))
                        .descargas(firstBook.getDownloadCount())
                        .autores(autor)
                        .build();

                autor.addLibro(entityLibro);
                autoresRepository.save(autor);

                System.out.println(entityLibro);
            }
        }
    }

    private void listarLibros() {
        libroRepository.listarLibros().forEach(System.out::println);
    }

    private void listarAutores() {
        autoresRepository.listarAutores().forEach(System.out::println);
    }

    private void listarAutoresVivosPorFecha() {
        System.out.println("Ingrese una fecha posterior al nacimiento del autor de búsqueda");
        int fecha = scanner.nextInt();
        scanner.nextLine();
        autoresRepository.listarAutoresPorFecha(fecha).forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma que quiere filtrar:
                1.- es - Español
                2.- en - Inglés
                3.- fr - Francés
                4.- pt - Portugués""");
        String idioma = scanner.nextLine();
        libroRepository.listarLibrosPorIdioma(idioma).forEach(System.out::println);
    }
}
