package com.zallek.collide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DAOBase {
	protected final static int VERSION = 1;
	protected final static String DB_FILE = "database.db";

	protected SQLiteDatabase mDb = null;
	protected SQLiteOpenHelper handler = null;
	
	protected DAOBase(Context pContext) {
		this.handler = new DbHandler(pContext, DB_FILE, null, VERSION);
		open();
	}

	public SQLiteDatabase open() {
		mDb = handler.getWritableDatabase();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}
}
