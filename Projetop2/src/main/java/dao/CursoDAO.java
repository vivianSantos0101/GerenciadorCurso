package dao;

import modelo.Curso;
import factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    // CRIAR UM CURSO NOVO 
    public void inserir(Curso curso) throws SQLException {
        String sql = "INSERT INTO Curso (Nome_Curso, Carga_Horaria, Limite_Alunos, Ativo_Curso) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCargaHoraria());
            stmt.setInt(3, curso.getLimiteAlunos());
            stmt.setBoolean(4, curso.isAtivo());
            stmt.executeUpdate();
        }
    }
    
    // EDITAR O CURSO, MUDAR AS INFO

    public void editar(Curso curso) throws SQLException {
        String sql = "UPDATE Curso SET Nome_Curso = ?, Carga_Horaria = ?, Limite_Alunos = ?, Ativo_Curso = ? WHERE ID_Curso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCargaHoraria());
            stmt.setInt(3, curso.getLimiteAlunos());
            stmt.setBoolean(4, curso.isAtivo());
            stmt.setInt(5, curso.getId());
            stmt.executeUpdate();
        }
    }

    // LISTAR  OS CURSOS CRIADOS
    
    public List<Curso> listarTodos() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM Curso ORDER BY Nome_Curso";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                lista.add(c);
            }
        }
        return lista;
    }

    // BUSCA POR ID DO CURSO
    
    public Curso buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Curso WHERE ID_Curso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                return c;
            }
        }
        return null;
    }

    // DELETE O CURSO 
    
    public void excluir(int idCurso) throws SQLException {
        String sql = "DELETE FROM Curso WHERE ID_Curso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            stmt.executeUpdate();
        }
    }

    // DEIXAR O CURSO INATIVO SEM EXCLUIR
    
    public void desabilitar(int idCurso) throws SQLException {
        String sql = "UPDATE Curso SET Ativo_Curso = FALSE WHERE ID_Curso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            stmt.executeUpdate();
        }
    }
    
    // DEIXAR O CURSO ATIVO 

    public void reativar(int idCurso) throws SQLException {
        String sql = "UPDATE Curso SET Ativo_Curso = TRUE WHERE ID_Curso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            stmt.executeUpdate();
        }
    }
    
    // LISTAR CURSOS POR STATUS (ATIVO/INATIVO)
    
    public List<Curso> listarPorStatus(boolean ativo) throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM Curso WHERE Ativo_Curso = ? ORDER BY Nome_Curso";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, ativo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Curso c = new Curso();
                c.setId(rs.getInt("ID_Curso"));
                c.setNome(rs.getString("Nome_Curso"));
                c.setCargaHoraria(rs.getInt("Carga_Horaria"));
                c.setLimiteAlunos(rs.getInt("Limite_Alunos"));
                c.setAtivo(rs.getBoolean("Ativo_Curso"));
                lista.add(c);
            }
        }
        return lista;
    }
}
