package gui;

import dao.AlunoDAO;
import dao.CursoDAO;
import gui.util.StyledComponents;
import gui.util.ValidationUtil;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AlunoPainel extends JPanel {

    private JTextField txtNome, txtEmail, txtCPF;
    private JSpinner spnDataNascimento;
    private JCheckBox chkAtivo;
    private JComboBox<Curso> comboCurso;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private Aluno alunoSelecionado = null;

    public AlunoPainel() {
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

        // Titulo
        JLabel titleLabel = new JLabel("Cadastro de Alunos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyledComponents.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(titleLabel, BorderLayout.NORTH);

        // Form pra escrever
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);

        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(StyledComponents.createStyledLabel("Nome Completo:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = StyledComponents.createStyledTextField();
        fieldsPanel.add(txtNome, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = StyledComponents.createStyledTextField();
        fieldsPanel.add(txtEmail, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("CPF:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCPF = StyledComponents.createStyledTextField();
        txtCPF.setToolTipText("Formato: XXX.XXX.XXX-XX");
        fieldsPanel.add(txtCPF, gbc);

        // Data de Nascimento
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Data de Nascimento:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        spnDataNascimento = StyledComponents.createStyledSpinner();
        spnDataNascimento.setModel(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnDataNascimento, "dd/MM/yyyy");
        spnDataNascimento.setEditor(editor);
        fieldsPanel.add(spnDataNascimento, gbc);

        // Curso
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Curso:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboCurso = StyledComponents.createStyledComboBox();
        fieldsPanel.add(comboCurso, gbc);

        // Status 
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(StyledComponents.createStyledLabel("Status:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
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

        btnSalvar = StyledComponents.createSuccessButton(" Salvar");
        btnEditar = StyledComponents.createPrimaryButton("️ Editar");
        btnExcluir = StyledComponents.createDangerButton("️ Excluir");
        btnLimpar = StyledComponents.createPrimaryButton(" Limpar");
        
        // Add refresh button
        JButton btnRefresh = StyledComponents.createPrimaryButton("Atualizar");
        btnRefresh.addActionListener(e -> refreshData());

        btnSalvar.addActionListener(e -> salvarAluno());
        btnEditar.addActionListener(e -> editarAluno());
        btnExcluir.addActionListener(e -> excluirAluno());
        btnLimpar.addActionListener(e -> limparFormulario());

        // Initially disable edit and delete buttons
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

        // Title
        JLabel titleLabel = new JLabel("Alunos Cadastrados");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyledComponents.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] colunas = {"ID", "Nome", "CPF", "Email", "Curso", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
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
                    carregarAlunoSelecionado(selectedRow);
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

    // CRIAR O ALUNO E SALVAR (PODE COLOCAR O CHECK ATIVO OU NAO) 
    private void salvarAluno() {
        try {
            if (!validarFormulario()) {
                return;
            }

            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String cpf = txtCPF.getText().trim();
            
            Date nascimentoUtil = (Date) spnDataNascimento.getValue();
            LocalDate nascimento = nascimentoUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Curso cursoSelecionado = (Curso) comboCurso.getSelectedItem();
            if (cursoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um curso.", "Validação", JOptionPane.WARNING_MESSAGE);
                comboCurso.requestFocus();
                return;
            }

            Aluno aluno = new Aluno(cpf, nome, email, nascimento, chkAtivo.isSelected(), cursoSelecionado);
            new AlunoDAO().inserir(aluno);
            
            carregarAlunos();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // editar o aluninho 
            
    private void editarAluno() {
        try {
            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validarFormulario()) {
                return;
            }

            alunoSelecionado.setNome(txtNome.getText().trim());
            alunoSelecionado.setEmail(txtEmail.getText().trim());
            alunoSelecionado.setCpf(txtCPF.getText().trim());
            
            Date nascimentoUtil = (Date) spnDataNascimento.getValue();
            LocalDate nascimento = nascimentoUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            alunoSelecionado.setDataNascimento(nascimento);
            
            alunoSelecionado.setAtivo(chkAtivo.isSelected());
            alunoSelecionado.setCurso((Curso) comboCurso.getSelectedItem());

            new AlunoDAO().editar(alunoSelecionado);
            
            carregarAlunos();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //delete o aluninho 
    
    private void excluirAluno() {
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir o aluno '" + alunoSelecionado.getNome() + "'?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                new AlunoDAO().excluir(alunoSelecionado.getId());
                carregarAlunos();
                limparFormulario();
                JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //botaozinho pra fazer gracinha e limpar 
    
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtCPF.setText("");
        spnDataNascimento.setValue(new Date());
        chkAtivo.setSelected(true);
        comboCurso.setSelectedIndex(-1);
        alunoSelecionado = null;
        tabela.clearSelection();
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        txtNome.requestFocus();
    }

    private void limparSelecao() {
        alunoSelecionado = null;
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    private void carregarAlunoSelecionado(int row) {
        int id = (Integer) modeloTabela.getValueAt(row, 0);
        try {
            alunoSelecionado = new AlunoDAO().buscarPorId(id);
            if (alunoSelecionado != null) {
                txtNome.setText(alunoSelecionado.getNome());
                txtEmail.setText(alunoSelecionado.getEmail());
                txtCPF.setText(alunoSelecionado.getCpf());
                
                Date nascimento = Date.from(alunoSelecionado.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
                spnDataNascimento.setValue(nascimento);
                
                chkAtivo.setSelected(alunoSelecionado.isAtivo());
                comboCurso.setSelectedItem(alunoSelecionado.getCurso());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String cpf = txtCPF.getText().trim();
       
        
        
        // criterios da professora: minimo 3 caracteres o nome do aluno. 
        
        if (!ValidationUtil.isValidName(nome)) {
            JOptionPane.showMessageDialog(this, "Nome deve ter entre 3 e 100 caracteres.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }

        // formatação e validação do email
        
        if (!ValidationUtil.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        // Validação de CPF real
        
        if (!ValidationUtil.isValidCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido. Use o formato: XXX.XXX.XXX-XX", "Validação", JOptionPane.WARNING_MESSAGE);
            txtCPF.requestFocus();
            return false;
        }

        Date nascimentoUtil = (Date) spnDataNascimento.getValue();
        LocalDate nascimento = nascimentoUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        
        // CRITERIO DA PROFESSORA ALUNO > 16 ANOS
        
        if (!ValidationUtil.isValidAge(nascimento)) {
            JOptionPane.showMessageDialog(this, "O aluno deve ter entre 16 e 100 anos.", "Validação", JOptionPane.WARNING_MESSAGE);
            spnDataNascimento.requestFocus();
            return false;
        }

        if (comboCurso.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso.", "Validação", JOptionPane.WARNING_MESSAGE);
            comboCurso.requestFocus();
            return false;
        }

        return true;
    }

    private void carregarCursos() {
        try {
            List<Curso> cursos = new CursoDAO().listarTodos();
            comboCurso.removeAllItems();
            for (Curso curso : cursos) {
                if (curso.isAtivo()) {
                    comboCurso.addItem(curso);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // AQUI ELE VERIFICA SE É ATIVO ELE COLOCA NA TABELA DE ATIVO, SENAO COLOCA NO INATIVO
    
    private void carregarAlunos() {
        try {
            modeloTabela.setRowCount(0);
            List<Aluno> alunos = new AlunoDAO().listarTodos();
            
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // PRIMEIRO CARREGAMENTO DE DADOS QUANDO INICIALIZA
    
    @Override
    public void addNotify() {
        super.addNotify();
        carregarCursos();
        carregarAlunos();
    }
    
    // PRA ATUALIZAR OS DADOS DE UMA ABA PARA OUTRA
    public void refreshData() {
        carregarCursos();
        carregarAlunos();
    }
}
