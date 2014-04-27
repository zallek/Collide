package com.zallek.collide.game.dialog;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zallek.collide.R;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.scene.GameScene;

public class QuitDialog extends ConfirmDialog 
{
	public QuitDialog(GameScene scene, Camera camera, VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(ResourcesManager.getInstance().getRessourcesString(R.string.quitDialog), scene, camera, pSpriteVertexBufferObject);
	}

	@Override
	protected void OnAcceptClick() {
		scene.backToMenu();
	}


	@Override
	protected void OnCancelClick() {
		
	}
}
