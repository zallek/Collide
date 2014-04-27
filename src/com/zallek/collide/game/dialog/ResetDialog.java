package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zallek.collide.R;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.scene.GameScene;

public class ResetDialog extends ConfirmDialog 
{
	public ResetDialog(GameScene scene, Camera camera, VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(ResourcesManager.getInstance().getRessourcesString(R.string.resetDialog), scene, camera, pSpriteVertexBufferObject);
	}

	@Override
	protected void OnAcceptClick() {
		scene.reset();
	}


	@Override
	protected void OnCancelClick() {
		
	}
}
