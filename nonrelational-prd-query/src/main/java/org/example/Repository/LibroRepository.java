package org.example.Repository;

import org.example.Model.Libro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends MongoRepository<Libro, String> {
    List<Libro> findByNome(String nome);
}