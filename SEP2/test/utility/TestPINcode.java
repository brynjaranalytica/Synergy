package utility;

import java.io.IOException;

import database.DBdummy;

public class TestPINcode {

	public static void main(String[] args) throws IOException {
		PINcode pinCode = PINcode.getInstance();
		DBdummy db = DBdummy.getInstance();
		String phone = db.retrieveUser("mogens.bjerregaard@mac.com").getPhone();
		System.out.println(phone);
//		System.out.println("Your PIN code is: "+pinCode.requestPin("4593961547"));

	}

}
