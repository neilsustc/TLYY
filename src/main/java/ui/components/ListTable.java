package main.java.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.java.ui.StackLayout;

public class ListTable extends JPanel
{
    private static final long serialVersionUID = 1L;

    public final static int DISABLE = 1;
    public final static int WARNING = 2;
    private Color warnBgColor = new Color(249, 204, 226);
    private Color borderColor = new Color(210, 210, 210);
    private Color headerColor = new Color(213, 234, 255);

    private String hGap = String.format("%-8s", "");
    private HashMap<Integer, String> regexColFilters = new HashMap<>();
    private HashMap<Integer, String> warningColFilters = new HashMap<>();
    private HashSet<Integer> warnRows = new HashSet<>();
    private List<JPanel> jpColumns = new ArrayList<>();
    private List<JLabel> jlbColumnNames = new ArrayList<>();
    private List<DefaultListModel<String>> listModels = new ArrayList<>();
    private List<JList<String>> lists = new ArrayList<>();
    private List<String[]> data = new ArrayList<>();
    private List<String[]> currentViewData = new ArrayList<>();

    HighlightCellRenderer cellRenderer = new HighlightCellRenderer();
    SyncSelectionListener syncListener = new SyncSelectionListener();

    public ListTable(String[] columnNames)
    {
        for (String string : columnNames)
        {
            JLabel jlbColumnName = new JLabel(string + hGap); // Label
            Dimension preferredSize = jlbColumnName.getPreferredSize();
            jlbColumnName.setPreferredSize(new Dimension(preferredSize.width,
                    (int) (preferredSize.height * 1.2)));
            jlbColumnName.setBackground(headerColor);
            jlbColumnName.setOpaque(true);
            jlbColumnNames.add(jlbColumnName);

            DefaultListModel<String> columnModel = new DefaultListModel<>(); // ListModel
            listModels.add(columnModel);

            JList<String> column = new JList<String>(columnModel); // List
            column.setCellRenderer(cellRenderer);
            column.addListSelectionListener(syncListener);
            lists.add(column);

            JPanel jpColumn = new JPanel(new StackLayout(StackLayout.VERTICAL,
                    0, false, false, true));
            jpColumn.add(jlbColumnName);
            jpColumn.add(column);
            jpColumns.add(jpColumn);

            add(jpColumn);
        }

        setLayout(new StackLayout(StackLayout.HORIZONTAL, 0));
        setBorder(new LineBorder(borderColor));
    }

    public void setData(List<String[]> data)
    {
        this.data = data;
        updateModelUsingData();
    }

    public void addRow(String[] row)
    {
        data.add(row);
        updateModelUsingData();
    }

    /**
     * Match substring or regular expression
     * 
     * @param column
     * @param regex
     */
    public void addColumnRegexFilter(int column, String regex)
    {
        regexColFilters.put(column, regex);
        updateModelUsingData();
    }

    public void addColumnStyleFilter(int column, String regex, int type)
    {
        switch (type)
        {
        case WARNING:
            warningColFilters.put(column, regex);
            break;
        }
        updateModelUsingData();
    }

    public int getColumnCount()
    {
        return lists.size();
    }

    public int getRowCount()
    {
        return listModels.get(0).getSize();
    }

    public String[] getSelectedRow()
    {
        int selectedIndex = lists.get(0).getSelectedIndex();
        return currentViewData.get(selectedIndex);
    }

    private void updateModelUsingData()
    {
        /* Clear */
        for (DefaultListModel<String> model : listModels)
        {
            model.clear();
        }
        warnRows.clear();
        /* Regex Filter */
        ArrayList<String[]> viewData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
        {
            String[] row = data.get(i);
            if (regexColFilters.size() == 0)
            {
                viewData.add(row);
            } else
            {
                for (Entry<Integer, String> entry : regexColFilters.entrySet())
                {
                    if (hasOrMatch(row[entry.getKey()], entry.getValue()))
                    {
                        viewData.add(row);
                        break; // Don't add the same row more than 1 time !!!
                    }
                }
            }
        }
        /* Row Style Filter */
        for (int i = 0; i < viewData.size(); i++)
        {
            String[] row = viewData.get(i);
            /* Add to model */
            for (int j = 0; j < listModels.size(); j++)
            {
                listModels.get(j).addElement(row[j] + hGap);
            }
            for (Entry<Integer, String> entry : warningColFilters.entrySet())
            {
                if (hasOrMatch(row[entry.getKey()], entry.getValue()))
                {
                    warnRows.add(i);
                }
            }
        }
        currentViewData = viewData;
    }

    private boolean hasOrMatch(String parent, String matcher)
    {
        parent = parent.toLowerCase();
        matcher = matcher.toLowerCase();
        return parent.contains(matcher) || parent.matches(matcher);
    }

    class SyncSelectionListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
            JList<?> jlst = (JList<?>) e.getSource();
            int selectedIndex = jlst.getSelectedIndex();
            for (JList<String> list : lists)
            {
                list.setSelectedIndex(selectedIndex);
            }
        }
    }

    class HighlightCellRenderer extends DefaultListCellRenderer
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus)
        {
            Component comp = super.getListCellRendererComponent(list, value,
                    index, isSelected, cellHasFocus);
            if (!isSelected)
            {
                if (warnRows.contains(index))
                {
                    comp.setBackground(warnBgColor);
                }
            }
            return comp;
        }
    }

    public void clearFilter()
    {
        regexColFilters.clear();
    }
}
