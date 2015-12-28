package main.java.ui.views;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import main.java.bl.AdminService;
import main.java.ui.StackLayout;

public class ChangePassFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private AdminService adminService = new AdminService();

    private JPasswordField jpfOldPass = new JPasswordField(15);
    private JPasswordField jpfNewPass = new JPasswordField(15);
    private JPasswordField jpfConfirm = new JPasswordField(15);
    private JButton jbtnCancel = new JButton("Cancel");
    private JButton jbtnOK = new JButton("OK");

    public ChangePassFrame(String staffNum)
    {
        // Components
        JPanel jpOldPass = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpNewPass = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpComfirm = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpBottom = new JPanel(new BorderLayout());
        JPanel jpBtns = new JPanel(new StackLayout(StackLayout.HORIZONTAL, 10));
        JPanel jpCenter = new JPanel(new StackLayout());

        // Initialize
        jpfOldPass.setEchoChar('*');
        jpfNewPass.setEchoChar('*');
        jpfConfirm.setEchoChar('*');
        jbtnOK.setEnabled(false);

        // Actions
        NotEmptyKeyListener listener = new NotEmptyKeyListener();
        jpfOldPass.addKeyListener(listener);
        jpfNewPass.addKeyListener(listener);
        jpfConfirm.addKeyListener(listener);
        jbtnCancel.addActionListener(e -> dispose());
        jbtnOK.addActionListener(e ->
        {
            String oldPass = new String(jpfOldPass.getPassword()).trim();
            String newPass = new String(jpfNewPass.getPassword()).trim();
            if (adminService.checkPassword(staffNum, oldPass))
            {
                if (adminService.changePass(staffNum, newPass))
                {
                    JOptionPane.showMessageDialog(null, "Password changed.");
                    dispose();
                } else
                {
                    JOptionPane.showMessageDialog(null, "Failed.");
                }
            } else
            {
                JOptionPane.showMessageDialog(null, "Incorrect password");
            }
        });

        // Layout
        jpOldPass.add(new JLabel("Old password"));
        jpOldPass.add(jpfOldPass);
        jpNewPass.add(new JLabel("New password"));
        jpNewPass.add(jpfNewPass);
        jpComfirm.add(new JLabel("Confirm"));
        jpComfirm.add(jpfConfirm);
        jpBtns.add(jbtnCancel);
        jpBtns.add(jbtnOK);
        jpBottom.add(jpBtns, BorderLayout.EAST);

        jpCenter.add(jpOldPass);
        jpCenter.add(jpNewPass);
        jpCenter.add(jpComfirm);

        add(jpCenter, BorderLayout.CENTER);
        add(jpBottom, BorderLayout.SOUTH);

        setTitle("Change password");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    class NotEmptyKeyListener extends KeyAdapter
    {
        @Override
        public void keyReleased(KeyEvent e)
        {
            String oldPass = new String(jpfOldPass.getPassword()).trim();
            String newPass = new String(jpfNewPass.getPassword()).trim();
            String confrim = new String(jpfConfirm.getPassword()).trim();
            if ("".equals(oldPass) || "".equals(newPass)
                    || !newPass.equals(confrim))
                jbtnOK.setEnabled(false);
            else
                jbtnOK.setEnabled(true);
        }
    }
}
