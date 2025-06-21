-- Script de configuração do banco de dados
-- Sistema de Gestão de Cursos e Alunos

-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS gerenciadorcurso;
USE gerenciadorcurso;

-- Criar tabela de Cursos
CREATE TABLE IF NOT EXISTS Curso (
    ID_Curso INT AUTO_INCREMENT PRIMARY KEY,
    Nome_Curso VARCHAR(50) NOT NULL,
    Carga_Horaria INT NOT NULL,
    Limite_Alunos INT NOT NULL,
    Ativo_Curso BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_carga_horaria CHECK (Carga_Horaria >= 20 AND Carga_Horaria <= 1000),
    CONSTRAINT chk_limite_alunos CHECK (Limite_Alunos >= 1 AND Limite_Alunos <= 500)
);

-- Criar tabela de Alunos
CREATE TABLE IF NOT EXISTS Aluno (
    ID_Aluno INT AUTO_INCREMENT PRIMARY KEY,
    CPF_Aluno VARCHAR(14) NOT NULL UNIQUE,
    Nome_Aluno VARCHAR(100) NOT NULL,
    Email_Aluno VARCHAR(100) NOT NULL,
    Data_Nasc DATE NOT NULL,
    Ativo_Aluno BOOLEAN DEFAULT TRUE,
    ID_Curso INT,
    FOREIGN KEY (ID_Curso) REFERENCES Curso(ID_Curso) ON DELETE SET NULL,
    CONSTRAINT chk_idade CHECK (Data_Nasc <= DATE_SUB(CURDATE(), INTERVAL 16 YEAR)),
    CONSTRAINT chk_email CHECK (Email_Aluno REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Inserir alguns cursos de exemplo
INSERT INTO Curso (Nome_Curso, Carga_Horaria, Limite_Alunos, Ativo_Curso) VALUES
('Programação Java', 80, 30, TRUE),
('Desenvolvimento Web', 60, 25, TRUE),
('Banco de Dados', 40, 20, TRUE),
('Análise de Sistemas', 100, 35, TRUE),
('Mobile Development', 70, 28, TRUE);

-- Inserir alguns alunos de exemplo
INSERT INTO Aluno (CPF_Aluno, Nome_Aluno, Email_Aluno, Data_Nasc, Ativo_Aluno, ID_Curso) VALUES
('123.456.789-01', 'João Silva', 'joao.silva@email.com', '1990-05-15', TRUE, 1),
('987.654.321-00', 'Maria Santos', 'maria.santos@email.com', '1988-12-03', TRUE, 2),
('456.789.123-45', 'Pedro Oliveira', 'pedro.oliveira@email.com', '1995-08-22', TRUE, 1),
('789.123.456-78', 'Ana Costa', 'ana.costa@email.com', '1992-03-10', FALSE, 3),
('321.654.987-32', 'Carlos Ferreira', 'carlos.ferreira@email.com', '1987-11-28', TRUE, 4);

-- Criar índices para melhor performance
CREATE INDEX idx_aluno_cpf ON Aluno(CPF_Aluno);
CREATE INDEX idx_aluno_email ON Aluno(Email_Aluno);
CREATE INDEX idx_aluno_curso ON Aluno(ID_Curso);
CREATE INDEX idx_aluno_ativo ON Aluno(Ativo_Aluno);
CREATE INDEX idx_curso_ativo ON Curso(Ativo_Curso);

-- Verificar as tabelas criadas
SHOW TABLES;

-- Verificar estrutura das tabelas
DESCRIBE Curso;
DESCRIBE Aluno;

-- Verificar dados inseridos
SELECT 'Cursos cadastrados:' AS Info;
SELECT * FROM Curso;

SELECT 'Alunos cadastrados:' AS Info;
SELECT a.ID_Aluno, a.Nome_Aluno, a.CPF_Aluno, a.Email_Aluno, 
       a.Data_Nasc, a.Ativo_Aluno, c.Nome_Curso
FROM Aluno a
LEFT JOIN Curso c ON a.ID_Curso = c.ID_Curso
ORDER BY a.Nome_Aluno; 