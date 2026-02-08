package main.java.cmms.ui.OtherSearchQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import main.java.cmms.database.*;

public class query_bw_act {
    public static void q_bw_act(String id) {
        String sql = """
                SELECT b.BWID, b.BWName, a.LOCID, a.Activity_date, a.Activity_time, a.ACT_Type, a.Chemical_product
                FROM BASE_WORKER b
                JOIN ACTIVITY a ON b.BWID=a.BWID
                WHERE b.BWID=?;""";

        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);  // MMID

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // 创建列名
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }

                // 创建数据
                Vector<String> data = new Vector<>();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(String.valueOf(row));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //整合时 用下面这个方法
    public static void q_bw_act() {

        // 创建界面
        JFrame frame = new JFrame("Inquiry Activity");
        JLabel label = new JLabel("Input id of base worker:");
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
                    q_bw_act(dateInput);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Date Input: " + dateInput);
                    JOptionPane.showMessageDialog(frame, "格式无效", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
