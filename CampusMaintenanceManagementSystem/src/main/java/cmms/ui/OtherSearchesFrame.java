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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Vector;


public class OtherSearchesFrame extends JFrame {
    private MainFrame parent;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public OtherSearchesFrame(MainFrame parent) {
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Other Searches");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1001, 699);
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

        JPanel headerPanel = createHeaderPanel("Other Searches");

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Constants.SECONDARY_COLOR);

        createFunctionSelectionPanel();
        createExternalRequestsPanel();
        createManagementHierarchyPanel();
        createWeatherSchedulingPanel();
        createWorkerActivitiesPanel();
        createCourseSchedulePanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 11, 10, 11));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(Constants.HEADER_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);

        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> returnToMain());

        JButton homeButton = new JButton("Back to Search Menu");
        homeButton.addActionListener(e -> cardLayout.show(contentPanel, "MENU"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.SECONDARY_COLOR);
        buttonPanel.add(backButton);
        buttonPanel.add(homeButton);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private void createFunctionSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
        panel.setBackground(Constants.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(49, 99, 49, 98));

        JButton externalRequestsBtn = createFunctionButton("External Company Requests");
        JButton managementBtn = createFunctionButton("Management Hierarchy");
        JButton weatherBtn = createFunctionButton("Weather-based Scheduling");
        JButton workerActivitiesBtn = createFunctionButton("Worker Activities");
        JButton courseScheduleBtn = createFunctionButton("Course Schedule");
        JButton allInfoBtn = createFunctionButton("View All Information");

        externalRequestsBtn.addActionListener(e -> cardLayout.show(contentPanel, "EXTERNAL_REQUESTS"));
        managementBtn.addActionListener(e -> cardLayout.show(contentPanel, "MANAGEMENT_HIERARCHY"));
        weatherBtn.addActionListener(e -> cardLayout.show(contentPanel, "WEATHER_SCHEDULING"));
        workerActivitiesBtn.addActionListener(e -> cardLayout.show(contentPanel, "WORKER_ACTIVITIES"));
        courseScheduleBtn.addActionListener(e -> cardLayout.show(contentPanel, "COURSE_SCHEDULE"));
        allInfoBtn.addActionListener(e -> showAllInformation());

        panel.add(externalRequestsBtn);
        panel.add(managementBtn);
        panel.add(weatherBtn);
        panel.add(workerActivitiesBtn);
        panel.add(courseScheduleBtn);
        panel.add(allInfoBtn);

        contentPanel.add(panel, "MENU");
    }

    private JButton createFunctionButton(String text) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        button.addMouseListener(new MyMouseAdapter(button));

        return button;
    }
    private void createExternalRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);

        JPanel queryPanel = new JPanel(new FlowLayout());
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Search External Company Requests"));

        JComboBox<String> officerComboBox = new JComboBox<>();
        officerComboBox.addItem("All Executive Officers");
        JButton searchBtn = createStyledButton("Search Requests");
        JButton viewAllBtn = createStyledButton("View All Requests");

        queryPanel.add(new JLabel("Executive Officer:"));
        queryPanel.add(officerComboBox);
        queryPanel.add(searchBtn);
        queryPanel.add(viewAllBtn);

        JTable resultTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        resultTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        loadExecutiveOfficers(officerComboBox);

        searchBtn.addActionListener(e -> searchExternalRequests(officerComboBox, tableModel, false));
        viewAllBtn.addActionListener(e -> searchExternalRequests(officerComboBox, tableModel, true));

        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel, "EXTERNAL_REQUESTS");
    }

    private void createManagementHierarchyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);

        JPanel queryPanel = new JPanel(new FlowLayout());
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Management Hierarchy"));

        JComboBox<String> managerComboBox = new JComboBox<>();
        managerComboBox.addItem("All Managers");
        JButton searchBtn = createStyledButton("Search Hierarchy");
        JButton viewAllBtn = createStyledButton("View Complete Hierarchy");

        queryPanel.add(new JLabel("Manager:"));
        queryPanel.add(managerComboBox);
        queryPanel.add(searchBtn);
        queryPanel.add(viewAllBtn);

        JTable resultTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        resultTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        loadManagers(managerComboBox);

        searchBtn.addActionListener(e -> searchManagementHierarchy(managerComboBox, tableModel, false));
        viewAllBtn.addActionListener(e -> searchManagementHierarchy(managerComboBox, tableModel, true));

        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel, "MANAGEMENT_HIERARCHY");
    }

    private void createWeatherSchedulingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);

        JPanel queryPanel = new JPanel(new FlowLayout());
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Weather-based Staff Scheduling"));

        JComboBox<String> weatherTypeComboBox = new JComboBox<>();
        weatherTypeComboBox.addItem("All Weather Types");
        JComboBox<String> levelComboBox = new JComboBox<>();
        levelComboBox.addItem("All Levels");
        JButton searchBtn = createStyledButton("Search Scheduling");
        JButton viewAllBtn = createStyledButton("View All Weather Plans");

        queryPanel.add(new JLabel("Weather Type:"));
        queryPanel.add(weatherTypeComboBox);
        queryPanel.add(new JLabel("Level:"));
        queryPanel.add(levelComboBox);
        queryPanel.add(searchBtn);
        queryPanel.add(viewAllBtn);

        JTable resultTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        resultTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        loadWeatherTypes(weatherTypeComboBox);
        loadWeatherLevels(levelComboBox);

        searchBtn.addActionListener(e -> searchWeatherScheduling(weatherTypeComboBox, levelComboBox, tableModel, false));
        viewAllBtn.addActionListener(e -> searchWeatherScheduling(weatherTypeComboBox, levelComboBox, tableModel, true));

        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel, "WEATHER_SCHEDULING");
    }

    private void createWorkerActivitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);

        JPanel queryPanel = new JPanel(new GridLayout(2, 1));
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Worker Activities Search"));

        JPanel workerSelectPanel = new JPanel(new FlowLayout());
        workerSelectPanel.setBackground(Color.WHITE);

        JComboBox<String> workerComboBox = new JComboBox<>();
        workerComboBox.addItem("Select Worker");
        JButton addWorkerBtn = createStyledButton("Add Worker");
        JButton clearWorkersBtn = createStyledButton("Clear Selection");

        JTextArea selectedWorkersArea = new JTextArea(2, 29);
        selectedWorkersArea.setEditable(false);
        selectedWorkersArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        workerSelectPanel.add(new JLabel("Worker:"));
        workerSelectPanel.add(workerComboBox);
        workerSelectPanel.add(addWorkerBtn);
        workerSelectPanel.add(new JLabel("Selected Workers:"));
        workerSelectPanel.add(new JScrollPane(selectedWorkersArea));
        workerSelectPanel.add(clearWorkersBtn);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        JButton searchBtn = createStyledButton("Search Activities");
        JButton viewAllBtn = createStyledButton("View All Worker Activities");

        buttonPanel.add(searchBtn);
        buttonPanel.add(viewAllBtn);

        queryPanel.add(workerSelectPanel);
        queryPanel.add(buttonPanel);

        JTable resultTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        resultTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        loadWorkers(workerComboBox);

        addWorkerBtn.addActionListener(e -> addSelectedWorker(workerComboBox, selectedWorkersArea));
        clearWorkersBtn.addActionListener(e -> selectedWorkersArea.setText(""));
        searchBtn.addActionListener(e -> searchWorkerActivities(selectedWorkersArea, tableModel, false));
        viewAllBtn.addActionListener(e -> searchWorkerActivities(selectedWorkersArea, tableModel, true));

        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel, "WORKER_ACTIVITIES");
    }

    private void createCourseSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.SECONDARY_COLOR);

        JPanel queryPanel = new JPanel(new GridLayout(2, 1));
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Course Schedule Search"));

        JPanel criteriaPanel = new JPanel(new FlowLayout());
        criteriaPanel.setBackground(Color.WHITE);

        JComboBox<String> locationComboBox = new JComboBox<>();
        locationComboBox.addItem("All Locations");
        JComboBox<String> dayComboBox = new JComboBox<>();
        dayComboBox.addItem("All Days");
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            dayComboBox.addItem(day);
        }
        JTextField timeSlotField = new JTextField(11);
        criteriaPanel.add(new JLabel("Location:"));
        criteriaPanel.add(locationComboBox);
        criteriaPanel.add(new JLabel("Day:"));
        criteriaPanel.add(dayComboBox);
        criteriaPanel.add(new JLabel("Time Slot:"));
        criteriaPanel.add(timeSlotField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        JButton searchBtn = createStyledButton("Search Courses");
        JButton viewAllBtn = createStyledButton("View All Courses");

        buttonPanel.add(searchBtn);
        buttonPanel.add(viewAllBtn);

        queryPanel.add(criteriaPanel);
        queryPanel.add(buttonPanel);

        JTable resultTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        resultTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        loadCourseLocations(locationComboBox);

        searchBtn.addActionListener(e -> searchCourseSchedules(locationComboBox, dayComboBox, timeSlotField, tableModel, false));
        viewAllBtn.addActionListener(e -> searchCourseSchedules(locationComboBox, dayComboBox, timeSlotField, tableModel, true));

        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel, "COURSE_SCHEDULE");
    }

    private void loadExecutiveOfficers(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT EOID, EOName FROM EXECUTE_OFFICIER")) {

                    while (rs.next()) {
                        String eoid = rs.getString("EOID");
                        String name = rs.getString("EOName");
                        String displayText = eoid + " - " + name;

                        SwingUtilities.invokeLater(() -> comboBox.addItem(displayText));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadManagers(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT MMID, MMName FROM MID_MANAGER")) {

                    while (rs.next()) {
                        String eoid = rs.getString("MMID");
                        String name = rs.getString("MMName");
                        String displayText = eoid + " - " + name;

                        SwingUtilities.invokeLater(() -> comboBox.addItem(displayText));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadWeatherTypes(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT WEA_Type FROM BAD_WEATHER")) {

                    while (rs.next()) {
                        String eoid = rs.getString("WEA_Type");
                        SwingUtilities.invokeLater(() -> comboBox.addItem(eoid));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadWeatherLevels(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT WEA_Level FROM BAD_WEATHER")) {

                    while (rs.next()) {
                        String eoid = rs.getString("WEA_Level");
                        SwingUtilities.invokeLater(() -> comboBox.addItem(eoid));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadWorkers(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT BWID, BWName FROM BASE_WORKER")) {

                    while (rs.next()) {
                        String eoid = rs.getString("BWID");
                        String name = rs.getString("BWName");
                        String displayText = eoid + " - " + name;

                        SwingUtilities.invokeLater(() -> comboBox.addItem(displayText));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadCourseLocations(JComboBox<String> comboBox) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                   Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT DISTINCT LOCID FROM COURSE ORDER BY LOCID");//) {

                    while (rs.next()) {
                        String locid = rs.getString("LOCID");
                        SwingUtilities.invokeLater(() -> comboBox.addItem(locid));
                    }
                return null;
            }
        };
        worker.execute();
    }

    private void searchExternalRequests(JComboBox<String> officerComboBox, DefaultTableModel tableModel, boolean viewAll) {
        List<String> parameters = new ArrayList<>();
        String sql;

        if (viewAll) {
            sql = "SELECT c.EOID, c.EXCOMPID, e.EOName, ex.EXCOMP_Name,ex.Contact_number,ex.Email_address\n"+
                  "FROM CONTACT c\n"+
                  "JOIN EXECUTE_OFFICIER e ON e.EOID=c.EOID\n"+
                  "JOIN EXTERNAL_COMPANY ex ON ex.EXCOMPID=c.EXCOMPID;";
        } else {
            String selectedOfficer = (String) officerComboBox.getSelectedItem();
            if (selectedOfficer == null || selectedOfficer.equals("All Executive Officers")) {
                JOptionPane.showMessageDialog(this,
                        "Please select an executive officer first.",
                        "No Executive Officer Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String eoid = selectedOfficer.split(" - ")[0].trim();
            parameters.add(eoid);

            sql = "SELECT c.EOID, c.EXCOMPID, e.EOName, ex.EXCOMP_Name,ex.Contact_number,ex.Email_address\n"+
                  "FROM CONTACT c\n"+
                  "JOIN EXECUTE_OFFICIER e ON e.EOID=c.EOID\n"+
                  "JOIN EXTERNAL_COMPANY ex ON ex.EXCOMPID=c.EXCOMPID\n"+
                  "WHERE c.EOID=?;"
;
        }
        executeExtQuery(sql, parameters, tableModel);
    }

    private void executeExtQuery(String sql, List<String> parameters, DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground()  {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    for (int i = 0; i < parameters.size(); i++) {
                        pstmt.setString(i + 1, parameters.get(i));
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        columnNames = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnLabel(i));
                        }

                        data = new Vector<>();
                        while (rs.next()) {
                            Vector<Object> row = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                if (value == null) {
                                    value = "N/A";
                                }
                                row.add(value);
                            }
                            data.add(row);
                        }

                    }
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    System.err.println("SQL Error: " + errorMessage);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (errorMessage != null) {
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                "Database Error: " + errorMessage,
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.setDataVector(data, columnNames);

                        String message = String.format("Found %d external company records", data.size());
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                message,
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                            "Unexpected error: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void searchManagementHierarchy(JComboBox<String> managerComboBox, DefaultTableModel tableModel, boolean viewAll) {
        List<String> parameters = new ArrayList<>();
        String sql;
        if (viewAll) {
            sql = "SELECT m.MMID, m.MMName, e.EOID, e.EOName, b.BWID, b.BWName " +
                    "FROM MID_MANAGER m " +
                    "JOIN MID_TO_BASE_MANAGE r ON m.MMID = r.MMID " +
                    "JOIN BASE_WORKER b ON r.BWID = b.BWID " +
                    "JOIN EXECUTE_OFFICIER e ON m.EOID = e.EOID ";
        } else {
            String selectedManager = (String) managerComboBox.getSelectedItem();
            if (selectedManager == null || selectedManager.equals("All Managers")) {
                JOptionPane.showMessageDialog(this,
                        "Please select a manager first.",
                        "No Manager Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String mmid = selectedManager.split(" - ")[0].trim();
            parameters.add(mmid);

            sql = "SELECT m.MMID, m.MMName, e.EOID, e.EOName, b.BWID, b.BWName " +
                    "FROM MID_MANAGER m " +
                    "JOIN MID_TO_BASE_MANAGE r ON m.MMID = r.MMID " +
                    "JOIN BASE_WORKER b ON r.BWID = b.BWID " +
                    "JOIN EXECUTE_OFFICIER e ON m.EOID = e.EOID " +
                    "WHERE m.MMID = ? ";
        }

        executeManagementQuery(sql, parameters, tableModel);
    }

    private void executeManagementQuery(String sql, List<String> parameters, DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (int i = 0; i < parameters.size(); i++) {
                        pstmt.setString(i + 1, parameters.get(i));
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        columnNames = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnLabel(i));
                        }

                        data = new Vector<>();
                        while (rs.next()) {
                            Vector<Object> row = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                if (value == null) {
                                    value = "N/A";
                                }
                                row.add(value);
                            }
                            data.add(row);
                        }
                    }
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    System.err.println("SQL Error: " + errorMessage);
                }
                return null;
            }

            @Override
            protected void done() {
                    if (errorMessage != null) {
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                "Database Error: " + errorMessage,
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.setDataVector(data, columnNames);
                        String message = String.format("Found %d management hierarchy records", data.size());
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                message,
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
            }
        };

        worker.execute();
    }
    private void searchWeatherScheduling(JComboBox<String> weatherTypeComboBox, JComboBox<String> levelComboBox, DefaultTableModel tableModel, boolean viewAll) {
        List<String> parameters = new ArrayList<>();
        String sql;

        if (viewAll) {
            sql = "SELECT b.WEA_Type, b.WEA_Level, b.EPID, io.MMID, m.MMName, i.BWID, bw.BWName, ep.Plan " +
                    "FROM BAD_WEATHER b " +
                    "JOIN IMPLEMENT_ORGANIZE io ON b.EPID = io.EPID " +
                    "JOIN MID_MANAGER m ON m.MMID = io.MMID " +
                    "JOIN IMPLEMENT i ON b.EPID = i.EPID " +
                    "JOIN BASE_WORKER bw ON i.BWID = bw.BWID " +
                    "JOIN EMERGENCY_PLAN ep ON b.EPID = ep.EPID ";
        } else {
            String selectedWeatherType = (String) weatherTypeComboBox.getSelectedItem();
            String selectedLevel = (String) levelComboBox.getSelectedItem();

            if (selectedWeatherType == null || selectedWeatherType.equals("All Weather Types")) {
                JOptionPane.showMessageDialog(this,
                        "Please select a weather type first.",
                        "No Weather Type Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            StringBuilder whereClause = new StringBuilder();

            if (!selectedWeatherType.equals("All Weather Types")) {
                whereClause.append("b.WEA_Type = ?");
                parameters.add(selectedWeatherType);
            }

            if (selectedLevel != null && !selectedLevel.equals("All Levels")) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("b.WEA_Level = ?");
                parameters.add(selectedLevel);
            }

            sql = "SELECT b.WEA_Type, b.WEA_Level, b.EPID, io.MMID, m.MMName, i.BWID, bw.BWName, ep.Plan " +
              "FROM BAD_WEATHER b " +
              "JOIN IMPLEMENT_ORGANIZE io ON b.EPID = io.EPID " +
              "JOIN MID_MANAGER m ON m.MMID = io.MMID " +
              "JOIN IMPLEMENT i ON b.EPID = i.EPID " +
              "JOIN BASE_WORKER bw ON i.BWID = bw.BWID " +
              "JOIN EMERGENCY_PLAN ep ON b.EPID = ep.EPID " +
               (whereClause.length() > 0 ? "WHERE " + whereClause : "");
        }
        executeWeatherQuery(sql, parameters, tableModel);
    }

    private void executeWeatherQuery(String sql, List<String> parameters, DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (int i = 0; i < parameters.size(); i++) {
                        pstmt.setString(i + 1, parameters.get(i));
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        columnNames = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnLabel(i));
                        }

                        data = new Vector<>();
                        while (rs.next()) {
                            Vector<Object> row = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                if (value == null) {
                                    value = "N/A";
                                }
                                row.add(value);
                            }
                            data.add(row);
                        }
                    }
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    System.err.println("SQL Error: " + errorMessage);
                }
                return null;
            }

            @Override
            protected void done() {
                    if (errorMessage != null) {
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                "Database Error: " + errorMessage,
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.setDataVector(data, columnNames);

                        String message = String.format("Found %d weather scheduling records", data.size());
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                message,
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
            }
        };

        worker.execute();
    }

    private void searchWorkerActivities(JTextArea selectedWorkersArea, DefaultTableModel tableModel, boolean viewAll) {
        List<String> parameters = new ArrayList<>();
        String sql ;

        if (viewAll) {
            sql = "SELECT b.BWID, b.BWName, a.LOCID, a.Activity_date, a.Activity_time, " +
                    "a.ACT_Type, a.Chemical_product " +
                    "FROM BASE_WORKER b " +
                    "JOIN ACTIVITY a ON b.BWID = a.BWID ";
        } else {
        String selectedWorkers = selectedWorkersArea.getText().trim();
        if (selectedWorkers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one worker first.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] wIds = selectedWorkers.split(",");

        List<String> validWorkerIds = new ArrayList<>();
        for (String id : wIds) {
            String trimmedId = id.trim();
            if (!trimmedId.isEmpty()) {
                validWorkerIds.add(trimmedId);
            }
        }

        if (validWorkerIds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No valid worker IDs found.",
                    "Invalid",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < validWorkerIds.size(); i++) {
            if (i > 0) placeholders.append(",");
            placeholders.append("?");
        }
        sql="SELECT b.BWID, b.BWName, a.LOCID, a.Activity_date, a.Activity_time, a.ACT_Type, a.Chemical_product " +
            "FROM BASE_WORKER b " +
            "JOIN ACTIVITY a ON b.BWID=a.BWID " +
            "WHERE b.BWID IN (" + placeholders + ") " ;
        parameters.addAll(validWorkerIds);}
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (int i = 0; i < parameters.size(); i++) {
                        pstmt.setString(i + 1, parameters.get(i));
                        System.out.println("Set parameter " + (i + 1) + " to: " + parameters.get(i));
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        columnNames = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnLabel(i));
                        }
                        data = new Vector<>();
                        while (rs.next()) {
                            Vector<Object> row = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                row.add(value);
                            }
                            data.add(row);
                        }
                    }
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                    if (errorMessage != null) {
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                "Database Error: " + errorMessage,
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.setDataVector(data, columnNames);
                        String message = String.format("Found %d activity records", data.size());
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                message,
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
            }
        };

        worker.execute();
    }
    private void addSelectedWorker(JComboBox<String> workerComboBox, JTextArea selectedWorkersArea) {
        String selected = (String) workerComboBox.getSelectedItem();
        if (selected != null && !selected.equals("Select Worker")) {
            String current = selectedWorkersArea.getText().trim();
            String workerId = selected.split(" - ")[0].trim();
            if (!current.isEmpty()) {
                String[] existingIds = current.split(",");
                for (String existingId : existingIds) {
                    if (existingId.trim().equals(workerId)) {
                        JOptionPane.showMessageDialog(this,
                                "Worker " + workerId + " is already selected.",
                                "Duplicate Worker",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                current += "," + workerId;
            } else {
                current = workerId;
            }
            selectedWorkersArea.setText(current);
        }
    }
    private void searchCourseSchedules(JComboBox<String> locationComboBox, JComboBox<String> dayComboBox, JTextField timeSlotField, DefaultTableModel tableModel, boolean viewAll) {
        List<String> parameters = new ArrayList<>();
        String sql;

        if (viewAll) {
            sql = "SELECT COURSEID, Dayofweek, Timeslot, LOCID " +
                    "FROM COURSE ";
        } else {
            String selectedLocation = (String) locationComboBox.getSelectedItem();
            String selectedDay = (String) dayComboBox.getSelectedItem();
            String timeSlot = timeSlotField.getText().trim();

            StringBuilder whereClause = new StringBuilder();

            if (selectedLocation != null && !selectedLocation.equals("All Locations")) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("LOCID = ?");
                parameters.add(selectedLocation);
            }

            if (selectedDay != null && !selectedDay.equals("All Days")) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("Dayofweek = ?");
                parameters.add(selectedDay);
            }

            if (!timeSlot.isEmpty()) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }

                String[] timeParts = parseTimeSlot(timeSlot);
                    whereClause.append("(SUBSTRING_INDEX(Timeslot, '-', 1) >= ? AND SUBSTRING_INDEX(Timeslot, '-', -1) <= ?)");
                    parameters.add(timeParts[0]);
                    parameters.add(timeParts[1]);

            }

            if (whereClause.length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select at least one search criteria.",
                        "No Criteria Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            sql = "SELECT COURSEID, Dayofweek, Timeslot, LOCID " +
                    "FROM COURSE " +
                    "WHERE " + whereClause + " ";
        }
        executeCourseQuery(sql, parameters, tableModel);
    }

    private String[] parseTimeSlot(String timeSlot) {
        String cleanTimeSlot = timeSlot.replaceAll("\\s+", "");
        String[] parts = cleanTimeSlot.split("-");
        if (parts.length != 2) {
            return null;
        }
        return parts;
    }


    private void executeCourseQuery(String sql, List<String> parameters, DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            private String errorMessage;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    for (int i = 0; i < parameters.size(); i++) {
                        pstmt.setString(i + 1, parameters.get(i));
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        columnNames = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnLabel(i));
                        }

                        data = new Vector<>();
                        while (rs.next()) {
                            Vector<Object> row = new Vector<>();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);

                                if (value == null) {
                                    value = "N/A";
                                }
                                row.add(value);
                            }
                            data.add(row);
                        }
                    }
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    System.err.println("SQL Error: " + errorMessage);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                        tableModel.setDataVector(data, columnNames);

                        String message = String.format("Found %d course schedule records", data.size());
                        JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                                message,
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(OtherSearchesFrame.this,
                            "Unexpected error: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
    private void showAllInformation() {

        JFrame allInfoFrame = new JFrame("All Information Summary");
        allInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allInfoFrame.setSize(1201, 801);
        allInfoFrame.setLocationRelativeTo(this);

        JTabbedPane tabbedPane = new JTabbedPane();

        createExternalRequestsSummary(tabbedPane);
        createManagementHierarchySummary(tabbedPane);
        createWeatherSchedulingSummary(tabbedPane);
        createWorkerActivitiesSummary(tabbedPane);
        createCourseScheduleSummary(tabbedPane);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        allInfoFrame.setLayout(new BorderLayout());
        allInfoFrame.add(tabbedPane, BorderLayout.CENTER);
        allInfoFrame.add(buttonPanel, BorderLayout.SOUTH);

        allInfoFrame.setVisible(true);
    }

    private void createExternalRequestsSummary(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 9, 10, 9));

        JLabel titleLabel = new JLabel("External Company Requests Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadExternalRequestsData(tableModel);

        tabbedPane.addTab("External Requests", panel);
    }

    private void createManagementHierarchySummary(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 9, 10, 9));

        JLabel titleLabel = new JLabel("Management Hierarchy Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadManagementHierarchyData(tableModel);

        tabbedPane.addTab("Management Hierarchy", panel);
    }

    private void createWeatherSchedulingSummary(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 9, 10, 9));

        JLabel titleLabel = new JLabel("Weather Scheduling Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadWeatherSchedulingData(tableModel);

        tabbedPane.addTab("Weather Scheduling", panel);
    }

    private void createWorkerActivitiesSummary(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 9, 10, 9));

        JLabel titleLabel = new JLabel("Worker Activities Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadWorkerActivitiesData(tableModel);

        tabbedPane.addTab("Worker Activities", panel);
    }

    private void createCourseScheduleSummary(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 9, 10, 9));

        JLabel titleLabel = new JLabel("Course Schedule Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadCourseScheduleData(tableModel);

        tabbedPane.addTab("Course Schedule", panel);
    }

    private void loadExternalRequestsData(DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;
            @Override
            protected Void doInBackground() {
                String sql = "SELECT c.EOID, c.EXCOMPID, e.EOName, ex.EXCOMP_Name,ex.Contact_number,ex.Email_address " +
                             "FROM CONTACT c " +
                                "JOIN EXECUTE_OFFICIER e ON e.EOID=c.EOID " +
                                "JOIN EXTERNAL_COMPANY ex ON ex.EXCOMPID=c.EXCOMPID;";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnLabel(i));
                    }
                    data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            if (value instanceof java.sql.Date) {
                                value = ((java.sql.Date) value).toLocalDate().toString();
                            } else if (value == null) {
                                value = "N/A";
                            }
                            row.add(value);
                        }
                        data.add(row);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                tableModel.setDataVector(data, columnNames);
            }
        };
        worker.execute();
    }

    private void loadManagementHierarchyData(DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;

            @Override
            protected Void doInBackground() {
                String sql = "SELECT m.MMID, m.MMName, e.EOID, e.EOName, b.BWID, b.BWName " +
                        "FROM MID_MANAGER m " +
                        "JOIN MID_TO_BASE_MANAGE r ON m.MMID = r.MMID " +
                        "JOIN BASE_WORKER b ON r.BWID = b.BWID " +
                        "JOIN EXECUTE_OFFICIER e ON m.EOID = e.EOID ";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnLabel(i));
                    }

                    data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            if (value == null) value = "N/A";
                            row.add(value);
                        }
                        data.add(row);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                tableModel.setDataVector(data, columnNames);
            }
        };
        worker.execute();
    }

    private void loadWeatherSchedulingData(DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;

            @Override
            protected Void doInBackground() throws Exception {
                String sql ="SELECT b.WEA_Type, b.WEA_Level, b.EPID, io.MMID, m.MMName, i.BWID, bw.BWName, ep.Plan " +
                            "FROM BAD_WEATHER b " +
                            "JOIN IMPLEMENT_ORGANIZE io ON b.EPID = io.EPID " +
                            "JOIN MID_MANAGER m ON m.MMID = io.MMID " +
                            "JOIN IMPLEMENT i ON b.EPID = i.EPID " +
                            "JOIN BASE_WORKER bw ON i.BWID = bw.BWID " +
                            "JOIN EMERGENCY_PLAN ep ON b.EPID = ep.EPID " +
                            "ORDER BY b.WEA_Type, b.WEA_Level";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnLabel(i));
                    }

                    data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            if (value == null) value = "N/A";
                            row.add(value);
                        }
                        data.add(row);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                tableModel.setDataVector(data, columnNames);
            }
        };
        worker.execute();
    }

    private void loadWorkerActivitiesData(DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;

            @Override
            protected Void doInBackground() throws Exception {
                String sql = "SELECT b.BWID, b.BWName, a.LOCID, a.Activity_date, a.Activity_time, " +
                                "a.ACT_Type, a.Chemical_product " +
                                "FROM BASE_WORKER b " +
                                "JOIN ACTIVITY a ON b.BWID = a.BWID ";


                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnLabel(i));
                    }

                    data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            if (value instanceof java.sql.Date) {
                                value = ((java.sql.Date) value).toLocalDate().toString();
                            } else if (value instanceof java.sql.Time) {
                                value = ((java.sql.Time) value).toLocalTime().toString();
                            } else if (value == null) {
                                value = "N/A";
                            }
                            row.add(value);
                        }
                        data.add(row);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                tableModel.setDataVector(data, columnNames);
            }
        };
        worker.execute();
    }

    private void loadCourseScheduleData(DefaultTableModel tableModel) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Vector<Vector<Object>> data;
            private Vector<String> columnNames;

            @Override
            protected Void doInBackground() {
                String sql = "SELECT COURSEID, Dayofweek, Timeslot, LOCID " +
                            "FROM COURSE ";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnLabel(i));
                    }

                    data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            if (value == null) value = "N/A";
                            row.add(value);
                        }
                        data.add(row);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                tableModel.setDataVector(data, columnNames);
            }
        };
        worker.execute();
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

    private void returnToMain() {
        parent.returnToMain();
        this.dispose();
    }

    private static class MyMouseAdapter extends java.awt.event.MouseAdapter {
        private final JButton button;

        public MyMouseAdapter(JButton button) {
            this.button = button;
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(Constants.ACCENT_COLOR);
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(Constants.PRIMARY_COLOR);
        }
    }
}