package main.java.cmms.ui;

import main.java.cmms.util.Constants;
import main.java.cmms.database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class SQLQueryFrame extends JFrame {
    private MainFrame parent;
    private JTextArea sqlTextArea;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SQLQueryFrame(MainFrame parent) {
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SQL Query Interface");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
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
        JPanel headerPanel = createHeaderPanel("SQL Query Interface");

        // SQL输入区域
        JPanel sqlInputPanel = createSQLInputPanel();

        // 按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 结果展示区域
        JPanel resultPanel = createResultPanel();

        // 状态栏
        JPanel statusPanel = createStatusPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sqlInputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                mainPanel, resultPanel);
        splitPane.setResizeWeight(0.3);

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

    private JPanel createSQLInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("SQL Query Input"));
        panel.setBackground(Color.WHITE);

        sqlTextArea = new JTextArea();
        sqlTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sqlTextArea.setLineWrap(true);
        sqlTextArea.setWrapStyleWord(true);

        // 添加示例SQL
        sqlTextArea.setText("-- Enter your SQL query here\n" +
                "SELECT * FROM BASE_WORKER LIMIT 10;");

        JScrollPane scrollPane = new JScrollPane(sqlTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 150));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton executeBtn = createStyledButton("Execute Query");
        JButton clearBtn = createStyledButton("Clear");
        JButton exampleBtn = createStyledButton("Example Queries");

        executeBtn.addActionListener(e -> executeQuery());
        clearBtn.addActionListener(e -> clearQuery());
        exampleBtn.addActionListener(e -> showExampleQueries());

        panel.add(executeBtn);
        panel.add(clearBtn);
        panel.add(exampleBtn);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Query Results"));
        panel.setBackground(Color.WHITE);

        // 创建表格模型和表格
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(Constants.NORMAL_FONT);

        panel.add(statusLabel);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color. BLACK);
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

    private void executeQuery() {
        String sql = sqlTextArea.getText().trim();

        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a SQL query",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 在后台线程中执行查询
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private ResultSet resultSet;
            private String errorMessage;

            @Override
            protected Boolean doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {

                    statusLabel.setText("Executing query...");

                    boolean hasResultSet = stmt.execute(sql);

                    if (hasResultSet) {
                        resultSet = stmt.getResultSet();
                        return true;
                    } else {
                        int updateCount = stmt.getUpdateCount();
                        statusLabel.setText("Query executed successfully. " + updateCount + " rows affected.");
                        return false;
                    }

                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean hasResults = get();

                    if (hasResults) {
                        displayResultSet(resultSet);
                        statusLabel.setText("Query executed successfully. Results loaded.");
                    }

                } catch (Exception e) {
                    if (errorMessage != null) {
                        statusLabel.setText("Error: " + errorMessage);
                        JOptionPane.showMessageDialog(SQLQueryFrame.this,
                                "SQL Error: " + errorMessage,
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        };

        worker.execute();
    }

    private void displayResultSet(ResultSet resultSet) throws SQLException {
        // 获取结果集的元数据
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 创建列名数组
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // 创建数据向量
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getObject(i));
            }
            data.add(row);
        }

        // 更新表格模型
        tableModel.setDataVector(data, columnNames);

        // 调整列宽
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(120);
        }

        // 关闭结果集
        resultSet.close();
    }

    private void clearQuery() {
        sqlTextArea.setText("");
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        statusLabel.setText("Cleared");
    }

    private void showExampleQueries() {
        String[] examples = {
                "SELECT * FROM BASE_WORKER;",
                "SELECT BWID, BWName, Address FROM BASE_WORKER WHERE Address LIKE '%Campus%';",
                "SELECT ACT_Type, COUNT(*) as Count FROM ACTIVITY GROUP BY ACT_Type;",
                "SELECT l.LOCID, l.LOC_Type, COUNT(a.LOCID) as ActivityCount " +
                        "FROM LOCATION l LEFT JOIN ACTIVITY a ON l.LOCID = a.LOCID " +
                        "GROUP BY l.LOCID, l.LOC_Type ORDER BY ActivityCount DESC;",
                "SELECT eo.EOName, ec.EXCOMP_Name " +
                        "FROM CONTACT c " +
                        "JOIN EXECUTE_OFFICIER eo ON c.EOID = eo.EOID " +
                        "JOIN EXTERNAL_COMPANY ec ON c.EXCOMPID = ec.EXCOMPID;"
        };

        String selectedExample = (String) JOptionPane.showInputDialog(
                this,
                "Select an example query:",
                "Example Queries",
                JOptionPane.QUESTION_MESSAGE,
                null,
                examples,
                examples[0]
        );

        if (selectedExample != null) {
            sqlTextArea.setText(selectedExample);
        }
    }

    private void returnToMain() {
        parent.returnToMain();
        this.dispose();
    }
}