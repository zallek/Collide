package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zallek.collide.scene.GameScene;

public class PauseDialog extends GameDialog {
	
	public PauseDialog(GameScene scene, Camera camera, VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(scene, camera, pSpriteVertexBufferObject);
		
		attach_button(pSpriteVertexBufferObject);
	}

	private void attach_button(VertexBufferObjectManager vbom){
		final ButtonSprite play_button = new ButtonSprite(this.getWidth()/2, this.getHeight()/2, resourcesManager.play_button_region, vbom);
		play_button.setOnClickListener(setPlayButtonListener());
        scene.registerTouchArea(play_button);
        this.attachChild(play_button);
    }
	
	private OnClickListener setPlayButtonListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				hide();
			}
		};
	}
}
