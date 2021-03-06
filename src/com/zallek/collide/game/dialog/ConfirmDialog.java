package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.zallek.collide.scene.GameScene;

public abstract class ConfirmDialog extends GameDialog {

	private Text confirmText;
	
	public ConfirmDialog(String text, GameScene scene, Camera camera, VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(scene, camera, pSpriteVertexBufferObject);
		
		attach_text(text, pSpriteVertexBufferObject);
		attach_buttons(pSpriteVertexBufferObject);
	}

	private void attach_text(String text, VertexBufferObjectManager vbom){
    	confirmText = new Text(200, 175, resourcesManager.font, text, new TextOptions(HorizontalAlign.CENTER), vbom);
    	attachChild(confirmText);
    }
	
	private void attach_buttons(VertexBufferObjectManager vbom){
		final ButtonSprite accept_button = new ButtonSprite(250, 100, resourcesManager.accept_button_region, vbom);
		accept_button.setOnClickListener(setAcceptButtonListener());
        scene.registerTouchArea(accept_button);
        this.attachChild(accept_button);
        
        final ButtonSprite cancel_button = new ButtonSprite(150, 100, resourcesManager.cancel_button_region, vbom);
        cancel_button.setOnClickListener(setCancelButtonListener());
        scene.registerTouchArea(cancel_button);
        this.attachChild(cancel_button);
    }
	
	private OnClickListener setAcceptButtonListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				hide();
				OnAcceptClick();
			}
		};
	}
	
	private OnClickListener setCancelButtonListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				hide();
				OnCancelClick();
			}
		};
	}
	
	protected abstract void OnAcceptClick();
	protected abstract void OnCancelClick();
}
