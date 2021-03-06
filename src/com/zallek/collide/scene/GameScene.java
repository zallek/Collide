package com.zallek.collide.scene;

import java.io.IOException;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.CountText;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.CountText.CountTextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zallek.collide.R;
import com.zallek.collide.entity.Ball;
import com.zallek.collide.game.dialog.LevelCompleteWindow;
import com.zallek.collide.game.dialog.PauseDialog;
import com.zallek.collide.game.dialog.QuitDialog;
import com.zallek.collide.game.dialog.ResetDialog;
import com.zallek.collide.manager.SceneManager;
import com.zallek.collide.manager.SceneManager.SceneType;
import com.zallek.collide.util.constants.GameConstants;
import com.zallek.collide.util.constants.XmlLevelConstants;
import com.zallek.collide.util.game.GameStateManagement;
import com.zallek.collide.util.score.Score;
import com.zallek.collide.scene.GameScene;

public class GameScene extends BaseScene implements GameStateManagement
{
	enum GameState {
		INIT,
		RUNNING,
		PAUSE,
		FINISHED
	}
	
    private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	
	//Elements
	private ButtonSprite reset_button;
	private ButtonSprite pause_button;
	private LevelCompleteWindow levelCompleteWindow;
	private Text scoreText;
	private CountText timeText;
	
	//Utils
	private Score score;
	private GameState state = GameState.INIT;
	private int timeValue;

	
	 //**** Initialization ****//
	
	@Override
    public void createScene()
    {
		state = GameState.INIT;
		score = new Score();
		setTouchAreaBindingOnActionDownEnabled(true);
	
		createBackground();
	    createHUD();
	    createPhysics();
	    
	    levelCompleteWindow = new LevelCompleteWindow(score, vbom);
	    
		loadLevel(1);
	    start();
	    state = GameState.RUNNING;
    }
	
    
    private void createPhysics()
    {
    	physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        physicsWorld.setContactListener(setContactListener());
        registerUpdateHandler(physicsWorld);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setHUD(null);
        camera.setCenter(camera.getWidth()/2, camera.getHeight()/2);
        Ball.getBallsList().clear();
    }
    

    public void backToMenu(){
    	SceneManager.getInstance().loadMenuScene(engine);
    }
    
    
    //**** UI ****//
    
    private void createBackground()
    {
        setBackground(new Background(Color.BLACK));
    }
    
    private void createHUD()
    {
        gameHUD = new HUD();

        final float cam_h = GameConstants.CAMERA_HEIGHT;
        
        //ScoreText    
        scoreText = new Text(110, cam_h-60, resourcesManager.font, resourcesManager.getRessourcesString(R.string.score) + "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom); //String : all characters that will be used
        scoreText.setPosition(0, 0);    
        setScore(0);
        gameHUD.attachChild(scoreText);
        
        //Pause Button
        pause_button = new ButtonSprite(25, cam_h-24, resourcesManager.pause_button_region, vbom);
        pause_button.setOnClickListener(setPauseButtonListener());
        gameHUD.attachChild(pause_button);
        
        //Reset Button
        reset_button = new ButtonSprite(75, cam_h-24, resourcesManager.reset_button_region, vbom);
        reset_button.setOnClickListener(setResetButtonListener());
        gameHUD.attachChild(reset_button);
        
        camera.setHUD(gameHUD);
    }

    private void setScore(int score)
    {
        this.score.setScore(score);
        scoreText.setText(resourcesManager.getRessourcesString(R.string.score) + score);
    }
    
    private void displayTimeText()
    {
    	timeText = new CountText(0, 0, resourcesManager.font, timeValue, 0, new CountTextOptions(), vbom){
			@Override
			public void onFinished() {
				finish();
			}
        };
        timeText.setPosition(camera.getCenterX(), camera.getCenterY());
        timeText.setScale(4);
        attachChild(timeText);
    }
    
    
    //**** Level ****//
    
    private void loadLevel(int levelID)
    {
        final LevelLoader levelLoader = new LevelLoader();
        		
        levelLoader.registerEntityLoader(XmlLevelConstants.TAG_LEVEL, new IEntityLoader()
        {
        	@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) 
        	{
                timeValue = SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_LEVEL_ATTRIBUTE_TIME);
                score.addCap(1, SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_LEVEL_ATTRIBUTE_STAR1CAP));
                score.addCap(2, SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_LEVEL_ATTRIBUTE_STAR2CAP));
                score.addCap(3, SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_LEVEL_ATTRIBUTE_STAR3CAP));
                
