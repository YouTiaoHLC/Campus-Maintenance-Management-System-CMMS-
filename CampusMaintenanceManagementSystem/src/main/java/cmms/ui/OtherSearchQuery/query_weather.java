package main.java.cmms.ui.OtherSearchQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class query_weather {
    public static void q_weather(String url, String user, String password, String id) {
        String sql = "SELECT b.WEA_Type, b.WEA_Level, b.EPID, io. MMID, m.MMName, i. BWID, bw.BWName, ep.Plan"+
       "FROM BAD_WEATHER b"+
        "JOIN IMPLEMENT_ORGANIZE io ON b.EPID=io.EPID"+
        "JOIN MID_MANAGER m ON m.MMID=io.MMID"+
        "JOIN IMPLEMENT i ON b.EPID=i.EPID"+
        "JOIN BASE_WORKER bw ON i.BWID=bw.BWID"+
        "JOIN EMERGENCY_PLAN ep ON b.EPID=ep.EPID"+
        "WHERE b.WEA_Type=?;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);  // MMID

            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    String weaType = rs.getString("WEA_Type");
                    String weaLevel = rs.getString("WEA_Level");
                    String epId = rs.getString("EPID");
                    String mmid = rs.getString("MMID");
                    String mmName = rs.getString("MMName");
                    String bwId = rs.getString("BWID");
                    String bwName = rs.getString("BWName");
                    String plan = rs.getString("Plan");

                    result.append(String.format("WEA_Type: %s, WEA_Level: %s, EPID: %d, MMID: %d, MMName: %s, BWID: %d, BWName: %s, Plan: %s%n",
                            weaType, weaLevel, epId, mmid, mmName, bwId, bwName, plan));
                }
                JOptionPane.showMessageDialog(null, result.toString(), "查询结果", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        }

//整合时 用下面这个方法
    public static void q_weather(String url, String user, String password) {
  
        // 创建界面
        JFrame frame = new JFrame("Inquiry Activity");
        JLabel label = new JLabel("输入bad weather type:");
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
                    q_weather(url, user, password, dateInput);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "weather格式无效", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
