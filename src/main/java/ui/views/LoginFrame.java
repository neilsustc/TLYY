package main.java.ui.views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.java.bl.AdminService;
import main.java.ui.StackLayout;

public class LoginFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private AdminService adminService = new AdminService();

    JLabel jlbBanner = new JLabel("TangLang YaYuan");
    JTextField jtfStaffNum = new JTextField(20);
    JPasswordField jpfPass = new JPasswordField(20);
    JButton jbtnLogin = new JButton("Login");
    JButton jbtnExit = new JButton("Exit");

    public LoginFrame()
    {
        // Components
        JPanel jpBorderCenter = new JPanel(new StackLayout());
        JLabel jlbAccount = new JLabel("Staff number");
        JLabel jlbPass = new JLabel("Password");
        JPanel jpAccount = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpPass = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpBorderSouth = new JPanel(new BorderLayout());
        JPanel jpBtns = new JPanel(new StackLayout(StackLayout.HORIZONTAL, 10));

        // Initialize
        jpfPass.setEchoChar('*');

        // Actions
        jbtnLogin.addActionListener(e -> {
            String staffNum = jtfStaffNum.getText().trim();
            String pass = new String(jpfPass.getPassword()).trim();
            if ("".equals(staffNum) || "".equals(pass))
            {
                JOptionPane.showMessageDialog(null, "Required fields", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (adminService.checkPassword(staffNum, pass))
            {
                new MainFrame(staffNum);
                disposeThis();
            } else
            {
                JOptionPane.showMessageDialog(null, "Failed", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        jbtnExit.addActionListener(e -> {
            disposeThis();
        });

        // Layout
        jpAccount.add(jlbAccount);
        jpAccount.add(jtfStaffNum);
        jpPass.add(jlbPass);
        jpPass.add(jpfPass);
        jpBtns.add(jbtnExit);
        jpBtns.add(jbtnLogin);

        jpBorderCenter.add(jlbBanner);
        jpBorderCenter.add(jpAccount);
        jpBorderCenter.add(jpPass);
        jpBorderSouth.add(jpBtns, BorderLayout.EAST);

        add(jpBorderCenter, BorderLayout.CENTER);
        add(jpBorderSouth, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void disposeThis()
    {
        this.dispose();
    }
}
