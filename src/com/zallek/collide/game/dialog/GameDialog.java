package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.scene.GameScene;

public abstract class GameDialog extends Sprite
{
	protected GameScene scene;
	protected Camera camera;
	protected ResourcesManager resourcesManager;
	private static boolean gd_visible = false;
	
    protected GameDialog(GameScene scene, Camera camera, VertexBufferObjectManager pSpriteVertexBufferObject)
    {
    	super(0, 0, 400, 250, ResourcesManager.getInstance().game_dialog_region, pSpriteVertexBufferObject);
    	this.resourcesManager = ResourcesManager.getInstance();
        this.scene = scene;
        this.camera = camera;
    }
    
    public boolean display() 
    {
    	if(scene.isStarted() && !GameDialog.gd_visible) {
    		scene.pause();
            setPosition(camera.getCenterX(), camera.getCenterY());
            scene.attachChild(this);
            
			GameDialog.gd_visible = true;
			return true;
		}
    	return false;
    }
    
    protected boolean hide() 
    {
    	if(GameDialog.gd_visible){
    		this.detachSelf();
    		scene.resume();
    		this.detachChildren();
    		
    		GameDialog.gd_visible = false;
    		return true;
    	}
    	return false;
    }
}
