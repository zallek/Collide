package com.zallek.collide.entity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.color.ColorUtils;
import org.andengine.util.math.MathUtils;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.BodyPolar;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.scene.GameScene;
import com.zallek.collide.util.constants.GameConstants;

public abstract class Ball extends Sprite {
	
	private static List<Ball> balls = new ArrayList<Ball>();
	
	private GameScene scene;
	private PhysicsWorld physicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, true);
	
	private boolean touchedFlag = false;
	
	private Line lineDirection;
	private BodyPolar body;
	
	private int size = 0;
	private Color type;

	public Ball(final float pX, final float pY, int color, GameScene scene, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().particule_region, ResourcesManager.getInstance().vbom);
		this.scene = scene;
		this.physicsWorld = physicsWorld;
		
		balls.add(this);
		
		setType(color);
		setSize(1);
		
		createLineDirection();
		createPhysics(ResourcesManager.getInstance().camera, physicsWorld);
		setCullingEnabled(true);

		scene.registerTouchArea(this);
	}

	/** Initialization **/
	
	private void createLineDirection(){
		lineDirection = new Line(0, 0, 0, 0, 2, ResourcesManager.getInstance().vbom);
		lineDirection.setColor(getColor());
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {        
		body = (BodyPolar) PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, FIXTURE_DEF);
		body.setUserData(this);
		this.setUserData(body);
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f); //helps to reduce camera "jitering" effect while moving
                
                //Detect out_side
                if (getY() <= 0 || getY() > (camera.getHeight() + mHeight)
                		|| getX() <= 0 || getX() > (camera.getWidth() + mWidth))
                {           
                	onLeaveFrame();
                }
            }
        });
    }
	
	public abstract void onChanged();
	

	/** Events **/
	
	private void onLeaveFrame(){
		 remove();
	}
	
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		
		if (pSceneTouchEvent.isActionDown())
		{
			this.touchedFlag = true;
			this.lineDirection.setPosition(this.getWidth()/2, this.getHeight()/2, pTouchAreaLocalX, pTouchAreaLocalY);
			this.attachChild(lineDirection);
		}
		else if (pSceneTouchEvent.isActionMove())
		{
			if(touchedFlag){
				this.lineDirection.setPosition(this.getWidth()/2, this.getHeight()/2, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}
		else if (pSceneTouchEvent.isActionUp())
		{
			this.touchedFlag = false;
			this.lineDirection.detachSelf();
			
			this.setAngle(pTouchAreaLocalX, pTouchAreaLocalY);
		}
		return true;
	}
	
	static public void onBallCollision(Ball b1, Ball b2){
		if(b1.type.equals(b2.type)) { //FUSION
			b1.setSize(b1.size + b2.size);
			b1.setLinearVelocity(b1.getLinearVelocity().add(b2.getLinearVelocity()));
			b1.setPosition(com.zallek.collide.util.math.Math.mean(b1.getX(), b2.getX()),
					com.zallek.collide.util.math.Math.mean(b1.getY(), b2.getY()));
			b2.remove();
		}
		else {
			if(b1.size >= b2.size){ //b2 is destroyed
				b1.setSize(b1.size - b2.size);
				b1.setLinearVelocity(b1.getLinearVelocity().add(b2.getLinearVelocity()));
				b1.setPosition(com.zallek.collide.util.math.Math.mean(b1.getX(), b2.getX()),
						com.zallek.collide.util.math.Math.mean(b1.getY(), b2.getY()));
				b2.remove();
			}
			else { //b1 is destroyed
				b2.setSize(b2.size - b1.size);
				b2.setLinearVelocity(b2.getLinearVelocity().add(b1.getLinearVelocity()));
				b2.setPosition(com.zallek.collide.util.math.Math.mean(b1.getX(), b2.getX()),
						com.zallek.collide.util.math.Math.mean(b1.getY(), b2.getY()));
				b1.remove();
			}
		}
	}


	
	/** Publics methods **/
	
	public void pause() {
		body.setAwake(false);
		scene.unregisterTouchArea(this);
	}
	
	public void resume() {
		body.setAwake(true);
		scene.registerTouchArea(this);
	}
	
	private void remove(){
		final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);

		physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
		physicsWorld.destroyBody(facePhysicsConnector.getBody());

		scene.unregisterTouchArea(this);
		scene.detachChild(this);
		
		balls.remove(this);
		this.dispose();
		
		System.gc();
		onChanged();
	}
	
	
	/** Getters & Setters **/
	
	public void setAngle(float x, float y){
		body.setAngle(x, y);
	}
	
	public Vector2 getLinearVelocity(){
		return body.getLinearVelocity();
	}
	
	public void setLinearVelocity(Vector2 lv){
		body.setLinearVelocity(lv);
	}
	
	public void setLinearVelocity(double velocity, double angle){
		body.setLinearVelocity(velocity, angle);
	}

	
	public int getSize() {
		return this.size;
	}
	
	private void setSize(int size) {
		this.size = size;
		
		if(this.size <= 0){
			remove();
		}
		else {
			setScale((float) Math.sqrt(this.size*GameConstants.BALL_SCALE/Math.PI));
			Log.d("setSize", "Ball new size : " + size);
		}
		
		onChanged();
	}

	private void setType(int pABGRPackedInt){
		final Color color = ColorUtils.convertABGRPackedIntToColor(pABGRPackedInt);
		setType(color);
	}
	
	private void setType(Color color){
		this.type = color;
		setColor(color);
	}
	
	
	/** Override behaviors **/
	
	//Touch area size modified to make easier to touch small balls
	@Override
	public boolean contains(float pX, float pY) {
		final float radius = Math.max(GameConstants.MIN_TOUCH_RADIUS, this.getWidth()/2);
		return MathUtils.distance(this.getX(), this.getY(), pX, pY) < radius;
	}
	
	
	/** Static interface **/
	
	public static List<Ball> getBallsList() {
		return balls;
	}
	
	public static void pauseAllBalls() {
		for(Ball b : Ball.getBallsList()){
    		b.pause();
    	}
	}

	public static void resumeAllBalls() {
		for(Ball b : Ball.getBallsList()){
			b.resume();
		}
	}
}