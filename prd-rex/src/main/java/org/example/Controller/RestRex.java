package org.example.Controller;

import org.example.Model.Libro;
import org.example.Service.RexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// RUTA BASE DE ESTE MICROSERVICIO (LA MISMA A LA QUE LLAMA CON-EXTERNAL)
@RequestMapping("/api/v1/prdrex")
public class RestRex {

    @Autowired
    private RexService rexService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody Libro libro) {

        // PROCESAMOS EL LIBRO
        rexService.procesarLibro(libro);

        // DEVOLVEMOS QUE TODO HA IDO BIEN
        return ResponseEntity.ok("REGISTRO XML CREADO CORRECTAMENTE PARAAAA EL LIBRO = " + libro.getNome());
    }

    // NUEVO ENDPOINT PARA CONSULTAR EL XML POR ISBN
    @GetMapping("/consulta/isbn/{isbn}")
    public ResponseEntity<Libro> consultarXmlPorIsbn(@PathVariable String isbn) {

        // BUSCAMOS EL FICHERO
        Optional<Libro> libro = rexService.buscarPorIsbn(isbn);

        // DEVOLVEMOS EL LIBRO SI EXISTE EL XML
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT PARA CONSULTAR XML POR NOMBRE
    @GetMapping("/consulta/nome/{nome}")
    public ResponseEntity<List<Libro>> consultarXmlPorNome(@PathVariable String nome) {

        // BUSCAMOS TODOS LOS FICHEROS XML QUE COINCIDAN CON ESE NOMBRE
        List<Libro> libros = rexService.buscarPorNome(nome);

        if (libros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libros);
    }
}