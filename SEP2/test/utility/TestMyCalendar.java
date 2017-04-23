package utility;
import java.time.LocalDate;
import java.util.ArrayList;


public class TestMyCalendar {
	

	
	public static void main(String[] args) throws Exception {
		MyDate calendar = new MyDate();
		
		ArrayList<LocalDate> previousMonth = calendar.getPreviousMonthDates(calendar.getCurrentDate());
		for (LocalDate d: previousMonth){
			System.out.println(""+d.getDayOfMonth()+d.getDayOfWeek()+d.getMonthValue()+d.getMonth()+d.getYear());
		}

		ArrayList<LocalDate> currentMonth = calendar.getCurrentMonthDates();
		for (LocalDate d: currentMonth){
			System.out.println(""+d.getDayOfMonth()+d.getDayOfWeek()+d.getMonthValue()+d.getMonth()+d.getYear());
		}
		
		ArrayList<LocalDate> nextMonth = calendar.getNextMonthDates(calendar.getCurrentDate());
		for (LocalDate d: nextMonth){
			System.out.println(""+d.getDayOfMonth()+d.getDayOfWeek()+d.getMonthValue()+d.getMonth()+d.getYear());
		}
		
		
	}
}
