package main.java.cmms.ui.OtherSearchQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class query_manage {
    public static void q_manage(String url, String user, String password, String id) {
        String sql = "SELECT m.MMID, m.MMName, e.EOID, e.EOName, b.BWID, b.BWName " +
                "FROM MID_MANAGER m " +
                "JOIN MID_TOBASE_MANAGE r ON m.MMID = r.MMID " +
                "JOIN BASE_WORKER b ON r.BWID = b.BWID " +
                "JOIN EXECUTE_OFFICER e ON m.EOID = e.EOID " +
                "WHERE m.MMID = ?;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);  // MMID
            pstmt.setString(2, id);  // EOID
            pstmt.setString(3, id);  // BWID

            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    int mmid = rs.getInt("MMID");
                    String mmName = rs.getString("MMName");
                    int eoId = rs.getInt("EOID");
                    String eoName = rs.getString("EOName");
                    int bwId = rs.getInt("BWID");
                    String bwName = rs.getString("BWName");

                    result.append("MMID: ").append(mmid)
                            .append(", MMName: ").append(mmName)
                            .append(", EOID: ").append(eoId)
                            .append(", EOName: ").append(eoName)
                            .append(", BWID: ").append(bwId)
                            .append(", BWName: ").append(bwName)
                            .append("\n");
                }
                JOptionPane.showMessageDialog(null, result.toString(), "查询结果", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //整合时 用下面这个方法
    public static void q_manage(String url, String user, String password) {

        // 创建界面
        JFrame frame = new JFrame("Inquiry Activity");
        JLabel label = new JLabel("输入员工/管理人员id:");
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
                    q_manage(url, user, password, dateInput);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "id格式无效", "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
