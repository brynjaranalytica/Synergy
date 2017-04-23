package utility;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MyDate {
	
	public MyDate(){

	}
	
	public static ArrayList<LocalDate> getCurrentMonthDates(){
		ArrayList<LocalDate> currentMonth = new ArrayList<>();
		for (int i=1; i<32; i++){
			try {
				LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), i);
				currentMonth.add(date);
			} catch (DateTimeException e) {
				break;
			}
		}
		return currentMonth;
	}
	
	public static LocalDate getCurrentDate(){
		return LocalDate.now();
	}
	
	public static ArrayList<LocalDate> getNextMonthDates(LocalDate date){
		int thisMonth = date.getMonthValue();
		int thisYear = date.getYear();
		int month = (thisMonth==12)? 1:thisMonth+1;
		int year = (month==1)? thisYear+1:thisYear;
		ArrayList<LocalDate> nextMonth = new ArrayList<>();
		for (int i=1; i<32; i++){
			try {
				LocalDate d = LocalDate.of(year, month, i);
				nextMonth.add(d);
			} catch (DateTimeException e) {
				break;
			}
		}
		return nextMonth;
	}
	
	public static ArrayList<LocalDate> getPreviousMonthDates(LocalDate date){
		int thisMonth = date.getMonthValue();
		int thisYear = date.getYear();
		int month = (thisMonth==1)? 12:thisMonth-1;
		int year = (month==12)? thisYear-1:thisYear;
		ArrayList<LocalDate> previousMonth = new ArrayList<>();
		for (int i=1; i<32; i++){
			try {
				LocalDate d = LocalDate.of(year, month, i);
				previousMonth.add(d);
			} catch (DateTimeException e) {
				break;
			}
		}
		return previousMonth;
	}
	
}
