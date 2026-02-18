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

    public void guardarLibro(Libro libro) {
        libroRepository.save(libro);
    }
    // BUSCAR UN DOCUMENTO POR SU ID (ISBN)
    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libroRepository.findById(isbn);
    }

    // BUSCAR DOCUMENTOS POR SU NOMBRE
    public List<Libro> buscarPorNome(String nome) {
        return libroRepository.findByNome(nome);
    }
}