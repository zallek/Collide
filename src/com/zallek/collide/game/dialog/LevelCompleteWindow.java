package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.zallek.collide.R;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.util.score.Score;

public class LevelCompleteWindow extends Sprite
{
    private Text levelCompleteText;
    
    private Text scoreText;
    private Text timeText;
    private Text totalScoreText;
    
    private ResourcesManager resourcesManager;
    private Score score;
    
    public LevelCompleteWindow(Score score, VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);
        resourcesManager = ResourcesManager.getInstance();
        this.score = score;
        attachText(pSpriteVertexBufferObject);
    }
    
    private void attachText(VertexBufferObjectManager vbom){
    	levelCompleteText = new Text(325, 350, resourcesManager.font, resourcesManager.getRessourcesString(R.string.levelComplete), new TextOptions(HorizontalAlign.CENTER), vbom);
    	attachChild(levelCompleteText);
    			
    	scoreText = new Text(200, 250, resourcesManager.font, resourcesManager.getRessourcesString(R.string.score) + "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
    	attachChild(scoreText);
    	
    	timeText = new Text(200, 200, resourcesManager.font, resourcesManager.getRessourcesString(R.string.timeElapsed) + "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
    	attachChild(timeText);
    	
    	totalScoreText = new Text(200, 100, resourcesManager.font, resourcesManager.getRessourcesString(R.string.totalScore) + "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
    	attachChild(totalScoreText);
    }
 

    public void display(Scene scene, Camera camera)
    {
    	scoreText.setText(resourcesManager.getRessourcesString(R.string.score) + score.score);	
    	timeText.setText(resourcesManager.getRessourcesString(R.string.timeElapsed) + score.timeElapsed);   	
    	totalScoreText.setText(resourcesManager.getRessourcesString(R.string.totalScore) + score.getFinalScore());  	
        
        // Attach our level complete panel in the middle of camera
        setPosition(0, 0);
        //scene.attachChild(this);
    }
}
