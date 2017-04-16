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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyDatePicker extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MyCalendar myCalendar;
	private JTable table;
	private LocalDate pickedDate;
	private static MyDatePicker frame;

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

	/**
	 * Create the frame.
	 */
	public MyDatePicker() {
		myCalendar = new MyCalendar();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setType(Type.UTILITY);
		setBounds(100, 100, 222, 253);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(getMonthPanel(myCalendar.getCurrentMonthDates()));
		
	}
	
	public JPanel getMonthPanel(ArrayList<LocalDate>dates){
		LocalDate thisDate = dates.get(0);
		String monthYearStr = ""+dates.get(0).getMonth()+" "+dates.get(0).getYear();
		JTable table = new JTable();

		
		table.setIntercellSpacing(new Dimension(2, 2));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowSelectionAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {

			},
			new String[] {
				"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		ArrayList<LocalDate> monthDates = dates;
		JPanel monthPanel = new JPanel();
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		Object[] rowData = new Object[7];
		int before = 0; //Null elements pushed in before first LocalDate in array
		switch (monthDates.get(0).getDayOfWeek()) {
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
		int after = (rowData.length*6)-before-monthDates.size(); //Null element added after last LocalDate to fill all 42 cells in table
		
		for (int i =0; i<before; i++ ) monthDates.add(0, null);
		for (int i=0; i<after; i++) monthDates.add(null);
		for (int i=0; i<monthDates.size();){
			for (int j=0; j<7; j++){
				if (monthDates.get(i) != null){
					rowData[j] = monthDates.get(i).getDayOfMonth();					
				} else {
					rowData[j] = null;
				}
				i++;
			}
			tableModel.addRow(rowData);
		}
		
		JLabel lblMon = new JLabel("Mon");
		
		JLabel lblTue = new JLabel("Tue");
		
		JLabel lblWed = new JLabel("Wed");
		
		JLabel lblThu = new JLabel("Thu");
		
		JLabel lblFri = new JLabel("Fri");
		
		JLabel lblSat = new JLabel("Sat");
		
		JLabel lblSun = new JLabel("Sun");
		
		JLabel lblMonthYear = new JLabel("Month Year");
		lblMonthYear.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonthYear.setText(monthYearStr);
		
		JButton btnNxtMonth = new JButton(">");
		btnNxtMonth.setBorder(new EmptyBorder(2, 8, 2, 8));
		btnNxtMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(getMonthPanel(myCalendar.getNextMonthDates(thisDate)));
			}
		});
		
		JButton btnPrevMonth = new JButton("<");
		btnPrevMonth.setBorder(new EmptyBorder(2, 8, 2, 8));
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setContentPane(getMonthPanel(myCalendar.getPreviousMonthDates(thisDate)));
			}
		});
		
		JLabel lblDatePicked = new JLabel("Date picked: ");
		GroupLayout gl_currentMonthPanel = new GroupLayout(monthPanel);
		gl_currentMonthPanel.setHorizontalGroup(
			gl_currentMonthPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_currentMonthPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_currentMonthPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_currentMonthPanel.createSequentialGroup()
							.addComponent(lblDatePicked)
							.addPreferredGap(ComponentPlacement.RELATED, 123, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_currentMonthPanel.createParallelGroup(Alignment.TRAILING)
							.addComponent(table, 0, 0, Short.MAX_VALUE)
							.addGroup(gl_currentMonthPanel.createSequentialGroup()
								.addComponent(lblMon)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblTue)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblWed)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblThu)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblFri)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblSat)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblSun))
							.addGroup(gl_currentMonthPanel.createSequentialGroup()
								.addComponent(btnPrevMonth)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblMonthYear, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnNxtMonth))))
					.addGap(238))
		);
		gl_currentMonthPanel.setVerticalGroup(
			gl_currentMonthPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_currentMonthPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDatePicked)
					.addGap(18)
					.addGroup(gl_currentMonthPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMon)
						.addComponent(lblTue)
						.addComponent(lblWed)
						.addComponent(lblThu)
						.addComponent(lblFri)
						.addComponent(lblSat)
						.addComponent(lblSun))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_currentMonthPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNxtMonth)
						.addComponent(btnPrevMonth)
						.addComponent(lblMonthYear))
					.addContainerGap(61, Short.MAX_VALUE))
		);
		
		

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int dayPicked = (int)table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
					lblDatePicked.setText("Date picked: "+dayPicked+" "+monthYearStr);
					pickedDate = LocalDate.of(thisDate.getYear(), thisDate.getMonth(), dayPicked);

					
					///////////////////////
					System.out.println(pickedDate);
					/////////////////////////////
					
				} catch (Exception e) {
				}
			}
		});
		monthPanel.setLayout(gl_currentMonthPanel);
		return monthPanel;
	}
	
	
}
