package main.java.ui.views;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        JScrollPane jscpTb = new JScrollPane(tbCustomer);
        JPanel jpBtns = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 10, false));

        // Initialize
        tbCustomer.setData(customerService.findAllCustemersInArray());
        jscpTb.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Actions
        jtfFilter.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                String regex = jtfFilter.getText().trim();
                setFilter(tbCustomer, regex);
            }
        });
        jbtnAdd.addActionListener(e ->
        {
            JFrame newCustomer = new NewCustomerFrame();
            newCustomer.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    tbCustomer
                            .setData(customerService.findAllCustemersInArray());
                    pack();
                    super.windowClosed(e);
                }
            });
        });
        jbtnDelete.addActionListener(e ->
        {
            if (tbCustomer.getSelectedRow() != null)
            {
                customerService.deleteCustomer(tbCustomer.getSelectedRow()[0]);
                tbCustomer.setData(customerService.findAllCustemersInArray());
            } else
                JOptionPane.showMessageDialog(null, "No customer is selected",
                        "Error", JOptionPane.ERROR_MESSAGE);
        });

        // Layout
        jpFilter.add(jlbFilter);
        jpFilter.add(jtfFilter);
        jpBtns.add(jbtnAdd);
        jpBtns.add(jbtnDelete);

        add(jpFilter);
        add(jscpTb);
        add(jpBtns);

        setTitle("Customer management");
        setLayout(new StackLayout());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setFilter(ListTable table, String regex)
    {
        table.clearFilter();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++)
        {
            table.addColumnRegexFilter(i, regex);
        }
    }
}
