package server.model;

import java.util.Arrays;

import javax.crypto.SecretKey;

import database.DBdummy;
import shared.User;
import utility.Cryptography;

public class Adapter implements AdapterInterface {
	private DBdummy dbDummy;
	public Adapter() {
		dbDummy = new DBdummy();
	}
	
	@Override
	public User getUser(String userID) {
		User user = dbDummy.retrieveUser(userID);
		char[] passEncrypted = user.getPass();
		char[] passDecrypted = Cryptography.decryptPass(passEncrypted, Cryptography.getKey());
		user.setPass(passDecrypted);
		return user;
	}

	@Override
	public String validateID(String userID) {
		return dbDummy.checkID(userID);
	}

	@Override
	public boolean savePass(String userID, char[] pass) {
		SecretKey key = Cryptography.getKey();
		char[] passEncrypted = (Cryptography.encryptPass(pass, key));
		try {
			dbDummy.archiveNewPassword(userID,passEncrypted);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public char[] getPass(String userID) {
		return Cryptography.decryptPass(dbDummy.retrievePassword(userID), Cryptography.getKey());
	}

	@Override
	public User login(String userID, char[] passWord) {
		User user = dbDummy.retrieveUser(userID);
		if (user == null) return null;
		char[] passEncrypted = user.getPass(); 
		if (passEncrypted == null) return null;
		char[] passDecrypted = Cryptography.decryptPass(passEncrypted, Cryptography.getKey());
		if (userID.equalsIgnoreCase(user.getiD()) && Arrays.equals(passWord, passDecrypted)){
			user.setPass(passDecrypted);
			return user;
		} else {
			return null;
		}
	}

}
