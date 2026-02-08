package main.java.cmms;

import main.java.cmms.ui.MainFrame;
import javax.swing.*;

public class Main {
    /*
    // 打印所有可用的外观风格（帮助你直观查看，保留这个方法很有用）
    private static void printAvailableLookAndFeels() {
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        System.out.println("=== List of supported appearance styles ===");
        for (UIManager.LookAndFeelInfo look : looks) {
            System.out.println("Appearance Name: " + look.getName());
            System.out.println("Corresponding class name: " + look.getClassName());
            // 标记出“系统外观”（方便你对照）
            if (look.getClassName().equals(UIManager.getSystemLookAndFeelClassName())) {
                System.out.println("(This is the default appearance of the current system)");
            }
            System.out.println("---");
        }
    }
    */

    public static void main(String[] args) {
        // 打印可用外观（可选，方便调试）
        //printAvailableLookAndFeels();

        // 正确设置“系统外观”（核心修正）
        try {
            // 1. 获取系统外观的全类名（这是你之前想实现的核心）
            String systemLaf = UIManager.getSystemLookAndFeelClassName();
            // 2. 应用系统外观
            UIManager.setLookAndFeel(systemLaf);
            //System.out.println("✅ Successful application system appearance：" + UIManager.getLookAndFeel().getName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // 捕获所有可能的异常（比笼统的 Exception 更精准）
            //System.out.println("❌ Unable to set system appearance, switch to cross-platform default appearance");
            e.printStackTrace(); // 打印异常详情（方便调试，发布时可注释）
            /*
            // 降级方案：使用 Java 跨平台默认外观（确保界面能正常显示）
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                System.out.println("✅ Cross-platform default appearance is enabled：" + UIManager.getLookAndFeel().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            */
        }

        // 规范：在 EDT 线程中创建和显示界面
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            // 建议添加：设置窗口关闭规则（避免程序后台运行）
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // 建议添加：让窗口居中显示（更美观）
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }
}