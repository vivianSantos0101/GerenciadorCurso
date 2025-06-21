package gui;

import dao.CursoDAO;
import gui.util.StyledComponents;
import gui.util.ValidationUtil;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CursoPainel extends JPanel {

    private JTextField txtNome;
    private JSpinner spnCarga;
    private JSpinner spnLimite;
    private JCheckBox chkAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private Curso cursoSelecionado = null;

    public CursoPainel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyledComponents.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = StyledComponents.createCardPanel();
        formPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Cadastro de Cursos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyledComponents.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(titleLabel, BorderLayout.NORTH);

        // Form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);

        // Nome do Curso
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(StyledComponents.createStyledLabel("Nome do Curso:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = StyledComponents.createStyledTextField();
        fieldsPanel.add(txtNome, gbc);

        // Carga Horária
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Carga Horária (horas):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        spnCarga = StyledComponents.createStyledSpinner();
        spnCarga.setModel(new SpinnerNumberModel(40, 20, 1000, 10));
        fieldsPanel.add(spnCarga, gbc);

        // Limite de Alunos
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Limite de Alunos:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        spnLimite = StyledComponents.createStyledSpinner();
        spnLimite.setModel(new SpinnerNumberModel(30, 1, 500, 1));
        fieldsPanel.add(spnLimite, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Status:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        chkAtivo = StyledComponents.createStyledCheckBox("Ativo");
        chkAtivo.setSelected(true);
        fieldsPanel.add(chkAtivo, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonsPanel.setOpaque(false);

        btnSalvar = StyledComponents.createSuccessButton("Salvar");
        btnEditar = StyledComponents.createPrimaryButton(" Editar");
        btnExcluir = StyledComponents.createDangerButton(" Excluir");
        btnLimpar = StyledComponents.createPrimaryButton(" Limpar");
        
        // Add refresh button
        JButton btnRefresh = StyledComponents.createPrimaryButton(" Atualizar");
        btnRefresh.addActionListener(e -> refreshData());

        btnSalvar.addActionListener(e -> salvarCurso());
        btnEditar.addActionListener(e -> editarCurso());
        btnExcluir.addActionListener(e -> excluirCurso());
        btnLimpar.addActionListener(e -> limparFormulario());

        // botoes de excluir e editar 
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        buttonsPanel.add(btnSalvar);
        buttonsPanel.add(btnEditar);
        buttonsPanel.add(btnExcluir);
        buttonsPanel.add(btnLimpar);
        buttonsPanel.add(btnRefresh);

        return buttonsPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = StyledComponents.createCardPanel();
        tablePanel.setLayout(new BorderLayout());

        // Titulo do curso
        JLabel titleLabel = new JLabel("Cursos Cadastrados");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyledComponents.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(titleLabel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Carga Horária", "Limite", "Status", "Ações"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        tabela = StyledComponents.createStyledTable();
        tabela.setModel(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabela.getSelectedRow();
                if (selectedRow >= 0) {
                    carregarCursoSelecionado(selectedRow);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                    btnSalvar.setEnabled(false);
                } else {
                    limparSelecao();
                }
            }
        });

        JScrollPane scrollPane = StyledComponents.createStyledScrollPane(tabela);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void salvarCurso() {
        try {
            if (!validarFormulario()) {
                return;
            }

            String nome = txtNome.getText().trim();
            int carga = (Integer) spnCarga.getValue();
            int limite = (Integer) spnLimite.getValue();
            boolean ativo = chkAtivo.isSelected();

            Curso curso = new Curso(nome, carga, limite, ativo);
            new CursoDAO().inserir(curso);
            
            carregarCursos();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCurso() {
        try {
            if (cursoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um curso para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validarFormulario()) {
                return;
            }

            cursoSelecionado.setNome(txtNome.getText().trim());
            cursoSelecionado.setCargaHoraria((Integer) spnCarga.getValue());
            cursoSelecionado.setLimiteAlunos((Integer) spnLimite.getValue());
            cursoSelecionado.setAtivo(chkAtivo.isSelected());

            new CursoDAO().editar(cursoSelecionado);
            
            carregarCursos();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Curso atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCurso() {
        if (cursoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir o curso '" + cursoSelecionado.getNome() + "'?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                new CursoDAO().excluir(cursoSelecionado.getId());
                carregarCursos();
                limparFormulario();
                JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        spnCarga.setValue(40);
        spnLimite.setValue(30);
        chkAtivo.setSelected(true);
        cursoSelecionado = null;
        tabela.clearSelection();
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        txtNome.requestFocus();
    }

    private void limparSelecao() {
        cursoSelecionado = null;
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    private void carregarCursoSelecionado(int row) {
        int id = (Integer) modeloTabela.getValueAt(row, 0);
        try {
            cursoSelecionado = new CursoDAO().buscarPorId(id);
            if (cursoSelecionado != null) {
                txtNome.setText(cursoSelecionado.getNome());
                spnCarga.setValue(cursoSelecionado.getCargaHoraria());
                spnLimite.setValue(cursoSelecionado.getLimiteAlunos());
                chkAtivo.setSelected(cursoSelecionado.isAtivo());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
        String nome = txtNome.getText().trim();
        
        if (!ValidationUtil.isValidCourseName(nome)) {
            JOptionPane.showMessageDialog(this, "Nome do curso deve ter entre 3 e 50 caracteres.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }

        int carga = (Integer) spnCarga.getValue();
        if (!ValidationUtil.isValidWorkload(carga)) {
            JOptionPane.showMessageDialog(this, "Carga horária deve estar entre 20 e 1000 horas.", "Validação", JOptionPane.WARNING_MESSAGE);
            spnCarga.requestFocus();
            return false;
        }

        int limite = (Integer) spnLimite.getValue();
        if (!ValidationUtil.isValidStudentLimit(limite)) {
            JOptionPane.showMessageDialog(this, "Limite de alunos deve estar entre 1 e 500.", "Validação", JOptionPane.WARNING_MESSAGE);
            spnLimite.requestFocus();
            return false;
        }

        return true;
    }

    private void carregarCursos() {
        try {
            modeloTabela.setRowCount(0);
            List<Curso> cursos = new CursoDAO().listarTodos();
            
            for (Curso curso : cursos) {
                String status = curso.isAtivo() ? "✅ Ativo" : "❌ Inativo";
                modeloTabela.addRow(new Object[]{
                    curso.getId(),
                    curso.getNome(),
                    curso.getCargaHoraria() + "h",
                    curso.getLimiteAlunos(),
                    status
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load courses when panel is first shown
    @Override
    public void addNotify() {
        super.addNotify();
        carregarCursos();
    }
    
    //METODO DE ATUALIZAR OS DADOS DE UMA ABA PARA OUTRA
    public void refreshData() {
        carregarCursos();
    }
}
