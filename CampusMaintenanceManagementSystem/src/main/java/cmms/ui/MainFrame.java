package main.java.cmms.ui;

import main.java.cmms.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel mainPanel;

    public MainFrame() {
        initializeUI();
        setupDatabaseConnection();
    }

    private void initializeUI() {
        setTitle("Campus Maintenance and Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.MAIN_WIDTH, Constants.MAIN_HEIGHT);
        setLocationRelativeTo(null); // 居中显示

        createMainPanel();
        setContentPane(mainPanel);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Constants.SECONDARY_COLOR);

        // 标题
        JLabel titleLabel = new JLabel("Campus Maintenance and Management System", JLabel.CENTER);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        // 按钮面板
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

        // 创建按钮
        JButton modificationBtn = createStyledButton("Modification of Database");
        JButton sqlQueryBtn = createStyledButton("SQL Queries");
        JButton activityInfoBtn = createStyledButton("Activity Information");
        JButton otherSearchesBtn = createStyledButton("Other Searches");
        JButton reportBtn = createStyledButton("Report Generation");

        // 添加按钮监听器
        modificationBtn.addActionListener(e -> openModificationFrame());
        sqlQueryBtn.addActionListener(e -> openSQLQueryFrame());
        activityInfoBtn.addActionListener(e -> openActivityInfoFrame());
        otherSearchesBtn.addActionListener(e -> openOtherSearchesFrame());
        reportBtn.addActionListener(e -> openReportFrame());


        panel.add(modificationBtn);
        panel.add(sqlQueryBtn);
        panel.add(activityInfoBtn);
        panel.add(otherSearchesBtn);
        panel.add(reportBtn);

        // 添加一个空标签来保持布局平衡
        panel.add(new JLabel());

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void setupDatabaseConnection() {
        // 测试数据库连接
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return main.java.cmms.database.DatabaseConnection.testConnection();
            }

            @Override
            protected void done() {
                try {
                    boolean connected = get();
                    if (!connected) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Unable to connect to the database. Please check the database configuration.", //无法连接到数据库，请检查数据库配置
                                "Connection error", //连接错误
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    // 打开各个功能界面的方法 - 这些是框架方法，具体实现由组员完成
    private void openModificationFrame() {
        ModificationFrame frame = new ModificationFrame(this);
        frame.setVisible(true);
        this.setVisible(false); // 隐藏主界面
    }

    private void openSQLQueryFrame() {
        SQLQueryFrame frame = new SQLQueryFrame(this);
        frame.setVisible(true);
        this.setVisible(false);
    }

    private void openActivityInfoFrame() {
        ActivityInfoFrame frame = new ActivityInfoFrame(this);
        frame.setVisible(true);
        this.setVisible(false);
    }

    private void openOtherSearchesFrame() {
        OtherSearchesFrame frame = new OtherSearchesFrame(this);
        frame.setVisible(true);
        this.setVisible(false);
    }

    private void openReportFrame() {
        ReportFrame frame = new ReportFrame(this);
        frame.setVisible(true);
        this.setVisible(false);
    }

    // 供子界面调用的返回主界面的方法
    public void returnToMain() {
        this.setVisible(true);
    }
}