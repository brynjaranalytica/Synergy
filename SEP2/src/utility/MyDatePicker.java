package utility;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;


public class MyDatePicker extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MyCalendar myCalendar;
	private LocalDate pickedDate;
	private static MyDatePicker frame;
	private JTextField textField;
	private JPanel panelTable ;
	private Object[] rowData;
	private DefaultTableModel tableModel;
	private ArrayList<LocalDate> dates;
	private LocalDate thisDate;
	private JLabel lblMonthYear;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MyDatePicker();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public MyDatePicker() {
		myCalendar = new MyCalendar();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setType(Type.UTILITY);
		setBounds(100, 100, 531, 564);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblStartDate = new JLabel("Start date:");
		lblStartDate.setBounds(29, 34, 60, 14);
		contentPane.add(lblStartDate);
		
		textField = new JTextField();
		textField.setBounds(92, 31, 97, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		panelTable = new JPanel();
		panelTable = getMonthPanel(myCalendar.getCurrentMonthDates());
		panelTable.setVisible(false);
		panelTable.setBounds(20, 20, 200, 200);
		contentPane.add(panelTable);
		
		JLabel lblEndDate = new JLabel("End date:");
		lblEndDate.setBounds(30, 60, 83, 14);
		contentPane.add(lblEndDate);

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelTable.setVisible(true);
			}
		});
	
	}
	
	public JPanel getMonthPanel(ArrayList<LocalDate>datesArray){
		dates = datesArray;
		JPanel monthPanel = new JPanel();
		Font font = new Font("Tahoma", Font.PLAIN, 10);
		thisDate = dates.get(0);

		JTable table = new JTable();
		JLabel lblMon = new JLabel("Mon");
		JLabel lblTue = new JLabel("Tue");
		JLabel lblWed = new JLabel("Wed");
		JLabel lblThu = new JLabel("Thu");
		JLabel lblFri = new JLabel("Fri");
		JLabel lblSat = new JLabel("Sat");
		JLabel lblSun = new JLabel("Sun");
		lblMonthYear = new JLabel("Month Year");
		JButton btnNxtMonth = new JButton(">");
		JButton btnPrevMonth = new JButton("<");
		rowData = new Object[7];

		table.setBounds(10, 63, 160, 107);
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
		btnNxtMonth.setBounds(145, 181, 25, 19);
		btnPrevMonth.setBounds(10, 181, 25, 19);
		lblMonthYear.setBounds(39, 183, 102, 14);
		
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

		
		
		///////// Actionlisteners: 
		
		btnNxtMonth.addActionListener( e -> { loadNewTableData(myCalendar.getNextMonthDates(thisDate)); });	
		btnPrevMonth.addActionListener(e  -> { loadNewTableData(myCalendar.getPreviousMonthDates(thisDate)); });
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int dayPicked = (int)table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
					pickedDate = LocalDate.of(thisDate.getYear(), thisDate.getMonth(), dayPicked);
					
					///////////////////////
					textField.setText(""+pickedDate);
					panelTable.setVisible(false);
					/////////////////////////////
					
				} catch (Exception e) {
				}
			}
		});

		
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
