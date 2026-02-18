package org.example.Service;

import org.example.Model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class LibroService {

    @Autowired
    private RestTemplate rest;

    // ENVIAR EL LIBRO AL RESTO DE MICROSERVICIOS
    public void enviarLibro(Libro libro) {

        // ENVIAMOS A PRD-REX (PARA QUE HAGA EL XML)
        try {
            String urlRex = "http://localhost:8090/api/v1/prdrex/registro";
            rest.postForObject(urlRex, libro, String.class);
        } catch (Exception e) {
            System.out.println("ERROOORCH AL ENVIAR A PRD-REX: " + e.getMessage());
        }

        // CABECERAS JSON PARA LAS BASES DE DATOS
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Libro> request = new HttpEntity<>(libro, headers);

        // ENVIAMOS A RELATIONAL (POSTGRES)
        try {
            String urlSql = "http://localhost:8091/api/v1/relational/registro";
            rest.postForObject(urlSql, request, String.class);
        } catch (Exception e) {
            System.out.println("ERRORRR AL ENVIAR A POSTGRES: " + e.getMessage());
        }

        // A NON-RELATIONAL (MONGO)
        try {
            String urlMongo = "http://localhost:8093/api/v1/nonrelational/registro";
            rest.postForObject(urlMongo, request, String.class);
        } catch (Exception e) {
            System.out.println("ERRORRR AL ENVIAR A MONGO: " + e.getMessage());
        }
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