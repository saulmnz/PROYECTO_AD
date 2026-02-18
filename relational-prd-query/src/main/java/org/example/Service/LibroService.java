package org.example.Service;

import org.example.Model.Libro;
import org.example.Repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    // GUARDAR EL LIBRO EN POSTGRESQL
    public void guardarLibro(Libro libro) {
        libroRepository.save(libro);
    }

    // BUSCAR UN LIBRO POR SU ISBN
    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    // BUSCAR LIBROS POR SU NOMBRE
    public List<Libro> buscarPorNome(String nome) {
        return libroRepository.findByNome(nome);
    }
}