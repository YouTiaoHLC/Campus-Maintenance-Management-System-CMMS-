package main.java.cmms.ui;

import main.java.cmms.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModificationFrame extends JFrame {
    private MainFrame parent;

    public ModificationFrame(MainFrame parent) {
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Database Modification");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(Constants.CHILD_WIDTH, Constants.CHILD_HEIGHT);
        setLocationRelativeTo(parent);

        // 添加窗口关闭监听
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                returnToMain();
            }
        });

        createMainPanel();
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Constants.SECONDARY_COLOR);

        // 标题和返回按钮
        JPanel headerPanel = createHeaderPanel("Database Modification");

        // 功能按钮面板
        JPanel functionPanel = createFunctionPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(functionPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(Constants.HEADER_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);

        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> returnToMain());

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton insertBtn = createFunctionButton("Insert Data");
        JButton updateBtn = createFunctionButton("Update Data");
        JButton deleteBtn = createFunctionButton("Delete Data");
        JButton setBasedBtn = createFunctionButton("Set-Based Insert");

        // 这里添加具体的功能监听器 - 由负责此模块的组员实现
        insertBtn.addActionListener(e -> showNotImplemented("Insert Data"));
        updateBtn.addActionListener(e -> showNotImplemented("Update Data"));
        deleteBtn.addActionListener(e -> showNotImplemented("Delete Data"));
        setBasedBtn.addActionListener(e -> showNotImplemented("Set-Based Insert"));

        panel.add(insertBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(setBasedBtn);

        return panel;
    }

    private JButton createFunctionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        return button;
    }

    private void showNotImplemented(String feature) {
        JOptionPane.showMessageDialog(this,
                feature + " feature is not implemented yet.\nThis will be developed by team members.",
                "Feature Not Implemented",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void returnToMain() {
        parent.returnToMain();
        this.dispose();
    }
}
