package main.java.cmms.ui;

import main.java.cmms.util.Constants;
import main.java.cmms.database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ReportFrame extends JFrame {
    private MainFrame parent;
    private JComboBox<String> reportTypeComboBox;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JTextArea summaryTextArea;

    public ReportFrame(MainFrame parent) {
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Report Generation");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(parent);

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
        JPanel headerPanel = createHeaderPanel("Report Generation");

        // 报表配置面板
        JPanel configPanel = createConfigPanel();

        // 按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 结果展示区域
        JSplitPane resultSplitPane = createResultSplitPane();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, resultSplitPane);
        mainSplitPane.setResizeWeight(0.3);

        setContentPane(mainSplitPane);
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

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Configuration"));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 报表类型选择
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Report Type:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        reportTypeComboBox = new JComboBox<>(new String[]{
                "Activity Analysis by Type",
                "Worker Activity Count",
                "Location Activity Summary",
                "Chemical Usage Report",
                "Emergency Plan Usage",
                "External Company Performance",
                "Management Efficiency Report"
        });
        panel.add(reportTypeComboBox, gbc);

        // 开始日期
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Date:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        startDateField = createFormattedDateField("2025-11-01");
        panel.add(startDateField, gbc);

        // 结束日期
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("End Date:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        endDateField = createFormattedDateField("2025-11-30");
        panel.add(endDateField, gbc);

        return panel;
    }

    private JFormattedTextField createFormattedDateField(String defaultValue) {
        try {
            javax.swing.text.MaskFormatter dateFormatter = new javax.swing.text.MaskFormatter("####-##-##");
            dateFormatter.setPlaceholderCharacter('_');
            dateFormatter.setValidCharacters("0123456789");

            JFormattedTextField field = new JFormattedTextField(dateFormatter);
            field.setValue(defaultValue);
            field.setColumns(10);
            field.setHorizontalAlignment(JTextField.CENTER);

            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    SwingUtilities.invokeLater(() -> {
                        field.selectAll();
                    });
                }
            });

            return field;
        } catch (Exception e) {
            e.printStackTrace();
            JFormattedTextField field = new JFormattedTextField();
            field.setValue(defaultValue);
            field.setColumns(10);
            return field;
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton generateBtn = createStyledButton("Generate Report");
        JButton exportBtn = createStyledButton("Export to CSV");
        JButton clearBtn = createStyledButton("Clear Results");

        generateBtn.addActionListener(e -> generateReport());
        exportBtn.addActionListener(e -> exportReport());
        clearBtn.addActionListener(e -> clearResults());

        panel.add(generateBtn);
        panel.add(exportBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JSplitPane createResultSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Report Details"));

        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // 摘要面板
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Report Summary"));

        summaryTextArea = new JTextArea(8, 50);
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);

        // 状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel = new JLabel("Ready to generate reports");
        statusLabel.setFont(Constants.NORMAL_FONT);
        statusPanel.add(statusLabel);

        summaryPanel.add(summaryScrollPane, BorderLayout.CENTER);
        summaryPanel.add(statusPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(tablePanel);
        splitPane.setBottomComponent(summaryPanel);

        return splitPane;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

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

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String startDateStr = startDateField.getText();
        String endDateStr = endDateField.getText();

        // 验证日期格式
        if (!isValidDate(startDateStr) || !isValidDate(endDateStr)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter dates in YYYY-MM-DD format",
                    "Invalid Date Format",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 在后台线程中生成报表
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String summary;
            private String errorMessage;

            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    statusLabel.setText("Generating " + reportType + "...");

                    // 根据报表类型构建查询
                    String sql = buildReportQuery(reportType, startDateStr, endDateStr);

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // 设置日期参数
                        pstmt.setString(1, startDateStr);
                        pstmt.setString(2, endDateStr);

                        try (ResultSet rs = pstmt.executeQuery()) {
                            // 处理结果集
                            ResultSetMetaData metaData = rs.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            // 创建列名
                            columnNames = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                columnNames.add(metaData.getColumnName(i));
                            }

                            // 创建数据
                            data = new Vector<>();
                            while (rs.next()) {
                                Vector<Object> row = new Vector<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.add(rs.getObject(i));
                                }
                                data.add(row);
                            }
                        }
                    }

                    // 生成摘要
                    summary = generateReportSummary(reportType, data, startDateStr, endDateStr);

                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (errorMessage != null) {
                        statusLabel.setText("Error: " + errorMessage);
                        JOptionPane.showMessageDialog(ReportFrame.this,
                                "Database Error: " + errorMessage,
                                "Report Generation Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // 更新表格
                        tableModel.setDataVector(data, columnNames);

                        // 调整列宽
                        for (int i = 0; i < resultTable.getColumnCount(); i++) {
                            resultTable.getColumnModel().getColumn(i).setPreferredWidth(150);
                        }

                        // 更新摘要
                        summaryTextArea.setText(summary);

                        statusLabel.setText(reportType + " generated successfully. " + data.size() + " records found.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private String buildReportQuery(String reportType, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder();

        switch (reportType) {
            case "Activity Analysis by Type":
                sql.append("SELECT \n");
                sql.append("    ACT_Type as 'Activity Type',\n");
                sql.append("    COUNT(*) as 'Total Count',\n");
                sql.append("    SUM(CASE WHEN Chemical_product = 1 THEN 1 ELSE 0 END) as 'Chemical Activities',\n");
                sql.append("    ROUND(AVG(CASE WHEN Chemical_product = 1 THEN 1 ELSE 0 END) * 100, 2) as 'Chemical Usage %'\n");
                sql.append("FROM ACTIVITY\n");
                sql.append("WHERE Activity_date BETWEEN ? AND ?\n");
                sql.append("GROUP BY ACT_Type\n");
                sql.append("ORDER BY COUNT(*) DESC");
                break;

            case "Worker Activity Count":
                sql.append("SELECT \n");
                sql.append("    bw.BWID as 'Worker ID',\n");
                sql.append("    bw.BWName as 'Worker Name',\n");
                sql.append("    COUNT(a.BWID) as 'Activity Count',\n");
                sql.append("    GROUP_CONCAT(DISTINCT a.ACT_Type) as 'Activity Types',\n");
                sql.append("    COUNT(DISTINCT a.LOCID) as 'Unique Locations'\n");
                sql.append("FROM BASE_WORKER bw\n");
                sql.append("LEFT JOIN ACTIVITY a ON bw.BWID = a.BWID AND a.Activity_date BETWEEN ? AND ?\n");
                sql.append("GROUP BY bw.BWID, bw.BWName\n");
                sql.append("ORDER BY COUNT(a.BWID) DESC");
                break;

            case "Location Activity Summary":
                sql.append("SELECT \n");
                sql.append("    l.LOCID as 'Location ID',\n");
                sql.append("    l.LOC_Type as 'Location Type',\n");
                sql.append("    COUNT(a.LOCID) as 'Activity Count',\n");
                sql.append("    COUNT(DISTINCT a.BWID) as 'Unique Workers',\n");
                sql.append("    GROUP_CONCAT(DISTINCT a.ACT_Type) as 'Activity Types'\n");
                sql.append("FROM LOCATION l\n");
                sql.append("LEFT JOIN ACTIVITY a ON l.LOCID = a.LOCID AND a.Activity_date BETWEEN ? AND ?\n");
                sql.append("GROUP BY l.LOCID, l.LOC_Type\n");
                sql.append("ORDER BY COUNT(a.LOCID) DESC");
                break;

            // 其他报表类型的SQL查询...
            default:
                sql.append("SELECT 'Report type not implemented' as Status");
                break;
        }

        return sql.toString();
    }

    private String generateReportSummary(String reportType, Vector<Vector<Object>> data, String startDate, String endDate) {
        StringBuilder summary = new StringBuilder();

        summary.append("REPORT SUMMARY\n");
        summary.append("==============\n");
        summary.append("Report Type: ").append(reportType).append("\n");
        summary.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n");
        summary.append("Total Records: ").append(data.size()).append("\n\n");

        switch (reportType) {
            case "Activity Analysis by Type":
                int totalActivities = 0;
                int chemicalActivities = 0;

                for (Vector<Object> row : data) {
                    totalActivities += ((Number) row.get(1)).intValue();
                    chemicalActivities += ((Number) row.get(2)).intValue();
                }

                summary.append("Key Statistics:\n");
                summary.append("- Total Activities: ").append(totalActivities).append("\n");
                summary.append("- Chemical Activities: ").append(chemicalActivities).append("\n");
                summary.append("- Chemical Usage Rate: ").append(String.format("%.2f%%", (double) chemicalActivities / totalActivities * 100)).append("\n");
                break;

            case "Worker Activity Count":
                int activeWorkers = 0;
                int totalWorkerActivities = 0;

                for (Vector<Object> row : data) {
                    int activityCount = ((Number) row.get(2)).intValue();
                    if (activityCount > 0) {
                        activeWorkers++;
                        totalWorkerActivities += activityCount;
                    }
                }

                summary.append("Workforce Analysis:\n");
                summary.append("- Active Workers: ").append(activeWorkers).append("\n");
                summary.append("- Total Worker Activities: ").append(totalWorkerActivities).append("\n");
                summary.append("- Average Activities per Worker: ").append(String.format("%.2f", (double) totalWorkerActivities / activeWorkers)).append("\n");
                break;

            // 其他报表类型的摘要生成...
            default:
                summary.append("Summary not available for this report type.");
                break;
        }

        summary.append("\nGenerated on: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return summary.toString();
    }

    private boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 10) {
            return false;
        }

        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void exportReport() {
        // 实现导出功能
        JOptionPane.showMessageDialog(this,
                "Export functionality would be implemented here.\n" +
                        "This would save the current report as CSV or PDF format.",
                "Export Feature",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearResults() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        summaryTextArea.setText("");
        statusLabel.setText("Results cleared");
    }

    private void returnToMain() {
        parent.returnToMain();
        this.dispose();
    }
}