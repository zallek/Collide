package com.zallek.collide.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.zallek.collide.R;
import com.zallek.collide.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
    	setBackground(new Background(Color.WHITE));
        attachChild(new Text(camera.getCenterX(), camera.getCenterY(), resourcesManager.font, resourcesManager.getRessourcesString(R.string.loading), vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }

	@Override
	public void onRestart() {}
}
