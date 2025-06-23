package modelo;

import java.util.List;

public class Curso {
    private int id;
    private String nome;
    private int cargaHoraria;
    private int limiteAlunos;
    private boolean ativo;

    // Default constructor
    public Curso() {
        this.ativo = true;
    }
    
    // Constructor 
    public Curso(int id, String nome, int cargaHoraria, int limiteAlunos, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.limiteAlunos = limiteAlunos;
        this.ativo = ativo;
    }

    // Constructor sem ID 
    public Curso(String nome, int cargaHoraria, int limiteAlunos, boolean ativo) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.limiteAlunos = limiteAlunos;
        this.ativo = ativo;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getLimiteAlunos() {
        return limiteAlunos;
    }

    public void setLimiteAlunos(int limiteAlunos) {
        this.limiteAlunos = limiteAlunos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
