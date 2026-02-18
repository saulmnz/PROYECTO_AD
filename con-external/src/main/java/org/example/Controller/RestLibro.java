package org.example.Controller;

import org.example.Model.Libro;
import org.example.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/libros")
public class RestLibro {

    @Autowired
    private LibroService libroService;

    // ESCUCHA DE PETICIONES POST EN /REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<String> registrarLibro(@RequestBody Libro libro) {
        libroService.enviarLibro(libro);

        // PARA SABER SI SE REALIZÓ DE FORMA CORRECTA O NO
        return ResponseEntity.ok("LIBRO PROCESADO Y ENVIADO A TODOS LOS SERVICIOS CON EXXXITOOOO = " + libro.getNome());
    }

    // GET POR ISBN
    @GetMapping("/consulta/isbn/{isbn}")
    public ResponseEntity<Libro> consultarPorIsbn(@PathVariable String isbn) {
        Libro libro = libroService.buscarPorIsbn(isbn);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.notFound().build();
    }

    // GET POR NOMBRE
    @GetMapping("/consulta/nombre/{nombre}")
    public ResponseEntity<Libro[]> consultarPorNombre(@PathVariable String nombre) {
        Libro[] libros = libroService.buscarPorNombre(nombre);
        if (libros != null && libros.length > 0) {
            return ResponseEntity.ok(libros);
        }
        return ResponseEntity.notFound().build();
    }
}