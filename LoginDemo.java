import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginDemo extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JLabel statusLabel;

    public LoginDemo() {
        // 设置窗口标题
        setTitle("登录界面");

        // 设置窗口大小
        setSize(300, 150);

        // 设置关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建面板
        JPanel panel = new JPanel();

        // 添加面板到窗口
        add(panel);

        // 调用用户定义的方法来添加组件到面板
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // 创建用户标签
        JLabel userLabel = new JLabel("用户:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        // 创建文本域用于用户输入
        userField = new JTextField(20);
        userField.setBounds(100, 20, 165, 25);
        panel.add(userField);

        // 创建密码标签
        JLabel passLabel = new JLabel("密码:");
        passLabel.setBounds(10, 50, 80, 25);
        panel.add(passLabel);

        // 创建密码输入域
        passField = new JPasswordField(20);
        passField.setBounds(100, 50, 165, 25);
        panel.add(passField);

        // 创建登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        // 创建注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(100, 80, 80, 25);
        panel.add(registerButton);

        // 创建状态标签
        statusLabel = new JLabel("");
        statusLabel.setBounds(10, 110, 200, 25);
        panel.add(statusLabel);

        // 添加登录按钮点击事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userField.getText();
                String password = new String(passField.getPassword());

                if (authenticate(user, password)) {
                    // 成功登录
                    statusLabel.setText("登录成功");
                    // 跳转到新页面
                    openNewPage();
                } else {
                    // 登录失败
                    statusLabel.setText("登录失败");
                }
            }
        });

        // 添加注册按钮点击事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterPage();
            }
        });
    }

    private boolean authenticate(String user, String password) {
        // 在这里进行账号密码的验证，这里只是一个简单示例
        // 可以根据实际情况替换为数据库验证或其他方式
        return "admin".equals(user) && "password".equals(password);
    }

    private void openNewPage() {
        // 创建新的窗口
        JFrame newFrame = new JFrame("Welcome");
        newFrame.setSize(400, 200);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建新面板
        JPanel newPanel = new JPanel();
        newFrame.add(newPanel);

        // 添加欢迎标签
        JLabel welcomeLabel = new JLabel("登录成功");
        newPanel.add(welcomeLabel);

        // 设置新窗口可见
        newFrame.setVisible(true);

        // 隐藏当前窗口
        this.setVisible(false);
    }

    private void openRegisterPage() {
        // 创建新的注册窗口
        JFrame registerFrame = new JFrame("注册");
        registerFrame.setSize(300, 200);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 创建新面板
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerFrame.add(registerPanel);

        // 创建注册页面的组件
        JLabel userLabel = new JLabel("新用户:");
        userLabel.setBounds(10, 20, 80, 25);
        registerPanel.add(userLabel);

        JTextField newUserField = new JTextField(20);
        newUserField.setBounds(100, 20, 165, 25);
        registerPanel.add(newUserField);

        JLabel passLabel = new JLabel("新密码:");
        passLabel.setBounds(10, 50, 80, 25);
        registerPanel.add(passLabel);

        JPasswordField newPassField = new JPasswordField(20);
        newPassField.setBounds(100, 50, 165, 25);
        registerPanel.add(newPassField);

        JButton registerButton = new JButton("注册");
        registerButton.setBounds(10, 80, 80, 25);
        registerPanel.add(registerButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(10, 110, 200, 25);
        registerPanel.add(statusLabel);

        // 添加注册按钮点击事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 注册逻辑
                String newUser = newUserField.getText();
                String newPassword = new String(newPassField.getPassword());

                if (registerUser(newUser, newPassword)) {
                    // 显示对话框提示用户注册成功
                    JOptionPane.showMessageDialog(registerFrame, "注册成功，请返回登录界面", "提示", JOptionPane.INFORMATION_MESSAGE);

                    // 关闭注册窗口并重新显示登录窗口
                    registerFrame.dispose();
                    LoginDemo.this.setVisible(true);
                } else {
                    // 注册失败
                    statusLabel.setText("注册失败，请重试");
                }
            }
        });

        // 设置新窗口可见
        registerFrame.setVisible(true);

        // 隐藏当前窗口
        this.setVisible(false);
    }

    private boolean registerUser(String user, String password) {
        // 使用Amazon Aurora的连接信息
        String jdbcUrl = "jdbc:mysql://your-aurora-endpoint.amazonaws.com:3306/user";
        String jdbcUser = "user"; // 替换为你的数据库用户名
        String jdbcPassword = "123"; // 替换为你的数据库密码

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 创建登录窗口
        LoginDemo loginDemo = new LoginDemo();
        loginDemo.setVisible(true);
    }
}
