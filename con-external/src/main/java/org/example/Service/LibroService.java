package org.example.Service;

import org.example.Model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LibroService {

    @Autowired
    private RestTemplate rest;

    // METODO PARA ENVIAR EL LIBRO AL MICROSERVICIO PRD-REX
    public void enviarLibro(Libro libro) {

        // DEFINIMOS LA URL DESTINO DEL MICROSERVICIO PRD-REX
        String urlDestino = "http://localhost:8090/api/v1/prdrex/registro";

        // ENVIAMOS EL OBJETO LIBRO POR POST
        rest.postForObject(urlDestino, libro, String.class);
    }

    // GET POR ISBN
    public Libro buscarPorIsbn(String isbn) {
        String url = "http://localhost:8091/api/v1/relational/consulta/isbn/" + isbn;
        try {
            return rest.getForObject(url, Libro.class);
        } catch (Exception e) {
            return null;
        }
    }

    // GET POR NOMBRE
    public Libro[] buscarPorNombre(String nombre) {
        String url = "http://localhost:8091/api/v1/relational/consulta/nombre/" + nombre;
        try {
            return rest.getForObject(url, Libro[].class);
        } catch (Exception e) {
            return null;
        }
    }
}