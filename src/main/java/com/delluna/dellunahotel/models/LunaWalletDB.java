/**
 * File: LunaWallet.java
 * Author: Khilfi
 * Description:
 * The Luna Wallet is a component of Del Luna Hotel app.
 * The purpose of the e-wallet is for transactions.
 * User can top-up money inside the e-wallet and obtain points for future use.
 */
package com.delluna.dellunahotel.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles database operations for LunaWallet
 */
public class LunaWalletDB implements AutoCloseable {
	private String UID = "";
	private String pinHash;
	private double balance;
	private int lunaPoints;
	private String securityQuestion;
	private String securityAnswerHash;
	private String dateCreated;
	private String lastUpdated;
	private static final String FILE_PATH = "src/database/Wallet.txt";
	private static final double BALANCE_LIMIT = 100_000;
	
	/**
	 * Loads user LunaWallet based on UID
	 * @param UID
	 * @throws UidNotFound 
	 */
	public LunaWalletDB(String UID) throws UidNotFound {
		try (Scanner sc = new Scanner(new File(FILE_PATH))) {
			
			boolean isHeader = true;
			while (sc.hasNextLine()) {
				
				String line = sc.nextLine();
				
				if (isHeader) {
					isHeader = false;
					continue;
				}
				
				String[] data = line.split(",");
				
				String uid = data[0];
				
				if (!uid.equals(UID)) {
					continue;
				}
				this.UID = uid;
				this.pinHash = data[1];
				this.balance = Double.parseDouble(data[2]);
				this.lunaPoints = Integer.parseInt(data[3]);
				this.securityQuestion = data[4];
				this.securityAnswerHash = data[5];
				this.dateCreated = data[6];
				this.lastUpdated = data[7];
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!this.UID.equals(UID)) {
			throw new UidNotFound(UID);
		}
	}
	
	public void save() {
		String out = "UID,PIN,Balance,LunaPoints,SecQuestion,SecAnswer,DateCreated,LastUpdated\n";
		
		try (Scanner sc = new Scanner(new File(FILE_PATH))) {
			boolean isHeader = true;
			while (sc.hasNextLine()) {
				
				String line = sc.nextLine();
				
				if (isHeader) {
					isHeader = false;
					continue;
				}
				
				String[] data = line.split(",");
				
				String uid = data[0];
				
				if (!uid.equals(UID)) {
					out += line + "\n";
					continue;
				}
				out += String.join(",", UID, pinHash, Double.toString(balance), Integer.toString(lunaPoints), securityQuestion, securityAnswerHash, dateCreated, lastUpdated) + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileWriter writer = new FileWriter(FILE_PATH)) {
			writer.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Top up balance amount
	 * @param amount
	 * @throws BalanceLimitExceeded 
	 */
	public void topUpBalance(double amount) throws BalanceLimitExceeded {
		if (balance + amount > BALANCE_LIMIT) {
			throw new BalanceLimitExceeded();
		}
		balance += amount;
	}
	
	/**
	 * Deduct money from Wallet
	 * @param amount
	 * @throws NotEnoughBalance 
	 */
	public void deductBalance(double amount) throws NotEnoughBalance {
		if (balance - amount < 0) {
			throw new NotEnoughBalance();
		}
		balance -= amount;
	}
	
	/**
	 * Returns amount of balance inside Wallet
	 * @return
	 */
	public double getBalance() {
		return balance;
	}
	
	public String getPinHash() {
		return pinHash;
	}
	
	public void setPin(String newPin) {
		pinHash = newPin;
	}
	
	public void setSecurityQuestion(String securityQuestion, String answer) {
		this.securityQuestion = securityQuestion;
		this.securityAnswerHash = answer;
	}
	
    public static class UidNotFound extends Exception {
		private static final long serialVersionUID = -4102964319935793290L;

		public UidNotFound(String UID) {
            super("UID " + UID + " doesn't exist");
        }
    }
    
    public static class BalanceLimitExceeded extends Exception {
		private static final long serialVersionUID = -5197016556609704353L;

		public BalanceLimitExceeded() {
    		super("Balance limit exceeded RM " + BALANCE_LIMIT);
    	}
    }
    
    public static class NotEnoughBalance extends Exception {
		private static final long serialVersionUID = -9058636885723273681L;

		public NotEnoughBalance() {
    		super("Not enough balance");
    	}
    }
    
    LunaWalletDB() {
    	
    }
    
    @Override
    public void close() {}
}
