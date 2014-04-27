package com.zallek.collide.db.level;

import java.util.ArrayList;
import java.util.List;

import com.zallek.collide.db.DAOBase;
import com.zallek.collide.db.category.CategoryDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class LevelDAO extends DAOBase {
	
	public static final String LEVEL_TABLE_NAME = "Level";

	public static final String LEVEL_ID = "ID";
	public static final String LEVEL_CATEGORY_ID = "Category_ID";
	public static final String LEVEL_NUMBER = "Number";
	public static final String LEVEL_VERSION = "Version";
	public static final String LEVEL_SCORE = "Score";
	public static final String LEVEL_NBSTARS = "NbStars";
	
	
	public LevelDAO(Context pContext) {
		super(pContext);
	}

	
	public long insert(Level l) {
		ContentValues value = new ContentValues();
		
		value.put(LEVEL_CATEGORY_ID, l.getCategory_id());
		value.put(LEVEL_NUMBER, l.getNumber());
		value.put(LEVEL_VERSION, l.getVersion());
		
		final long newId = mDb.insert(LEVEL_TABLE_NAME, null, value);
		l.setId(newId);
		return newId;
	}

	public void delete(long id) {
		mDb.delete(LEVEL_TABLE_NAME, LEVEL_ID + " = ?", new String[] {String.valueOf(id)});
	}

	public void update(Level l) {
		//if(l.getId() != null) {
			ContentValues value = new ContentValues();
			
			value.put(LEVEL_CATEGORY_ID, l.getCategory_id());
			value.put(LEVEL_NUMBER, l.getNumber());
			value.put(LEVEL_VERSION, l.getVersion());
			if(l.getScore() != null){
				value.put(LEVEL_SCORE, l.getScore());
			}
			if(l.getNbStars() != null){
				value.put(LEVEL_NBSTARS, l.getNbStars());
			}
	
			mDb.update(LEVEL_TABLE_NAME, value, LEVEL_ID  + " = ?", new String[] {String.valueOf(l.getId())});
		//}
	}

	public List<Level> selectAll() {
		Cursor c = mDb.rawQuery("SELECT * FROM " + LEVEL_TABLE_NAME, new String[]{});
		
		return toLevel(c);
	}
	

	/**
	 * ! Cursor result must contain all columns 
	 * @param c Cursor : result of select request
	 * @return
	 */
	private List<Level> toLevel(Cursor c){
		List<Level> list = new ArrayList<Level>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex(LEVEL_ID));
			long category_id = c.getLong(c.getColumnIndex(LEVEL_CATEGORY_ID));
			long number = c.getLong(c.getColumnIndex(LEVEL_NUMBER));
			int version = c.getInt(c.getColumnIndex(LEVEL_VERSION));
			Integer score = c.getInt(c.getColumnIndex(LEVEL_SCORE));
			Integer nbStars = c.getInt(c.getColumnIndex(LEVEL_NBSTARS));
			
			list.add(new Level (id, category_id, number, version, score, nbStars));
		}
		c.close();
		
		return list;
	}

	public boolean exists(long id){
		Cursor cursor = mDb.rawQuery("SELECT * FROM " + LEVEL_TABLE_NAME + " WHERE " + LEVEL_ID + "=?", new String[] {String.valueOf(id)});
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}
	
	
	//** Table Definition **//

	public static String createTable() {
		return "CREATE TABLE " + LEVEL_TABLE_NAME + " (" +
				LEVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				LEVEL_CATEGORY_ID + " INTEGER NOT NULL, " +
				LEVEL_NUMBER + " INTEGER NOT NULL, " +
				LEVEL_VERSION + " INTEGER NOT NULL, " +
				LEVEL_SCORE + " INTEGER, " +
				LEVEL_NBSTARS + " INTEGER, " +
			" FOREIGN KEY (" + LEVEL_CATEGORY_ID +") REFERENCES "+ CategoryDAO.CATEGORY_TABLE_NAME +" ("+ CategoryDAO.CATEGORY_ID +")" +
			");";
	}

	public static String dropTable() {
		return "DROP TABLE IF EXISTS " + LEVEL_TABLE_NAME + ";";
	}
}
