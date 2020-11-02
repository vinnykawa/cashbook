package com.nodem.cashbook.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Helpers;
import android.util.Log;

public class DBReader {

	private Database dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public DBReader(Context c) {
		context = c;

		dbHelper = new Database(c);

	}

	public class AccountsTable {

		List<String> names;
		List<String> conts, emails, descs;

		public AccountsTable() {

			db = dbHelper.getReadableDatabase();
			getRecords();
		}

		private void getRecords() {
			String query = "SELECT * FROM " + Database.TABLE_ACCOUNTS;

			Cursor c = db.rawQuery(query, null);

			names = new ArrayList<String>();
			conts = new ArrayList<String>();
			emails = new ArrayList<String>();
			descs = new ArrayList<String>();

			while (c.moveToNext()) {
				String name = c.getString(c
						.getColumnIndex(Database.COLUMN_ACC_NAME));
				String contact = c.getString(c
						.getColumnIndex(Database.COLUMN_ACC_CONTACT));
				String email = c.getString(c
						.getColumnIndex(Database.COLUMN_ACC_EMAIL));

				String desc = c.getString(c
						.getColumnIndex(Database.COLUMN_ACC_DESC));
				// String bal =
				// c.getString(c.getColumnIndex(Database.COLUMN_CREDIT));

				names.add(name);
				conts.add(contact);
				emails.add(email);
				descs.add(desc);
			}

			
		}

		public List<String> getAccNames() {
			return names;
		}

		public void dropAccount(String name) {
			db = dbHelper.getWritableDatabase();
			String sql = "DELETE FROM " + Database.TABLE_ACCOUNTS + " WHERE "
					+ Database.COLUMN_ACC_NAME + "='" + name + "'";
			db.execSQL(sql);
			db.close();
		}

		public List<String> getAccContacts() {
			return conts;
		}

		public List<String> getAccountEmails() {
			return emails;
		}

		public List<String> getAccountDescriptions() {
			return descs;
		}

	}

	public class TransactionsTable {

		String where = null;

		public TransactionsTable() {
			db = dbHelper.getReadableDatabase();
			getRecords();
		}

		public TransactionsTable(String name) {
			db = dbHelper.getReadableDatabase();
			where = name;
			getRecords();
			
		}

		private List<String> names = new ArrayList<String>();
		private List<String> dates = new ArrayList<String>();
		private List<String> reminder_dates = new ArrayList<String>();
		private List<String> notes = new ArrayList<String>();
		private List<Integer> credits = new ArrayList<Integer>();
		private List<Integer> debits = new ArrayList<Integer>();
		private List<String> amounts = new ArrayList<String>();
		private List<String> contacts = new ArrayList<String>();

		private void getRecords() {
			String query;
			if (where == null)
				query = "SELECT * FROM " + Database.TABLE_TRANSACTIONS;
			else
				query = "SELECT * FROM " + Database.TABLE_TRANSACTIONS
						+ " WHERE " + Database.COLUMN_ACC_NAME + "='" + where
						+ "'";

			Cursor c = db.rawQuery(query, null);

			while (c.moveToNext()) {
				String name = c.getString(c
						.getColumnIndex(Database.COLUMN_ACC_NAME));
				String date = c.getString(c
						.getColumnIndex(Database.COLUMN_TRANS_DATE));
				String reminders = c.getString(c
						.getColumnIndex(Database.COLUMN_DUE_REMINDER));
				String note = c.getString(c
						.getColumnIndex(Database.COLUMN_TRANS_NOTE));

				int credit = c.getInt(c.getColumnIndex(Database.COLUMN_CREDIT));
				int debit = c.getInt(c.getColumnIndex(Database.COLUMN_DEBIT));

				names.add(name);
				dates.add(date);
				reminder_dates.add(reminders);
				notes.add(note);
				credits.add(credit);
				debits.add(debit);

				amounts.add(credit > 0 ? credit + " Cr" : debit + " Dr");

			}

			db.close();
		}

		// get records for particullar account
		public class DashBoard {

			List<String> creditors = new ArrayList<String>();
			List<String> debtors = new ArrayList<String>();

