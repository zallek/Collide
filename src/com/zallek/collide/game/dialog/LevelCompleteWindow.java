package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.zallek.collide.R;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.util.score.Score;

public class LevelCompleteWindow extends Sprite
{
    private TiledSprite star1;
    private TiledSprite star2;
    private TiledSprite star3;
    private Text scoreText;
    private Text levelCompleteText;
    
    private ResourcesManager resourcesManager;
    private Score score;
    
    public LevelCompleteWindow(Score score, VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);
        resourcesManager = ResourcesManager.getInstance();
        this.score = score;
        attachStars(pSpriteVertexBufferObject);
        attachText(pSpriteVertexBufferObject);
    }
    
    private void attachText(VertexBufferObjectManager vbom){
    	levelCompleteText = new Text(325, 350, resourcesManager.font, resourcesManager.getRessourcesString(R.string.levelComplete), new TextOptions(HorizontalAlign.CENTER), vbom);
    	attachChild(levelCompleteText);
    			
    	scoreText = new Text(150, 280, resourcesManager.font, resourcesManager.getRessourcesString(R.string.score) + "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
    	attachChild(scoreText);
    }
    
    private void attachStars(VertexBufferObjectManager vbom)
    {
        star1 = new TiledSprite(150, 150, resourcesManager.complete_stars_region, vbom);
        star2 = new TiledSprite(325, 150, resourcesManager.complete_stars_region, vbom);
        star3 = new TiledSprite(500, 150, resourcesManager.complete_stars_region, vbom);
        
        attachChild(star1);
        attachChild(star2);
        attachChild(star3);
    }
    
    /**
     * Change star`s tile index, depends on stars count.
     * @param starsCount
     */
    public void display(Scene scene, Camera camera)
    {
    	scoreText.setText(resourcesManager.getRessourcesString(R.string.score) + score.getScore());
    	if(score.getScore() <= 0){
    		levelCompleteText.setText(resourcesManager.getRessourcesString(R.string.levelFailed));
    	}
    	
        // Change stars tile index, based on stars count (1-3)
        switch (score.getNumCap())
        {
        	case 0:
	            star1.setCurrentTileIndex(1);
	            star2.setCurrentTileIndex(1);
	            star3.setCurrentTileIndex(1);
	            break;
            case 1:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(1);
                star3.setCurrentTileIndex(1);
                break;
            case 2:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(1);
                break;
            case 3:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(0);
                break;
        }
        
        // Attach our level complete panel in the middle of camera
        setPosition(camera.getCenterX(), camera.getCenterY());
        scene.attachChild(this);
    }
}
