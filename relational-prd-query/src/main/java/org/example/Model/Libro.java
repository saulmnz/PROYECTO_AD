package org.example.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "autor", length = 100)
    private String autor;

    @Column(name = "data_lectura")
    private LocalDate dataLectura;

    @Column(name = "data_rexistro")
    private LocalDate dataRexistro;

    public Libro() {}

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