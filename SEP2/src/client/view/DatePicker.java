package client.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import utility.MyDate;

import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DatePicker extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private LocalDate pickedDate;
	private static DatePicker frame;
	private JTextField textField;
	private JPanel panelTable ;
	private Object[] rowData;
	private DefaultTableModel tableModel;
	private ArrayList<LocalDate> dates;
	private LocalDate thisDate;
	private JLabel lblMonthYear;
	private JLabel lblEndDate;
	private JLabel lblStartDate;
	private JButton btnSave;
	private JButton btnCancel;
	private JTextPane textPane;
	private JLabel lblPlannedForThis;
	private JList list;
	private JButton btnAddToList;
	private JButton btnNewButton;
	private JLabel lblSprintNo;
	private JSpinner spinner;
	private JPanel monthPanel;
	private Font font;
	private JTable table;
	private JLabel lblMon;
	private JLabel lblTue;
	private JLabel lblWed;
	private JLabel lblThu;
	private JLabel lblFri;
	private JLabel lblSat;
	private JLabel lblSun;
	private JButton btnNxtMonth;
	private JButton btnPrevMonth;
	private static final long SPRINT_LENGTH = 4; //days


	public DatePicker() {
		initComponents();
		createEvents();
	}
	
	public void initComponents(){
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setType(Type.UTILITY);
		setBounds(100, 100, 426, 365);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblStartDate = new JLabel("Start date:");
		lblStartDate.setBounds(29, 34, 60, 14);
		contentPane.add(lblStartDate);
		
		textField = new JTextField();
		textField.setBounds(92, 31, 97, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		panelTable = new JPanel();
		panelTable = getMonthPanel(MyDate.getCurrentMonthDates());
		panelTable.setVisible(false);
		panelTable.setBounds(20, 20, 200, 200);
		contentPane.add(panelTable);
		
		lblEndDate = new JLabel("End date:");
		lblEndDate.setBounds(30, 60, 130, 14);
		contentPane.add(lblEndDate);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(29, 272, 89, 23);
		contentPane.add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(131, 272, 89, 23);
		contentPane.add(btnCancel);
		
		textPane = new JTextPane();
		textPane.setBounds(29, 152, 160, 51);
		contentPane.add(textPane);
		
		lblPlannedForThis = new JLabel("Planned for this sprint:");
		lblPlannedForThis.setBounds(230, 34, 150, 14);
		contentPane.add(lblPlannedForThis);
		
		list = new JList();
		list.setBounds(222, 59, 160, 146);
		contentPane.add(list);
		
		btnAddToList = new JButton("Add to list");
		btnAddToList.setBounds(29, 214, 160, 23);
		contentPane.add(btnAddToList);
		
		btnNewButton = new JButton("Remove from list");
		btnNewButton.setBounds(219, 214, 161, 23);
		contentPane.add(btnNewButton);
		
		lblSprintNo = new JLabel("Sprint no:");
		lblSprintNo.setBounds(29, 100, 60, 14);
		contentPane.add(lblSprintNo);
		
		spinner = new JSpinner();
		spinner.setBounds(92, 100, 29, 20);
		contentPane.add(spinner);
		
	}

	public void createEvents(){

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelTable.setVisible(true);
			}
		});
		
		btnSave.addActionListener(e -> {
			this.setVisible(false);
		});
		
		///////// Actionlisteners for date picker: 
		btnNxtMonth.addActionListener( e -> { loadNewTableData(MyDate.getNextMonthDates(thisDate)); });	
		btnPrevMonth.addActionListener(e  -> { loadNewTableData(MyDate.getPreviousMonthDates(thisDate)); });
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int dayPicked = (int)table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
					pickedDate = LocalDate.of(thisDate.getYear(), thisDate.getMonth(), dayPicked);
					
					///////////////////////
					textField.setText(""+pickedDate);
					lblEndDate.setText("End date: "+pickedDate.plusDays(SPRINT_LENGTH));
					panelTable.setVisible(false);
					/////////////////////////////
					
				} catch (Exception e) {
				}
			}
		});
	}
	 
	public JPanel getMonthPanel(ArrayList<LocalDate>datesArray){
		dates = datesArray;
		monthPanel = new JPanel();
		font = new Font("Tahoma", Font.PLAIN, 10);
		thisDate = dates.get(0);

		table = new JTable();
		lblMon = new JLabel("Mon");
		lblTue = new JLabel("Tue");
		lblWed = new JLabel("Wed");
		lblThu = new JLabel("Thu");
		lblFri = new JLabel("Fri");
		lblSat = new JLabel("Sat");
		lblSun = new JLabel("Sun");
		lblMonthYear = new JLabel("Month Year");
		btnNxtMonth = new JButton(">");
		btnPrevMonth = new JButton("<");
		rowData = new Object[7];

		table.setBounds(10, 63, 160, 96);
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
		
		lblMon.setBounds(10, 43, 20, 14);
		lblTue.setBounds(36, 43, 18, 14);
		lblWed.setBounds(60, 43, 22, 14);
		lblThu.setBounds(88, 43, 18, 14);
		lblFri.setBounds(112, 43, 12, 14);
		lblSat.setBounds(130, 43, 16, 14);
		lblSun.setBounds(152, 43, 18, 14);
		btnNxtMonth.setBounds(145, 163, 25, 19);
		btnPrevMonth.setBounds(10, 163, 25, 19);
		lblMonthYear.setBounds(39, 166, 102, 14);
		
		lblMonthYear.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonthYear.setText(""+dates.get(0).getMonth()+" "+dates.get(0).getYear());
		
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
		
		tableModel = (DefaultTableModel) table.getModel();
		loadNewTableData(dates);

		return monthPanel;
	}
	
	public void loadNewTableData(ArrayList<LocalDate> datesArray){
		dates = datesArray;
		thisDate = dates.get(0);
		lblMonthYear.setText(""+dates.get(0).getMonth()+" "+dates.get(0).getYear());
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
		int after = (rowData.length*6)-before-dates.size(); //Null element added after last LocalDate to fill all 42 cells in table
		
		for (int i =0; i<before; i++ ) dates.add(0, null);
		for (int i=0; i<after; i++) dates.add(null);
		for (int i=0; i<dates.size();){
			for (int j=0; j<7; j++){
				if (dates.get(i) != null){
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
