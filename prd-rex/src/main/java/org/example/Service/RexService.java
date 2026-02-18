package org.example.Service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.example.Model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class RexService {

    @Autowired
    private RestTemplate rest;

    // METODO QUE COORDINA TODAS LAS ACCIONES
    public void procesarLibro(Libro libro) {
        crearXml(libro);
        enviarAPostgres(libro);
        enviarAMongo(libro);
    }

    // METODO QUE ESCRIBE EL ARCHIVO XML LOCALMENTE (AHORA CON JACKSON)
    private void crearXml(Libro libro) {
        try {
            // HE DECIDIDO QUE EL NOMBRE DEL ARCHIVO SERÁ "REGISTRO" + SU ISB PARA MEJOR BÚSQUEDA
            String nombreArchivo = "registro_" + libro.getIsbn() + ".xml";
            File archivo = new File(nombreArchivo);


            // INICIALIZAMOS JACKSON
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(archivo, libro);

        } catch (Exception e) {
            System.out.println("ERROOORCH AL ESCRIBIR XML: " + e.getMessage());
        }
    }

    // ENVIAR AL MICROSERVICIO RELACIONAL (POSTGRES)
    private void enviarAPostgres(Libro libro) {
        try {
            String url = "http://localhost:8091/api/v1/relational/registro";

            // FORZAMOS EL ENVIO EN FORMATO JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Libro> request = new HttpEntity<>(libro, headers);

            rest.postForObject(url, request, String.class);
        } catch (Exception e) {
            // POR SI EL MICROSERVICIO AUN NO ESTA ENCENDIDO
            System.out.println("ERRORRR = MICROSERVICIO POSTGRES NO DISPONIBLE AÚN.");
        }
    }

    // ENVIAR AL MICROSERVICIO NO RELACIONAL (MONGO )
    private void enviarAMongo(Libro libro) {
        try {
            String url = "http://localhost:8093/api/v1/nonrelational/registro";

            // FORZAMOS EL ENVIO EN FORMATO JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Libro> request = new HttpEntity<>(libro, headers);

            rest.postForObject(url, request, String.class);
        } catch (Exception e) {
            System.out.println("EEERRROORCH = MICROSERVICIO MONGO NO DISPONIBLE AÚN.");
        }
    }


    // BUSCAR UN ARCHIVO XML POR SU ISBN
    public Optional<Libro> buscarPorIsbn(String isbn) {
        File archivo = new File("registro_" + isbn + ".xml");

        // SI EL ARCHIVO EXISTE, SE LEE
        if (archivo.exists()) {
            return Optional.ofNullable(leerXml(archivo));
        }

        // SI NO EXISTE DEVOLVEMOS UN OPTIONAL TOTALMENTE VACIO
        return Optional.empty();
    }

    // BUSCAR ARCHIVOS XML POR EL NOMBRE DEL LIBRO
    public List<Libro> buscarPorNome(String nome) {
        List<Libro> encontrados = new ArrayList<>();

        // APUNTA A LA CARPETA ACTUAL DEL PROYECTO
        File carpeta = new File(".");

        // SE OBTIENE TODOS LOS ARCHIVOS QUE EMPIECEN POR "registro_" Y ACABEN EN ".xml"
        File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("registro_") && name.endsWith(".xml"));

        if (archivos != null) {
            for (File archivo : archivos) {
                // LEEMOS CADA ARCHIVO PARA COMPROBAR SI EL NOMBRE COINCIDE
                Libro libro = leerXml(archivo);
                if (libro != null && libro.getNome().equalsIgnoreCase(nome)) {
                    encontrados.add(libro);
                }
            }
        }
        return encontrados;
    }

    // LEE Y TRANSFORMA UN XML A OBJETO LIBRO
    private Libro leerXml(File archivo) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(archivo, Libro.class);
        } catch (Exception e) {
            System.out.println("ERROR AL LEER EL ARCHIVO: " + archivo.getName());
            return null;
        }
    }
}