                return GameScene.this;
            }
        });
        
        levelLoader.registerEntityLoader(XmlLevelConstants.TAG_BALL, new IEntityLoader()
        {
        	@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) 
        	{
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_BALL_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_BALL_ATTRIBUTE_Y);
                
                final String colorString = SAXUtils.getAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_BALL_ATTRIBUTE_COLOR);
                final int color = android.graphics.Color.parseColor(colorString);
                
                final float velocity = SAXUtils.getFloatAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_BALL_ATTRIBUTE_VELOCITY);
                final float direction = SAXUtils.getFloatAttributeOrThrow(pAttributes, XmlLevelConstants.TAG_BALL_ATTRIBUTE_DIRECTION);
                
                Ball ball = new Ball(x, y, color, GameScene.this, physicsWorld){
					@Override
					public void onChanged() {
						calculateScore();
					}
                };
                ball.setVelocityAndDirection(velocity, direction);
                ball.pause();
                
                return ball;
            }
        });

        try {
        	levelLoader.loadLevelFromAsset(activity.getAssets(), "level/standard/" + levelID + ".lvl");
        }
        catch(IOException e){
        	Debug.e(e);
        	//TODO Display erreur chargement et debugger
        	backToMenu();
        }
    }
    
    
    //**** Listeners ****//
    
    private OnClickListener setPauseButtonListener() {
    	return new OnClickListener(){
    		@Override
    		public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
    			new PauseDialog(GameScene.this, camera, vbom).display();
    		}
        };
    }
    
    private OnClickListener setResetButtonListener() {
    	return new OnClickListener(){
    		@Override
    		public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
    			new ResetDialog(GameScene.this, camera, vbom).display();
    		}
        };
    }

    private ContactListener setContactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Object x1 = contact.getFixtureA().getBody().getUserData();
	            final Object x2 = contact.getFixtureB().getBody().getUserData();
	            if(x1 != null && x2 != null){
	            	if(x1 instanceof Ball && x2 instanceof Ball){
		            	Ball.ballCollision((Ball) x1, (Ball) x2);
		            }
	            }
            }

            public void endContact(Contact contact)
            {
                
            }

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) 
			{
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) 
			{
				
			}
        };
        return contactListener;
    }
    
    
    //**** Game activity management ****//
    
    public void start() {
    	displayTimeText();
    	
    	final float wait_time = 2;
    	final float transition_time = 1;
    	
    	engine.registerUpdateHandler(new TimerHandler(wait_time, new ITimerCallback() 
    	{
    		public void onTimePassed(final TimerHandler pTimerHandler) 
    		{
    			timeText.registerEntityModifier(new ScaleModifier(transition_time, 4, 1));
    			timeText.registerEntityModifier(new MoveModifier(transition_time, camera.getCenterX(), camera.getCenterY(), GameConstants.CAMERA_WIDTH - 50, GameConstants.CAMERA_HEIGHT - 30));
    			engine.registerUpdateHandler(new TimerHandler(transition_time, new ITimerCallback() 
    	    	{
    	    		public void onTimePassed(final TimerHandler pTimerHandler) 
    	    		{
		    			timeText.start();
		    			resume();
    	    		}
    	    	}));
    		}
    	}));
    }
    
    //TODO Doit bloquer l'action sur les balls et boutons HUD aussi
    public void pause() {
    	Ball.pauseAllBalls();
    	timeText.pause();
    	camera.getHUD().unregisterTouchArea(pause_button);
    	camera.getHUD().unregisterTouchArea(reset_button);
    	setIgnoreUpdate(true);
    	state = GameState.PAUSE;
    }

    public void resume() {
    	setIgnoreUpdate(false);
    	camera.getHUD().registerTouchArea(pause_button);
    	camera.getHUD().registerTouchArea(reset_button);
    	Ball.resumeAllBalls();
    	timeText.resume();
    	state = GameState.RUNNING;
    }
    
    public void reset() {
    	disposeScene();
    	this.detachChildren();
    	createScene();
    }
    
    public void finish() 
    {
    	levelCompleteWindow.display(this, camera);
        camera.getHUD().setVisible(false);
        timeText.setVisible(false);
        
        setIgnoreUpdate(true);
    	pause();
    	state = GameState.FINISHED;
    }
    
    
    //**** Utils ****//
    
    private void calculateScore() {
    	int newScore = 0;
		for(Ball b : Ball.getBallsList()){
			newScore += Math.pow(b.getSize(), 1.5);
		}
		newScore = Math.round(newScore);

		setScore(newScore);
		finishIfNoBall();
    }
    
    private void finishIfNoBall() {
    	if(Ball.getBallsList().isEmpty()){
    		finish();
    	}
    }

    public boolean isStarted(){
    	return this.state != GameState.INIT;
    }

    
    /** App Activity Management **/
    
	@Override
	public void onBackKeyPressed()
	{
		if(state != GameState.FINISHED){
			new QuitDialog(GameScene.this, camera, vbom).display();
		}
		else {
			this.backToMenu();
		}
	}
	
	@Override
	public void onRestart() {
	
	}
}
