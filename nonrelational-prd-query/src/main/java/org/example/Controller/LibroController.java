package org.example.Controller;

import org.example.Model.Libro;
import org.example.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/nonrelational")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody Libro libro) {
        libroService.guardarLibro(libro);
        return ResponseEntity.ok("LIBRO GUARDADO CON EXITOOO EN MONGO");
    }

    // ENDPOINT PARA BUSCAR POR ISBN
    @GetMapping("/consulta/isbn/{isbn}")
    public ResponseEntity<Libro> consultarPorIsbn(@PathVariable String isbn) {
        Optional<Libro> libro = libroService.buscarPorIsbn(isbn);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT PARA BUSCAR POR NOMBRE
    @GetMapping("/consulta/nome/{nome}")
    public ResponseEntity<List<Libro>> consultarPorNome(@PathVariable String nome) {
        List<Libro> libros = libroService.buscarPorNome(nome);
        if (libros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libros);
    }
}