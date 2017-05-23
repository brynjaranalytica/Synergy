package utility;

import server.model.ServerModel;

import java.io.IOException;

public class TestPINcode {

	public static void main(String[] args) throws IOException {
		PINcode pinCode = PINcode.getInstance();
		ServerModel serverModel = ServerModel.getInstance();
		String phone = serverModel.retrieveUser("mogens.bjerregaard@mac.com").getPhone();
		System.out.println(phone);
//		System.out.println("Your PIN code is: "+pinCode.requestPin("4593961547"));

	}

}
