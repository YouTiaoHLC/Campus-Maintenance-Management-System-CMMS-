package main.java.cmms.ui.OtherSearchQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//查询外包公司调用记录（无输入）
public class query_ext_rec {
    public static void q_ext_rec(String url, String user, String password) {
        // 创建界面
        JFrame frame = new JFrame("Inquiry Requests to External Company");
        JButton queryButton = new JButton("Queue");

        // 设置布局
        JPanel panel = new JPanel();
        panel.add(queryButton);

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // 添加查询按钮的监听器
        queryButton.addActionListener(new MyActionListener(url, user, password, frame));
    }

    private static class MyActionListener implements ActionListener {
        private final String url;
        private final String user;
        private final String password;
        private final JFrame frame;

        public MyActionListener(String url, String user, String password, JFrame frame) {
            this.url = url;
            this.user = user;
            this.password = password;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String sql = """
                    SELECT COURSEID, Dayofweek,Timeslot,LOCID
                    FROM COURSE
                    WHERE LOCID=?,Dayofweek=?,Timeslot=?;
                    """;
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                try (ResultSet rs = pstmt.executeQuery()) {
                    StringBuilder result = new StringBuilder();
                    while (rs.next()) {
                        //type here
                        int eoid = rs.getInt("EOID");
                        int excompid = rs.getInt("EXCOMPID");
                        result.append("EOID: ").append(eoid).append(", ID of external company: ").append(excompid).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, result.toString(), "查询结果", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
}