			List<Integer> creditTotals = new ArrayList<Integer>();
			List<Integer> debitTotals = new ArrayList<Integer>();
			HashMap<String, String> debits, credits;
			SQLiteDatabase	db;
			public DashBoard() {
				 db= dbHelper.getReadableDatabase();
				getRecords();

			}

			private void getRecords() {
				db = dbHelper.getReadableDatabase();
				
				Set<String> set = new LinkedHashSet<String>();
				set.addAll(names);
				names.clear();
				names.addAll(set);

				set.clear();
				set.addAll(new AccountsTable().getAccContacts());
				contacts.addAll(set);

				credits = new HashMap<String, String>();

				// get creditors
				for (int i = 0; i < names.size(); i++) {
					String query = "SELECT SUM (" + Database.COLUMN_CREDIT
							+ ") as sum FROM " + Database.TABLE_TRANSACTIONS
							+ " WHERE " + Database.COLUMN_ACC_NAME + "='"
							+ names.get(i) + "'";

					Cursor c = db.rawQuery(query, null);

					c.moveToFirst();

					String sum = c.getString(c.getColumnIndex("sum"));
					credits.put(names.get(i), sum);

				}

				debits = new HashMap<String, String>();
				// debtors
				for (int i = 0; i < names.size(); i++) {
					String query = "SELECT SUM (" + Database.COLUMN_DEBIT
							+ ") as sum FROM " + Database.TABLE_TRANSACTIONS
							+ " WHERE " + Database.COLUMN_ACC_NAME + "='"
							+ names.get(i) + "'";

					Cursor c = db.rawQuery(query, null);

					c.moveToFirst();

					String sum = c.getString(c.getColumnIndex("sum"));
					debits.put(names.get(i), sum);

				}

				for (String key : names) {
					int credit = Integer.parseInt(credits.get(key));
					int debit = Integer.parseInt(debits.get(key));

					int diff = credit - debit;
					if (diff > 0) {
						creditors.add(key);
						creditTotals.add(diff);
					} else {
						debtors.add(key);
						debitTotals.add((-diff));
					}
				}
			}

			public List<String> getCreditors() {
				return creditors;
			}

			public List<Integer> getCreditTotals() {
				return creditTotals;
			}

			public List<String> getDebtors() {
				return debtors;
			}

			public List<Integer> getDebitTotals() {
				return debitTotals;
			}

			public String getContacts(int index) {
				return contacts.get(index);
			}

		}

		public List<String> getTransactionAccountNames() {
			return names;
		}

		public List<String> getTransactionDates() {
			return dates;
		}

		public List<String> getTransactionDueDates() {
			return reminder_dates;
		}

		public List<String> getTransactionNotes() {
			return notes;
		}

		public List<Integer> getCreditTransactions() {
			return credits;
		}

		public List<Integer> getDebitTransactions() {
			return debits;
		}

		public List<String> getTransactionAmounts() {
			return amounts;
		}
	}

	public void insertIntoAccountsTable(String acName, String contact,
			String email, String desc) {

		db = dbHelper.getWritableDatabase();

		String query = "INSERT INTO " + Database.TABLE_ACCOUNTS + " VALUES ('"
				+ acName + "'," + contact + ",'" + email + "','" + desc + "')";

		db.execSQL(query);

		db.close();

	}

	public void insertIntoTransactionTable(String name, String date,
			String due_reminder, String note, int debit, int credit) {
		db = dbHelper.getWritableDatabase();
		String query = "INSERT INTO " + Database.TABLE_TRANSACTIONS
				+ " VALUES ('" + name + "','" + date + "','" + due_reminder
				+ "','" + note + "'," + credit + "," + debit + ")";

		db.execSQL(query);
		db.close();

	}

	public void flushDb() {
		db = dbHelper.getWritableDatabase();
		// things are going ok drop current db
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_ACCOUNTS);
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_TRANSACTIONS);

		// recreate tables
		db.execSQL(Database.createAccountsTable);
		db.execSQL(Database.createTransactionTable);
	}
}
