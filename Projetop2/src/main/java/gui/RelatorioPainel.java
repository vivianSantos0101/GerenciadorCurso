package gui;

import dao.AlunoDAO;
import dao.CursoDAO;
import gui.util.StyledComponents;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioPainel extends JPanel {

    private JTable tabelaRelatorio;
    private DefaultTableModel modeloTabela;
    private JTextArea areaEstatisticas;
    private JComboBox<String> comboFiltro;

    public RelatorioPainel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyledComponents.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = StyledComponents.createCardPanel();
        controlPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Relatórios e Estatísticas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyledComponents.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        controlPanel.add(titleLabel, BorderLayout.NORTH);

        // Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlsPanel.setOpaque(false);

        // Filter combo
        controlsPanel.add(StyledComponents.createStyledLabel("Filtrar por:"));
        comboFiltro = StyledComponents.createStyledComboBox();
        comboFiltro.addItem("Todos os Alunos");
        comboFiltro.addItem("Alunos Ativos");
        comboFiltro.addItem("Alunos Inativos");
        comboFiltro.addItem("Por Curso");
        controlsPanel.add(comboFiltro);

        // Buttons
        JButton btnGerar = StyledComponents.createPrimaryButton("Gerar Relatório");
        JButton btnExportar = StyledComponents.createSuccessButton("📄 Exportar TXT");
        JButton btnAtualizar = StyledComponents.createPrimaryButton("🔄 Atualizar");

        btnGerar.addActionListener(e -> gerarRelatorio());
        btnExportar.addActionListener(e -> exportarTXT());
        btnAtualizar.addActionListener(e -> atualizarDados());

        controlsPanel.add(btnGerar);
        controlsPanel.add(btnExportar);
        controlsPanel.add(btnAtualizar);

        controlPanel.add(controlsPanel, BorderLayout.CENTER);

        return controlPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);

        // Statistics panel
        JPanel statsPanel = StyledComponents.createCardPanel();
        statsPanel.setLayout(new BorderLayout());

        JLabel statsTitle = new JLabel("Estatísticas Gerais");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsTitle.setForeground(StyledComponents.PRIMARY_COLOR);
        statsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        statsPanel.add(statsTitle, BorderLayout.NORTH);

        areaEstatisticas = new JTextArea();
        areaEstatisticas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaEstatisticas.setEditable(false);
        areaEstatisticas.setLineWrap(true);
        areaEstatisticas.setWrapStyleWord(true);
        areaEstatisticas.setBackground(StyledComponents.BACKGROUND_COLOR);
        areaEstatisticas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane statsScroll = StyledComponents.createStyledScrollPane(areaEstatisticas);
        statsPanel.add(statsScroll, BorderLayout.CENTER);

        // Report table panel
        JPanel tablePanel = StyledComponents.createCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JLabel tableTitle = new JLabel("Dados do Relatório");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(StyledComponents.PRIMARY_COLOR);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Table
        String[] colunas = {"ID", "Nome", "CPF", "Email", "Curso", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaRelatorio = StyledComponents.createStyledTable();
        tabelaRelatorio.setModel(modeloTabela);
        tabelaRelatorio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = StyledComponents.createStyledScrollPane(tabelaRelatorio);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        contentPanel.add(statsPanel);
        contentPanel.add(tablePanel);

        return contentPanel;
    }

    private void gerarRelatorio() {
        try {
            String filtro = (String) comboFiltro.getSelectedItem();
            List<Aluno> alunos = null;

            switch (filtro) {
                case "Todos os Alunos":
                    alunos = new AlunoDAO().listarTodos();
                    break;
                case "Alunos Ativos":
                    alunos = new AlunoDAO().listarPorStatus(true);
                    break;
                case "Alunos Inativos":
                    alunos = new AlunoDAO().listarPorStatus(false);
                    break;
                case "Por Curso":
                   
                    alunos = new AlunoDAO().listarTodos();
                    break;
            }

            if (alunos != null) {
                carregarTabelaRelatorio(alunos);
                gerarEstatisticas(alunos);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabelaRelatorio(List<Aluno> alunos) {
        modeloTabela.setRowCount(0);
        
        for (Aluno aluno : alunos) {
            String status = aluno.isAtivo() ? " Ativo" : " Inativo";
            String cursoNome = aluno.getCurso() != null ? aluno.getCurso().getNome() : "N/A";
            
            modeloTabela.addRow(new Object[]{
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEmail(),
                cursoNome,
                status
            });
        }
    }

    private void gerarEstatisticas(List<Aluno> alunos) {
        StringBuilder stats = new StringBuilder();
        stats.append(" ESTATÍSTICAS DO RELATÓRIO\n");
        stats.append("\n\n");
        
        stats.append(" Total de Alunos: ").append(alunos.size()).append("\n");
        
        long ativos = alunos.stream().filter(Aluno::isAtivo).count();
        long inativos = alunos.size() - ativos;
        
        stats.append(" Alunos Ativos: ").append(ativos).append("\n");
        stats.append(" Alunos Inativos: ").append(inativos).append("\n");
        
        if (alunos.size() > 0) {
            double percentualAtivos = (double) ativos / alunos.size() * 100;
            stats.append(" Percentual Ativos: ").append(String.format("%.1f%%", percentualAtivos)).append("\n\n");
        }

        // Statistics by course
        stats.append(" DISTRIBUIÇÃO POR CURSO:\n");
        stats.append("\n");
        
        try {
            List<Curso> cursos = new CursoDAO().listarTodos();
            for (Curso curso : cursos) {
                long alunosNoCurso = alunos.stream()
                    .filter(a -> a.getCurso() != null && a.getCurso().getId() == curso.getId())
                    .count();
                
                if (alunosNoCurso > 0) {
                    stats.append("• ").append(curso.getNome()).append(": ").append(alunosNoCurso).append(" alunos\n");
                }
            }
        } catch (Exception e) {
            stats.append("Erro ao carregar dados dos cursos\n");
        }

        stats.append("\n Relatório gerado em: ");
        stats.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        areaEstatisticas.setText(stats.toString());
    }

    private void exportarTXT() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório TXT");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos TXT", "txt"));
            
            String defaultFileName = "relatorio_alunos_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            fileChooser.setSelectedFile(new java.io.File(defaultFileName));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    // Header
                    writer.println("=".repeat(80));
                    writer.println("                    RELATÓRIO DE ALUNOS");
                    writer.println("=".repeat(80));
                    writer.println();
                    
                    // Statistics section
                    writer.println("ESTATÍSTICAS DO RELATÓRIO");
                    writer.println("-".repeat(40));
                    writer.println();
                    
                    // Get statistics from the text area
                    String estatisticas = areaEstatisticas.getText();
                    writer.println(estatisticas);
                    writer.println();
                    
                    // Data section
                    writer.println("DADOS DOS ALUNOS");
                    writer.println("-".repeat(40));
                    writer.println();
                    
                    // Table header
                    writer.printf("%-5s %-30s %-15s %-25s %-20s %-10s%n", 
                        "ID", "Nome", "CPF", "Email", "Curso", "Status");
                    writer.println("-".repeat(120));
                    
                    // Data rows
                    for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                        Object id = modeloTabela.getValueAt(i, 0);
                        Object nome = modeloTabela.getValueAt(i, 1);
                        Object cpf = modeloTabela.getValueAt(i, 2);
                        Object email = modeloTabela.getValueAt(i, 3);
                        Object curso = modeloTabela.getValueAt(i, 4);
                        Object status = modeloTabela.getValueAt(i, 5);
                        
                        writer.printf("%-5s %-30s %-15s %-25s %-20s %-10s%n",
                            id != null ? id.toString() : "",
                            nome != null ? nome.toString() : "",
                            cpf != null ? cpf.toString() : "",
                            email != null ? email.toString() : "",
                            curso != null ? curso.toString() : "",
                            status != null ? status.toString() : ""
                        );
                    }
                    
                    writer.println();
                    writer.println("=".repeat(80));
                    writer.println("Relatório gerado em: " + 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                    writer.println("=".repeat(80));
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Relatório exportado com sucesso para:\n" + file.getAbsolutePath(), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDados() {
        gerarRelatorio();
    }

    
    @Override
    public void addNotify() {
        super.addNotify();
        gerarRelatorio();
    }
    
  
    public void refreshData() {
        atualizarDados();
    }
}
