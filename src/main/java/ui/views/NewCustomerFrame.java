package main.java.ui.views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.bl.CustomerService;
import main.java.pojo.Customer;
import main.java.ui.StackLayout;

public class NewCustomerFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private CustomerService customerService = new CustomerService();

    private JTextField jtfIdCard = new JTextField(15);
    private JTextField jtfName = new JTextField(15);
    private JTextField jtfPhoneNum = new JTextField(15);
    private JButton jbtnCancel = new JButton("Cancel");
    private JButton jbtnOK = new JButton("OK");

    public NewCustomerFrame()
    {
        // Components
        JPanel jpIdCard = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpName = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpPhoneNum = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 2, false));
        JPanel jpBottom = new JPanel(new BorderLayout());
        JPanel jpBtns = new JPanel(new StackLayout(StackLayout.HORIZONTAL, 10));
        JPanel jpCenter = new JPanel(new StackLayout());

        // Initialize

        // Actions
        jbtnCancel.addActionListener(e -> {
            dispose();
        });
        jbtnOK.addActionListener(e -> {
            String idCard = jtfIdCard.getText().trim();
            String name = jtfName.getText().trim();
            String phoneNum = jtfPhoneNum.getText().trim();
            if ("".equals(idCard) || "".equals(name))
            {
                JOptionPane.showMessageDialog(null, "Required field", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else
            {
                if (customerService.isCustomerExisting(idCard))
                {
                    JOptionPane.showMessageDialog(null, "Already exists",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else
                {
                    if (customerService
                            .addCustomer(new Customer(idCard, name, phoneNum)))
                    {
                        JOptionPane.showMessageDialog(null, "Customer added");
                        dispose();
                    } else
                    {
                        JOptionPane.showMessageDialog(null, "Failed", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Layout
        jpIdCard.add(new JLabel("Id Card"));
        jpIdCard.add(jtfIdCard);
        jpName.add(new JLabel("Name"));
        jpName.add(jtfName);
        jpPhoneNum.add(new JLabel("Phone number"));
        jpPhoneNum.add(jtfPhoneNum);
        jpBtns.add(jbtnCancel);
        jpBtns.add(jbtnOK);
        jpBottom.add(jpBtns, BorderLayout.EAST);

        jpCenter.add(jpIdCard);
        jpCenter.add(jpName);
        jpCenter.add(jpPhoneNum);

        add(jpCenter, BorderLayout.CENTER);
        add(jpBottom, BorderLayout.SOUTH);

        setTitle("New customer");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
