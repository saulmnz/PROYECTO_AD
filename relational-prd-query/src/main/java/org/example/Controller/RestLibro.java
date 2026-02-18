package org.example.Controller;

import org.example.Model.Libro;
import org.example.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// RUTA BASE, COINCIDIENDO CON LA QUE ENVIA PRD-REX
@RequestMapping("/api/v1/relational")
public class RestLibro {

    @Autowired
    private LibroService libroService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody Libro libro) {
        libroService.guardarLibro(libro);
        return ResponseEntity.ok("LIBRO ALMACENADO EN POSTGRESSSS");
    }
    // ENDPOINT PARA BUSCAR POR ISBN DIRECTAMENTE EN LA URL
    @GetMapping("/consulta/isbn/{isbn}")
    public ResponseEntity<Libro> consultarPorIsbn(@PathVariable String isbn) {
        Optional<Libro> libro = libroService.buscarPorIsbn(isbn);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT PARA BUSCAR POR NOMBRE
    @GetMapping("/consulta/nombre/{nome}")
    public ResponseEntity<List<Libro>> consultarPorNome(@PathVariable String nome) {
        List<Libro> libros = libroService.buscarPorNome(nome);
        if (libros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libros);
    }

}