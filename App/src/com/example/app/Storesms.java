package com.example.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Storesms 
{
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NUMBER = "persons_number";
	public static final String KEY_MESSANGE = "persons_message";
	
	private static final String DATABASE_NAME = "smsdb";
	private static final String DATABASE_TABLE = "peopletable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper
	{
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ("+
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			KEY_NUMBER + " TEXT NOT NULL, " + 
			KEY_MESSANGE + " TEXT NOT NULL);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}  
	
	public Storesms(Context c) 
	{
		ourContext = c;
	}
	
	public Storesms open() throws SQLException
	{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		ourHelper.close();
	}

	public long createEntry(String phoneNumber) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NUMBER, phoneNumber);
		cv.put(KEY_MESSANGE,"");
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	public void updateEntry(String phoneNumber, String message) 
	{
		String messages="";
		String lRow="";
		
		String [] columns = new String[] {KEY_ROWID,KEY_NUMBER,KEY_MESSANGE};
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iNumber = c.getColumnIndex(KEY_NUMBER);
		int iMessage = c.getColumnIndex(KEY_MESSANGE);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			String s="";
			s=c.getString(iNumber);
			if(s.equals(phoneNumber))
			{
				messages=c.getString(iMessage);
				lRow=c.getString(iRow);
			}
		}
		
		if(lRow!="")
		{
			messages=messages+"\n\n"+message;
			long row = Long.parseLong(lRow);
		
			ContentValues cv = new ContentValues();
			cv.put(KEY_MESSANGE, messages);
			ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + row, null);
		}
		else
		{
			createEntry(phoneNumber);
			updateEntry(phoneNumber, message);
		}
	}
	public ArrayList<String> getnumbers() {
		String [] columns = new String[] {KEY_ROWID,KEY_NUMBER,KEY_MESSANGE};
		ArrayList< String> dbnumbers = new ArrayList<String>();
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iNumber = c.getColumnIndex(KEY_NUMBER);
		int iMessage = c.getColumnIndex(KEY_MESSANGE);
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			String s="";
			s=c.getString(iNumber);
			dbnumbers.add(s);
		}
		
		return dbnumbers;
	}

	public String getmessages(String phone_number) 
	{
		// TODO Auto-generated method stub
		String messages="";
		
		String [] columns = new String[] {KEY_ROWID,KEY_NUMBER,KEY_MESSANGE};
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iNumber = c.getColumnIndex(KEY_NUMBER);
		int iMessage = c.getColumnIndex(KEY_MESSANGE);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			String s="";
			s=c.getString(iNumber);
			if(s.equals(phone_number))
			{
				messages=c.getString(iMessage);
			}
		}
		return messages;
	}

	public boolean deleteEntry(String temp_number) {
		// TODO Auto-generated method stub
		String messages="";
		
		String [] columns = new String[] {KEY_ROWID,KEY_NUMBER,KEY_MESSANGE};
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iNumber = c.getColumnIndex(KEY_NUMBER);
		int iMessage = c.getColumnIndex(KEY_MESSANGE);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			messages=c.getString(iNumber);
			if(messages.equals(temp_number))
			{
				String key = c.getString(iRow);
				long row = Long.parseLong(key);
				ourDatabase.delete(DATABASE_TABLE, KEY_ROWID+"="+row, null);
				return true;
			}
		}
		return false;
	}	
}
