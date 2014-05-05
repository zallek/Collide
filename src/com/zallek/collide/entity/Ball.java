package com.zallek.collide.entity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zallek.collide.manager.ResourcesManager;
import com.zallek.collide.util.constants.GameConstants;

public abstract class Ball extends Sprite {
	
	private static List<Ball> balls = new ArrayList<Ball>();
	
	private Scene scene;
	private PhysicsWorld physicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, true);
	
	private boolean touchedFlag = false;
	
	private Line lineDirection;
	private Body body;
	
	private int size = 0;
	private Color type;
	
	private float velocity = 0;
	private double direction = 0;

	public Ball(final float pX, final float pY, int color, Scene scene, PhysicsWorld physicsWorld) {
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
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, FIXTURE_DEF);
		body.setUserData(this);
		this.setUserData(body);
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f); //helps to reduce camera "jitering" effect while moving
                
                //Go outside
                if (getY() <= 0 || getY() > (camera.getHeight() + mHeight)
                		|| getX() <= 0 || getX() > (camera.getWidth() + mWidth))
                {           
                    remove();
                }
            }
        });
    }
	
	public abstract void onChanged();
	
	
	/** Listener **/
	
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
			
			this.setDirection(pTouchAreaLocalX, pTouchAreaLocalY);
		}
		return true;
	}
	
	static public void ballCollision(Ball b1, Ball b2){
		if(b1.type.equals(b2.type)) { //FUSION
			b1.setSize(b1.size + b2.size);
			b2.remove();
		}
		else {
			if(b1.size >= b2.size){ //b is destroyed
				b1.setSize(b1.size - b2.size);
				b2.remove();
			}
			else { //this is destroyed
				b2.setSize(b2.size - b1.size);
				b2.remove();
			}
		}
	}

	//Touch area size modified to make easier to touch small balls
	@Override
	public boolean contains(float pX, float pY) {
		final float radius = Math.max(GameConstants.MIN_TOUCH_RADIUS, this.getWidth()/2);
		return MathUtils.distance(this.getX(), this.getY(), pX, pY) < radius;
	}
	
	
	/** Publics methods **/
	
	public void pause() {
		body.setAwake(false);
		scene.unregisterTouchArea(this);
	}
	
	public void resume() {
		setVelocityAndDirection(this.velocity, this.direction);
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
	
	
	/** Getters & Getters **/
	
	public void setDirection(double direction){
		setVelocityAndDirection(this.velocity, direction);
	}
	
	public void setDirection(float x, float y){
		if(x > 0){
			setDirection(Math.atan(y/x));
		}
		else if(x < 0 && y >= 0){
			setDirection(Math.atan(y/x) + Math.PI);
		}
		else if(x < 0 && y < 0){
			setDirection(Math.atan(y/x) - Math.PI);
		}
		else if(x == 0 && y > 0){
			setDirection(Math.PI/2);
		}
		else if(x == 0 && y < 0){
			setDirection(Math.PI/2*-1);
		}
		
		setVelocityAndDirection(this.velocity, direction);
	}
	
	public void setVelocity(float velocity){
		setVelocityAndDirection(velocity, this.direction);
	}
	
	public void setVelocityAndDirection(float velocity, double direction){
		this.velocity = velocity;
		this.direction = direction;
		
		final float pX = (float) (Math.cos(direction) * velocity);
		final float pY = (float) (Math.sin(direction) * velocity);
		
		body.setLinearVelocity(new Vector2(pX, pY)); 
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