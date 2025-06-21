package gui;

import com.formdev.flatlaf.FlatLightLaf;
import gui.util.StyledComponents;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setupFrame();
        setupUI();
    }

    private void setupFrame() {
        setTitle("Sistema de Gestão de Cursos e Alunos");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        
        // ICONE DO PROJETO
        try {
            java.net.URL resourceUrl = getClass().getClassLoader().getResource("assets/logo2.png");
            if (resourceUrl != null) {
                ImageIcon icon = new ImageIcon(resourceUrl);
                setIconImage(icon.getImage());
                System.out.println("Application icon loaded successfully from: " + resourceUrl);
            } else {
                System.err.println("Could not find logo2.png in assets directory");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone da aplicação: " + e.getMessage());
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(StyledComponents.BACKGROUND_COLOR);

        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Main content with tabs
        JTabbedPane tabbedPane = createTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyledComponents.PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        JLabel titleLabel = new JLabel("Sistema de Gestão de Cursos e Alunos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        // Logo or additional info
        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        versionLabel.setForeground(Color.WHITE);
        header.add(versionLabel, BorderLayout.EAST);

        return header;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(StyledComponents.BACKGROUND_COLOR);
        tabbedPane.setForeground(StyledComponents.TEXT_COLOR);

        // Create panels
        CursoPainel cursoPanel = new CursoPainel();
        AlunoPainel alunoPanel = new AlunoPainel();
        RelatorioPainel relatorioPanel = new RelatorioPainel();

        // Add tabs
        tabbedPane.addTab(" Cursos", cursoPanel);
        tabbedPane.addTab(" Alunos", alunoPanel);
        tabbedPane.addTab("Relatórios", relatorioPanel);

        // Add change listener to refresh data when switching tabs
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 0: // Cursos tab
                    cursoPanel.refreshData();
                    break;
                case 1: // Alunos tab
                    alunoPanel.refreshData();
                    break;
                case 2: // Relatórios tab
                    relatorioPanel.refreshData();
                    break;
            }
        });

        return tabbedPane;
    }

    private JLabel createTabIcon(String emoji) {
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return icon;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(52, 73, 94));
        statusBar.setPreferredSize(new Dimension(0, 30));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel statusLabel = new JLabel("Sistema pronto");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JLabel timeLabel = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);
        statusBar.add(timeLabel, BorderLayout.EAST);

        return statusBar;
    }

    public static void main(String[] args) {
        try {
            // Apply modern look and feel
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Set default fonts
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
            
        } catch (Exception e) {
            System.err.println("Erro ao aplicar o tema FlatLaf: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Erro ao aplicar o tema do sistema: " + ex.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal app = new TelaPrincipal();
            app.setVisible(true);
        });
    }
}
