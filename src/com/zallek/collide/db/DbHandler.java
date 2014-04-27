package com.zallek.collide.db;


import com.zallek.collide.db.category.CategoryDAO;
import com.zallek.collide.db.level.LevelDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

	/** Handler methods **/
	public DbHandler(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CategoryDAO.createTable());
		db.execSQL(LevelDAO.createTable());
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(LevelDAO.dropTable());
		db.execSQL(CategoryDAO.createTable());
		onCreate(db);
	}
}
