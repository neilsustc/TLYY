package main.java.ui.views;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.java.ui.StackLayout;
import main.java.ui.components.ListTable;

public class MainFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private boolean updateTime = true;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "'<html><font color=\"red\">'HH:mm:ss'</font>&nbsp;&nbsp;'"
                    + "yyyy/MM/dd'&nbsp;&nbsp;'EEEEEEEEE'</html>'");
    private JLabel jlbTime = new JLabel();
    private JLabel jlbFilter = new JLabel("Filter");
    private JTextField jtfFilter = new JTextField(15);
    private ListTable tbRoom = new ListTable(
            new String[] { "RoomNum", "Status", "RoomType", "Level" });
    private ListTable tbPrice = new ListTable(
            new String[] { "RoomType", "Level", "Price" });
    private ListTable tbCustomer = new ListTable(
            new String[] { "IdCard", "Name", "PhoneNum" });
    private JTextField jtfUserSearch = new JTextField(15);
    private JButton jbtnCheckIn = new JButton("Check in");
    private JButton jbtnCheckOut = new JButton("Check out");
    private JLabel jlbStatistics = new JLabel("Money: ...");
    private JButton jbtnStatistics = new JButton("Statistics");
    private JButton jbtnChangePass = new JButton("Change password");
    private JButton jbtnUserMgmt = new JButton("User management");

    public MainFrame(String staffNum)
    {
        // Components
        JPanel jpTop = new JPanel(new BorderLayout());
        JPanel jpCenter = new JPanel(new StackLayout(StackLayout.VERTICAL, 10));
        JPanel jpFilter = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 5, false, true));
        JPanel jpTable = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 10, false));
        JPanel jpBottom = new JPanel(
                new StackLayout(StackLayout.HORIZONTAL, 10, false));
        JPanel jpSearch = new JPanel(new StackLayout(StackLayout.VERTICAL, 10));
        JPanel jpUserSearch = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 5, false));
        JLabel jlbUserSearch = new JLabel("User");
        JPanel jpAction = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 10, true, false, true));
        JPanel jpStatistics = new JPanel(new StackLayout());
        JPanel jpUserMgmt = new JPanel(
                new StackLayout(StackLayout.VERTICAL, 10, true, false, true));

        // Initialize
        tbRoom.addColumnStyleFilter(1, "Full", ListTable.WARNING);
        tbRoom.addRow(new String[] { "A101", "Empty", "Single", "Economic" });
        tbRoom.addRow(new String[] { "A102", "Full", "Single", "Standard" });
        tbRoom.addRow(new String[] { "A103", "Empty", "Triple", "Standard" });
        tbRoom.addRow(new String[] { "B101", "Empty", "Double", "Deluxe" });
        tbRoom.addRow(new String[] { "B102", "Empty", "Single", "Business" });
        jpSearch.setBorder(new TitledBorder(" Search "));
        jpAction.setBorder(new TitledBorder(" Action "));
        jpStatistics.setBorder(new TitledBorder(" Statistics "));
        jpUserMgmt.setBorder(new TitledBorder(" Account "));

        // Actions
        new Thread(() ->
        {
            while (updateTime)
            {
                jlbTime.setText(dateFormat.format(new Date()));
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        jtfFilter.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                String regex = jtfFilter.getText().trim();
                setFilter(tbCustomer, regex);
            }
        });
        tbCustomer.addRowSelectionListener(e ->
        {
            String selected = tbCustomer.getSelectedRow()[0];
            System.out.println(selected);
        });
        jbtnChangePass.addActionListener(e -> new ChangePassFrame(staffNum));
        jbtnUserMgmt.addActionListener(e -> new CustomerMgmtFrame());
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosing(e);
                updateTime = false; // Stop thread
            }
        });

        // Layout
        jpFilter.add(jlbFilter);
        jpFilter.add(jtfFilter);
        jpTable.add(tbRoom);
        jpTable.add(tbPrice);
        jpTable.add(tbCustomer);
        jpUserSearch.add(jlbUserSearch);
        jpUserSearch.add(jtfUserSearch);
        jpStatistics.add(jlbStatistics);
        jpStatistics.add(jbtnStatistics);

        jpAction.add(jbtnCheckIn);
        jpAction.add(jbtnCheckOut);
        jpSearch.add(jpUserSearch);
        jpUserMgmt.add(jbtnChangePass);
        jpUserMgmt.add(jbtnUserMgmt);

        jpTop.add(jlbTime);
        jpCenter.add(jpFilter, BorderLayout.NORTH);
        jpCenter.add(jpTable, BorderLayout.CENTER);
        jpBottom.add(jpSearch);
        jpBottom.add(jpAction);
        jpBottom.add(jpStatistics);
        jpBottom.add(jpUserMgmt);

        add(jpTop);
        add(jpCenter);
        add(jpBottom);

        setTitle("Welcome " + staffNum);
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
