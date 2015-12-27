package main.java.ui.views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.bl.CustomerService;
import main.java.ui.StackLayout;
import main.java.ui.components.ListTable;

public class CustomerMgmtFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private CustomerService customerService = new CustomerService();

    private JLabel jlbFilter = new JLabel("Filter");
    private JTextField jtfFilter = new JTextField(15);
    private ListTable tbCustomer = new ListTable(
            new String[] { "IdCard", "Name", "PhoneNum" });
    private JButton jbtnAdd = new JButton("Add");
    private JButton jbtnDelete = new JButton("Delete");

    public CustomerMgmtFrame()
    {
        // Panels
        JPanel jpFilter = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 5, false, true));
        JPanel jpBtns = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 10, false));

        // Initialize
        tbCustomer.setData(customerService.findAllCustemers());

        // Actions
        jbtnAdd.addActionListener(e -> {
            JFrame newCustomer = new NewCustomerFrame();
            newCustomer.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    tbCustomer.setData(customerService.findAllCustemers());
                    super.windowClosing(e);
                }
            });
        });

        // Layout
        jpFilter.add(jlbFilter);
        jpFilter.add(jtfFilter);
        jpBtns.add(jbtnAdd);
        jpBtns.add(jbtnDelete);

        add(jpFilter);
        add(tbCustomer);
        add(jpBtns);

        setTitle("Customer management");
        setLayout(new StackLayout());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
