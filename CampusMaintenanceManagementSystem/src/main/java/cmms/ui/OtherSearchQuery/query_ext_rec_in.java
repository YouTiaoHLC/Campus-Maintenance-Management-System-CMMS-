package main.java.cmms.ui.OtherSearchQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


//查询外包公司调用记录（有输入）
public class query_ext_rec_in {
    public static void q_ext_rec_in(String url, String user, String password,int input) {
        String sql = "SELECT c.EOID, c.EXCOMPID" +
                "FROM COMPANY c" +
                "WHERE c.EXCOMPID = ?;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, input);

            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    int eoid = rs.getInt("EOID");
                    int excompid = rs.getInt("EXCOMPID");
                    result.append("EOID: ").append(eoid).append(", ID of external company: ").append(excompid).append("\n");
                }
                JOptionPane.showMessageDialog(null, result.toString(), "查询结果", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //整合时 用下面这个方法
    public static void q_ext_rec_in(String url, String user, String password) {

        // 创建界面
        JFrame frame = new JFrame("Inquiry Activity");
        JLabel label = new JLabel("Input data (ExtCompany id):");
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
                    int dateVariable = Integer.parseInt(dateInput);
                    q_ext_rec_in(url, user, password, dateVariable);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "日期格式无效，请使用 yyyy-MM-dd 格式。", "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}