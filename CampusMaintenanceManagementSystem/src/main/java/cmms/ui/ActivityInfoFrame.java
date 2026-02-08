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
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class ActivityInfoFrame extends JFrame {
    private MainFrame parent;
    private JComboBox<String> locationComboBox;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;

    public ActivityInfoFrame(MainFrame parent) {
        this.parent = parent;
        initializeUI();
        loadLocations();
    }

    private void initializeUI() {
        setTitle("Activity Information");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
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
        JPanel headerPanel = createHeaderPanel("Activity Information");

        // 查询条件面板
        JPanel queryPanel = createQueryPanel();

        // 按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 结果展示区域
        JPanel resultPanel = createResultPanel();

        // 状态栏
        JPanel statusPanel = createStatusPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(queryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                mainPanel, resultPanel);
        splitPane.setResizeWeight(0.2);

        setContentPane(new JPanel(new BorderLayout()));
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
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

    private JFormattedTextField createFormattedDateField(String defaultValue) {
        try {
            // 使用掩码格式化器创建日期字段
            MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
            dateFormatter.setPlaceholderCharacter('_');
            dateFormatter.setValidCharacters("0123456789");

            JFormattedTextField field = new JFormattedTextField(dateFormatter);
            field.setValue(defaultValue);
            field.setColumns(10);
            field.setHorizontalAlignment(JTextField.CENTER);

            // 添加焦点监听器，使选择文本更容易
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
            // 如果掩码格式化失败，创建一个普通文本字段
            JFormattedTextField field = new JFormattedTextField();
            field.setValue(defaultValue);
            field.setColumns(10);
            return field;
        }
    }

    /*
    private JFormattedTextField createFormattedDateField(String defaultValue) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormatter formatter = new DateFormatter(dateFormat);
            formatter.setValueClass(Date.class);

            JFormattedTextField field = new JFormattedTextField(formatter);
            field.setValue(dateFormat.parse(defaultValue));
            field.setColumns(10);
            field.setHorizontalAlignment(JTextField.CENTER);

            // 添加焦点监听器，使选择文本更容易
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
            return new JFormattedTextField();
        }
    }
    */

    private JPanel createQueryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 地点选择
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        locationComboBox = new JComboBox<>();
        locationComboBox.addItem("All Locations"); // 默认选项
        panel.add(locationComboBox, gbc);
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
        /*
        // 开始日期
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Date:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        // 使用更灵活的日期模型，允许按天调整
        startDateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);
        // 设置默认值为2025年11月1日，以匹配您的数据
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startDateSpinner.setValue(sdf.parse("2025-11-01"));
        } catch (Exception e) {
            startDateSpinner.setValue(new Date()); // 备用方案
        }
        panel.add(startDateSpinner, gbc);

        // 结束日期
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("End Date:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        // 使用更灵活的日期模型
        endDateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);
        // 设置默认值为2025年11月30日，以匹配您的数据
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            endDateSpinner.setValue(sdf.parse("2025-11-30"));
        } catch (Exception e) {
            // 默认结束日期为开始日期后7天
            Date endDate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
            endDateSpinner.setValue(endDate);
        }
        panel.add(endDateSpinner, gbc);
        */

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchBtn = createStyledButton("Search Activities");
        JButton clearBtn = createStyledButton("Clear Results");

        searchBtn.addActionListener(e -> searchActivities());
        clearBtn.addActionListener(e -> clearResults());

        panel.add(searchBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Activity Results"));
        panel.setBackground(Color.WHITE);

        // 创建表格模型和表格
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel("Ready to search activities");
        statusLabel.setFont(Constants.NORMAL_FONT);

        panel.add(statusLabel);
        return panel;
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

    private void loadLocations() {
        // 在后台线程中加载地点数据
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT LOCID, LOC_Type FROM LOCATION ORDER BY LOC_Type, LOCID")) {

                    while (rs.next()) {
                        String locationId = rs.getString("LOCID");
                        String locationType = rs.getString("LOC_Type");
                        String displayText = locationId + " - " + locationType;

                        // 在EDT线程中更新UI
                        SwingUtilities.invokeLater(() -> {
                            locationComboBox.addItem(displayText);
                        });
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ActivityInfoFrame.this,
                                "Error loading locations: " + e.getMessage(),
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
                return null;
            }
        };

        worker.execute();
    }

    private boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 10) {
            return false;
        }

        // 检查格式是否为 YYYY-MM-DD
        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        // 尝试解析日期
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // 严格解析
            dateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void searchActivities() {
        String selectedLocation = (String) locationComboBox.getSelectedItem();
        // 获取日期值 - 现在是从 JFormattedTextField 获取字符串
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

        // 将字符串转换为 Date 对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;
        try {
            startDate = dateFormat.parse(startDateStr);
            endDate = dateFormat.parse(endDateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error parsing dates: " + e.getMessage(),
                    "Date Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (startDate.after(endDate)) {
            JOptionPane.showMessageDialog(this,
                    "Start date cannot be after end date",
                    "Invalid Date Range",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        /*
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();

        if (startDate.after(endDate)) {
            JOptionPane.showMessageDialog(this,
                    "Start date cannot be after end date",
                    "Invalid Date Range",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        */

        // 在后台线程中执行查询
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    statusLabel.setText("Searching activities...");

                    // 构建查询
                    String sql = buildQuery(selectedLocation, startDate, endDate);

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // 设置参数
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        pstmt.setString(1, dateFormat.format(startDate));
                        pstmt.setString(2, dateFormat.format(endDate));

                        if (selectedLocation != null && !selectedLocation.equals("All Locations")) {
                            String locationId = selectedLocation.split(" - ")[0];
                            pstmt.setString(3, locationId);
                        }

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
                        JOptionPane.showMessageDialog(ActivityInfoFrame.this,
                                "Database Error: " + errorMessage,
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // 更新表格
                        tableModel.setDataVector(data, columnNames);

                        // 调整列宽
                        for (int i = 0; i < resultTable.getColumnCount(); i++) {
                            resultTable.getColumnModel().getColumn(i).setPreferredWidth(150);
                        }

                        statusLabel.setText("Found " + data.size() + " activities");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private String buildQuery(String selectedLocation, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT \n");
        sql.append("    a.Activity_date as 'Date',\n");
        sql.append("    a.Activity_time as 'Time',\n");
        sql.append("    a.ACT_Type as 'Activity Type',\n");
        sql.append("    a.Note as 'Description',\n");
        sql.append("    bw.BWName as 'Worker Name',\n");
        sql.append("    l.LOCID as 'Location ID',\n");
        sql.append("    l.LOC_Type as 'Location Type',\n");
        sql.append("    CASE \n");
        sql.append("        WHEN a.Chemical_product = 1 THEN 'Yes'\n");
        sql.append("        ELSE 'No'\n");
        sql.append("    END as 'Chemical Used',\n");
        sql.append("    CASE \n");
        sql.append("        WHEN a.ACT_Type IN ('Renovation', 'Maintenance') THEN 'Area may be temporarily unavailable'\n");
        sql.append("        ELSE 'Normal usage'\n");
        sql.append("    END as 'Availability Status'\n");
        sql.append("FROM ACTIVITY a\n");
        sql.append("JOIN BASE_WORKER bw ON a.BWID = bw.BWID\n");
        sql.append("JOIN LOCATION l ON a.LOCID = l.LOCID\n");
        sql.append("WHERE a.Activity_date BETWEEN ? AND ?\n");

        if (selectedLocation != null && !selectedLocation.equals("All Locations")) {
            sql.append("  AND a.LOCID = ?\n");
        }

        sql.append("ORDER BY a.Activity_date, a.Activity_time");

        return sql.toString();
    }

    private void clearResults() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        statusLabel.setText("Results cleared");
    }

    private void returnToMain() {
        parent.returnToMain();
        this.dispose();
    }
}