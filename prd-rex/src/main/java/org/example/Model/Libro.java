package org.example.Model;

import java.time.LocalDate;

// CLASE PARA MAPEAR EL JSON QUE RECIBIMOS DEL MICROSERVICIO ANTERIOR
public class Libro {

    private String isbn;
    private String nome;
    private String autor;
    private LocalDate dataLectura;
    private LocalDate dataRexistro;

    // CONSTRUCTOR VACIO
    public Libro() {}

    // GETTERS Y SETTERS
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getDataLectura() {
        return dataLectura;
    }
    public void setDataLectura(LocalDate dataLectura) {
        this.dataLectura = dataLectura;
    }

    public LocalDate getDataRexistro() {
        return dataRexistro;
    }
    public void setDataRexistro(LocalDate dataRexistro) {
        this.dataRexistro = dataRexistro;
    }
}