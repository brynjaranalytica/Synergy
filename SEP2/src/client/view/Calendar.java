package client.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import client.controller.ClientController;
import shared.business_entities.Memo;
import utility.MyDate;
import utility.Utilities;

public class Calendar extends AbstractJIF {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panelTable;
    private ArrayList<LocalDate> dates;
    private LocalDate thisDate;
    private JLabel lblMonthYear;
    private Object[] rowData;
    private JTextField textField;
    private DefaultTableModel tableModel;
    private AbstractButton btnNxtMonth;
    private AbstractButton btnPrevMonth;
    private JTable table;
    private JLabel lblCalendar;
    private LocalDate pickedDate;
    private JPanel monthPanel;
    private Font font;
    private JLabel lblMon;
    private JLabel lblTue;
    private JLabel lblWed;
    private JLabel lblThu;
    private JLabel lblFri;
    private JLabel lblSat;
    private JLabel lblSun;
    private JLabel lblSelectedDateDate;
    private JTextPane textPane;
    private JButton btnSave;
    private JButton btnDelete;
    private JLabel lblMemo;

    public Calendar() {
        setBounds(900, 20, 620, 430);
        initComponents();
        createEvents();
    }

    @Override
    public void loadData(Object object) {
        String selectedDateString = textField.getText();
        if (selectedDateString == null || selectedDateString.equals(""))
            return;

        shared.business_entities.Calendar calendar = (shared.business_entities.Calendar) object;
        Date selectedDate = Utilities.parseDate(selectedDateString);
        Memo memo = calendar.getMemo(selectedDate);

        if (memo == null) {
            textPane.setText("");
            return;
        }

        textPane.setText(memo.getDescription());
    }

    public void initComponents() {

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblCalendar = new JLabel("Calendar");
        lblCalendar.setForeground(Color.GRAY);
        lblCalendar.setFont(new Font("Raleway", Font.PLAIN, 30));
        lblCalendar.setBounds(10, 11, 200, 27);
        getContentPane().add(lblCalendar);

        panelTable = new JPanel();
        panelTable = getMonthPanel(MyDate.getCurrentMonthDates());

        panelTable.setBounds(0, 27, 600, 400);
        contentPane.add(panelTable);
    }

