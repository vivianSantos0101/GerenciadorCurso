package modelo;

import java.time.LocalDate;

public class Aluno {
    private int id;
    private String cpf;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private boolean ativo;
    private Curso curso;

    // 
    public Aluno() {
        this.ativo = true;
    }
    
    // Constructor
    public Aluno(int id, String cpf, String nome, String email, LocalDate dataNascimento, boolean ativo, Curso curso) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
        this.curso = curso;
    }

    // Constructor sem ID 
    public Aluno(String cpf, String nome, String email, LocalDate dataNascimento, boolean ativo, Curso curso) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
        this.curso = curso;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return nome + " - " + cpf;
    }
}

