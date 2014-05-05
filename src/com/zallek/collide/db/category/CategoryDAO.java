package com.zallek.collide.db.category;

import java.util.ArrayList;
import java.util.List;

import com.zallek.collide.db.DAOBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CategoryDAO extends DAOBase {
	
	public static final String CATEGORY_TABLE_NAME = "Level_Category";

	public static final String CATEGORY_ID = "ID";
	public static final String CATEGORY_NAME = "Name";
	
	
	public CategoryDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	
	public long insert(Category c) {
		ContentValues value = new ContentValues();
		value.put(CATEGORY_NAME, c.getName());
		
		return mDb.insert(CATEGORY_TABLE_NAME, null, value);
	}

	public void delete(long id) {
		mDb.delete(CATEGORY_TABLE_NAME, CATEGORY_ID + " = ?", new String[] {String.valueOf(id)});
	}

	public void update(Category c) {
		ContentValues value = new ContentValues();
		value.put(CATEGORY_NAME, c.getName());
		
		mDb.update(CATEGORY_TABLE_NAME, value, CATEGORY_ID  + " = ?", new String[] {String.valueOf(c.getId())});
	}

	public List<Category> selectAll() {
		Cursor c = mDb.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME, new String[]{});
		
		return toCategory(c);
	}
	

	/**
	 * ! Cursor result must contain all columns 
	 * @param c Cursor : result of select request
	 * @return
	 */
	private List<Category> toCategory(Cursor c){
		List<Category> list = new ArrayList<Category>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex(CATEGORY_ID));
			String name = c.getString(c.getColumnIndex(CATEGORY_NAME));
			
			list.add(new Category (id, name));
		}
		c.close();
		
		return list;
	}
	
	public boolean exists(long id){
		Cursor cursor = mDb.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_ID + "=?", new String[] {String.valueOf(id)});
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}
	
	
	//** Table Definition **//

	public static String createTable() {
		return "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
				CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				CATEGORY_NAME + " TEXT NOT NULL, " +
		");";
	}

	public static String dropTable() {
		return "DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME + ";";
	}
}