    public void createEvents() {

        btnNxtMonth.addActionListener(e -> {
            loadNewTableData(MyDate.getNextMonthDates(thisDate));
        });
        btnPrevMonth.addActionListener(e -> {
            loadNewTableData(MyDate.getPreviousMonthDates(thisDate));
        });

        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String selectedDateString = textField.getText();
                if (selectedDateString == null || selectedDateString.equals("") || textPane.getText().equals("") || textPane.getText() == null)
                    return;

                Date selectedDate = Utilities.parseDate(selectedDateString);
                ClientController.getInstance().removeMemo(Root.currentProjectName, selectedDate);
            }
        });

        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String selectedDateString = textField.getText();
                if (selectedDateString == null || selectedDateString.equals("") || textPane.getText().equals("") || textPane.getText() == null)
                    return;

                Date selectedDate = Utilities.parseDate(selectedDateString);
                ClientController.getInstance().addMemo(Root.currentProjectName, selectedDate, textPane.getText());
            }
        });

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    int dayPicked = (int) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                    pickedDate = LocalDate.of(thisDate.getYear(), thisDate.getMonth(), dayPicked);

                    ///////////////////////
                    textField.setText("" + pickedDate);

                    /////////////////////////////
                    loadData(ClientController.getInstance().getProjectFromModel(Root.currentProjectName).getCalendar());
                } catch (Exception e) {
                }
            }
        });
    }

    public JPanel getMonthPanel(ArrayList<LocalDate> datesArray) {
        dates = datesArray;
        monthPanel = new JPanel();
        font = new Font("Raleway", Font.PLAIN, 10);
        thisDate = dates.get(0);

        table = new JTable();
        table.setGridColor(SystemColor.text);
        table.setForeground(SystemColor.text);
        table.setBackground(SystemColor.textHighlight);
        table.setRowHeight(50);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMon = new JLabel("Mon");
        lblTue = new JLabel("Tue");
        lblWed = new JLabel("Wed");
        lblThu = new JLabel("Thu");
        lblFri = new JLabel("Fri");
        lblSat = new JLabel("Sat");
        lblSun = new JLabel("Sun");
        lblMonthYear = new JLabel("Month Year");
        lblMonthYear.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnNxtMonth = new JButton(">");
        btnPrevMonth = new JButton("<");
        rowData = new Object[7];

        table.setBounds(10, 63, 378, 249);
        table.setIntercellSpacing(new Dimension(2, 2));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowSelectionAllowed(false);
        table.setModel(new DefaultTableModel(0, 7));

        lblMon.setFont(font);
        lblTue.setFont(font);
        lblWed.setFont(font);
        lblThu.setFont(font);
        lblFri.setFont(font);
        lblSat.setFont(font);
        lblSun.setFont(font);

        lblMon.setBounds(25, 43, 22, 14);
        lblTue.setBounds(77, 43, 18, 14);
        lblWed.setBounds(131, 43, 22, 14);
        lblThu.setBounds(187, 43, 18, 14);
        lblFri.setBounds(242, 43, 12, 14);
        lblSat.setBounds(297, 43, 16, 14);
        lblSun.setBounds(354, 43, 18, 14);
        btnNxtMonth.setBounds(298, 323, 25, 19);
        btnPrevMonth.setBounds(84, 323, 25, 19);
        lblMonthYear.setBounds(138, 323, 132, 14);

        lblMonthYear.setHorizontalAlignment(SwingConstants.CENTER);
        lblMonthYear.setText("" + dates.get(0).getMonth() + " " + dates.get(0).getYear());

        btnNxtMonth.setBorder(new EmptyBorder(2, 8, 2, 8));
        btnPrevMonth.setBorder(new EmptyBorder(2, 8, 2, 8));


        monthPanel.setLayout(null);
        monthPanel.add(table);
        monthPanel.add(lblMon);
        monthPanel.add(lblTue);
        monthPanel.add(lblWed);
        monthPanel.add(lblThu);
        monthPanel.add(lblFri);
        monthPanel.add(lblSat);
        monthPanel.add(lblSun);
        monthPanel.add(btnPrevMonth);
        monthPanel.add(lblMonthYear);
        monthPanel.add(btnNxtMonth);

        lblSelectedDateDate = new JLabel("Selected date:");
        lblSelectedDateDate.setBounds(409, 66, 89, 14);
        monthPanel.add(lblSelectedDateDate);

        textField = new JTextField();
        textField.setBounds(499, 63, 89, 20);
        monthPanel.add(textField);
        textField.setColumns(10);

        textPane = new JTextPane();
        textPane.setBounds(408, 91, 180, 163);
        monthPanel.add(textPane);

        btnSave = new JButton("Save");
        btnSave.setBounds(523, 289, 65, 23);
        monthPanel.add(btnSave);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(409, 289, 71, 23);
        monthPanel.add(btnDelete);

        lblMemo = new JLabel("Memo");
        lblMemo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMemo.setBounds(408, 43, 46, 14);
        monthPanel.add(lblMemo);


        tableModel = (DefaultTableModel) table.getModel();
        loadNewTableData(dates);

        return monthPanel;
    }

    public void loadNewTableData(ArrayList<LocalDate> datesArray) {
        dates = datesArray;
        thisDate = dates.get(0);
        lblMonthYear.setText("" + dates.get(0).getMonth() + " " + dates.get(0).getYear());
        tableModel.setRowCount(0);
        int before = 0; //Null elements pushed in before first LocalDate in array
        switch (dates.get(0).getDayOfWeek()) {
            case MONDAY:
                before = 0;
                break;
            case TUESDAY:
                before = 1;
                break;
            case WEDNESDAY:
                before = 2;
                break;
            case THURSDAY:
                before = 3;
                break;
            case FRIDAY:
                before = 4;
                break;
            case SATURDAY:
                before = 5;
                break;
            case SUNDAY:
                before = 6;
                break;
            default:
                break;
        }
        int after = (rowData.length * 6) - before - dates.size(); //Null element added after last LocalDate to fill all 42 cells in table

        for (int i = 0; i < before; i++) dates.add(0, null);
        for (int i = 0; i < after; i++) dates.add(null);
        for (int i = 0; i < dates.size(); ) {
            for (int j = 0; j < 7; j++) {
                if (dates.get(i) != null) {
                    rowData[j] = dates.get(i).getDayOfMonth();
                } else {
                    rowData[j] = null;
                }
                i++;
            }
            tableModel.addRow(rowData);
        }
    }

}
