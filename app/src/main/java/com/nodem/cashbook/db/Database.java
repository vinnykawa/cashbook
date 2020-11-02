package com.nodem.cashbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public static final String DBNAME = "cashbook";

	public static final String TABLE_ACCOUNTS = "Accounts";
	public static final String TABLE_TRANSACTIONS = "Transactions";
	public static final String COLUMN_ACC_NAME = "acc_name";
	public static final String COLUMN_ACC_CONTACT = "acc_contact";
	public static final String COLUMN_ACC_EMAIL = "acc_email";
	public static final String COLUMN_ACC_DESC = "acc_desc";
	public static final String COLUMN_TRANS_NOTE = "note";
	public static final String COLUMN_TRANS_DATE = "_date";
	public static final String COLUMN_DEBIT = "debit";
	public static final String COLUMN_CREDIT = "credit";
	public static final String COLUMN_DUE_REMINDER ="due_reminder";

	public static final String createAccountsTable = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_ACCOUNTS + "(" + COLUMN_ACC_NAME + " TEXT,"
			+ COLUMN_ACC_CONTACT + " TEXT," + COLUMN_ACC_EMAIL + " TEXT,"
			+ COLUMN_ACC_DESC + " TEXT)";
	public static final String createTransactionTable = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_TRANSACTIONS + "(" + COLUMN_ACC_NAME + " TEXT,"
			+ COLUMN_TRANS_DATE + " TEXT,"+COLUMN_DUE_REMINDER+" TEXT," + COLUMN_TRANS_NOTE + " TEXT,"
			+ COLUMN_CREDIT + " int(20) ," + COLUMN_DEBIT + " int(20))";

	public Database(Context c) {
		super(c, DBNAME, null, 4);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createAccountsTable);
		Log.i("Cashbook DB:SQL ", createAccountsTable);
		db.execSQL(createTransactionTable);
		Log.i("Cashbook DB:SQL ", createTransactionTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSACTIONS);
		onCreate(db);

	}

}
