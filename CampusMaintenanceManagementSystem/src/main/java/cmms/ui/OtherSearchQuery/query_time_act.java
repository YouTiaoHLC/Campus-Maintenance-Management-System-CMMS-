package main.java.cmms.ui.OtherSearchQuery;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class query_time_act {
    public static void q_activity_time(String url, String user, String password, Date dateVariable) {
        String sql = "SELECT a.ACID, b.BWName " +
                "FROM ACTIVITY a " +
                "JOIN BASE_WORKER b ON a.BWID = b.BWID " +
                "WHERE a.Date = ? " +
                "ORDER BY a.ACID;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, dateVariable); // 设置日期变量

            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    int acid = rs.getInt("ACID");
                    String bwName = rs.getString("BWName");
                    result.append("ACID: ").append(acid).append(", BWName: ").append(bwName).append("\n");
                }
                JOptionPane.showMessageDialog(null, result.toString(), "查询结果", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//整合时 用下面这个方法
    public static void q_activity_time(String url, String user, String password) {
  
        // 创建界面
        JFrame frame = new JFrame("Inquiry Activity");
        JLabel label = new JLabel("输入日期 (yyyy-MM-dd):");
        JTextField dateField = new JTextField(10);
        JButton queryButton = new JButton("Query");

        // 设置布局
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(dateField);
        panel.add(queryButton);

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // 添加查询按钮的监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateInput = dateField.getText();
                try {
                    Date dateVariable = Date.valueOf(dateInput); // 转换用户输入为 java.sql.Date
                    q_activity_time(url, user, password, dateVariable);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "日期格式无效，请使用 yyyy-MM-dd 格式。", "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}