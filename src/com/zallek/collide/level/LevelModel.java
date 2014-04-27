package com.zallek.collide.level;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;

import com.zallek.collide.db.category.Category;
import com.zallek.collide.db.category.CategoryDAO;
import com.zallek.collide.db.level.Level;
import com.zallek.collide.db.level.LevelDAO;
import com.zallek.collide.level.index.LevelIndexXml;
import com.zallek.collide.level.index.LevelIndexXmlHandler;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.util.constants.XmlLevelIndexConstants;

/**
 * Singleton
 * @author User
 */
public class LevelModel {

	private Context activity;
	private LevelDAO levelDao;
	private CategoryDAO categoryDao;

	
	public void UpdateDbLevels() 
	{
		final List<LevelIndexXml> xmlLevels = loadLevelsFromXml();
		for(LevelIndexXml xmlLevel : xmlLevels){

			if(levelDao.exists(xmlLevel.id)){
				//Update
				category_id = category.getId();
			}
			else {
				//Creation
				levelDao.insert(new Level());
			}
		}
	}
	
	public void UpdateDbCategories(){
		
	}

	public List<LevelIndexXml> loadLevelsFromXml()
	{
		try 
		{
			final InputStream indexFile = new FileInputStream(XmlLevelIndexConstants.FILE_PATH);
			return LevelIndexXmlHandler.loadLevels(indexFile);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//** Singleton **//
	private static final LevelModel INSTANCE = new LevelModel();
	
	protected LevelModel() {
		activity = ResourcesManager.getInstance().activity;
		levelDao = new LevelDAO(activity);
		categoryDao = new CategoryDAO(activity);
	}
	
	public static LevelModel getInstance()
    {
        return INSTANCE;
    }
}
