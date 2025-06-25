package dao;

import modelo.Aluno;
import modelo.Curso;
import factory.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // CREATE
    
    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO Aluno (CPF_Aluno, Nome_Aluno, Email_Aluno, Data_Nasc, Ativo_Aluno, ID_Curso) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.setDate(4, Date.valueOf(aluno.getDataNascimento()));
            stmt.setBoolean(5, aluno.isAtivo());
            stmt.setInt(6, aluno.getCurso().getId());
            stmt.executeUpdate();
        }
    }
    
    // UPDATE

    public void editar(Aluno aluno) throws SQLException {
        String sql = "UPDATE Aluno SET CPF_Aluno = ?, Nome_Aluno = ?, Email_Aluno = ?, Data_Nasc = ?, Ativo_Aluno = ?, ID_Curso = ? WHERE ID_Aluno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.setDate(4, Date.valueOf(aluno.getDataNascimento()));
            stmt.setBoolean(5, aluno.isAtivo());
            stmt.setInt(6, aluno.getCurso().getId());
            stmt.setInt(7, aluno.getId());
            stmt.executeUpdate();
        }
    }

    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.Nome_Curso, c.Carga_Horaria, c.Limite_Alunos, c.Ativo_Curso FROM Aluno a " +
                     "LEFT JOIN Curso c ON a.ID_Curso = c.ID_Curso ORDER BY a.Nome_Aluno";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Aluno a = new Aluno();
                a.setId(rs.getInt("ID_Aluno"));
                a.setCpf(rs.getString("CPF_Aluno"));
                a.setNome(rs.getString("Nome_Aluno"));
                a.setEmail(rs.getString("Email_Aluno"));
                a.setDataNascimento(rs.getDate("Data_Nasc").toLocalDate());
                a.setAtivo(rs.getBoolean("Ativo_Aluno"));
                
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                a.setCurso(c);
                
                lista.add(a);
            }
        }
        return lista;
    }

    public Aluno buscarPorId(int id) throws SQLException {
        String sql = "SELECT a.*, c.Nome_Curso, c.Carga_Horaria, c.Limite_Alunos, c.Ativo_Curso FROM Aluno a " +
                     "LEFT JOIN Curso c ON a.ID_Curso = c.ID_Curso WHERE a.ID_Aluno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Aluno a = new Aluno();
                a.setId(rs.getInt("ID_Aluno"));
                a.setCpf(rs.getString("CPF_Aluno"));
                a.setNome(rs.getString("Nome_Aluno"));
                a.setEmail(rs.getString("Email_Aluno"));
                a.setDataNascimento(rs.getDate("Data_Nasc").toLocalDate());
                a.setAtivo(rs.getBoolean("Ativo_Aluno"));
                
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                a.setCurso(c);
                
                return a;
            }
        }
        return null;
    }

    // DELETE 
    
    public void excluir(int idAluno) throws SQLException {
        String sql = "DELETE FROM Aluno WHERE ID_Aluno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.executeUpdate();
        }
    }

    // status inativo
    public void desabilitar(int idAluno) throws SQLException {
        String sql = "UPDATE Aluno SET Ativo_Aluno = FALSE WHERE ID_Aluno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.executeUpdate();
        }
    }

    
     // status ativo
    
    public void reativar(int idAluno) throws SQLException {
        String sql = "UPDATE Aluno SET Ativo_Aluno = TRUE WHERE ID_Aluno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.executeUpdate();
        }
    }

    //LISTA POR STATUS (TODOS OS ATIVOS E INATIVOS)
    
    public List<Aluno> listarPorStatus(boolean ativos) throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.Nome_Curso, c.Carga_Horaria, c.Limite_Alunos, c.Ativo_Curso FROM Aluno a " +
                     "LEFT JOIN Curso c ON a.ID_Curso = c.ID_Curso WHERE a.Ativo_Aluno = ? ORDER BY a.Nome_Aluno";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, ativos);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Aluno a = new Aluno();
                a.setId(rs.getInt("ID_Aluno"));
                a.setCpf(rs.getString("CPF_Aluno"));
                a.setNome(rs.getString("Nome_Aluno"));
                a.setEmail(rs.getString("Email_Aluno"));
                a.setDataNascimento(rs.getDate("Data_Nasc").toLocalDate());
                a.setAtivo(rs.getBoolean("Ativo_Aluno"));
                
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                a.setCurso(c);
                
                lista.add(a);
            }
        }
        return lista;
    }
}
