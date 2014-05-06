package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.World;


public class BodyPolar extends Body {
	
	protected BodyPolar(World world, long addr) {
		super(world, addr);
	}
	
	//TODO VERIFY THE BEHAVIOR OF THE NATIVE METHOD
	/*public double getAngle(){
		return toAngle(getLinearVelocity().x, getLinearVelocity().y);
	}*/
	
	public double getVelocity(){
		return Math.sqrt(Math.pow(getLinearVelocity().x, 2) + Math.pow(getLinearVelocity().y, 2));
	}
	
	
	public void setAngle(float x, float y){
		setAngle(toAngle(x, y));
	}
	
	public void setAngle(double angle){
		setLinearVelocity(this.getVelocity(), angle);
	}
	
	public void setLinearVelocity(double velocity, double angle){
		final float vX = (float) (Math.cos(angle) * velocity);
		final float vY = (float) (Math.sin(angle) * velocity);
		
		setLinearVelocity(vX, vY);
	}
	
	
	/** Utils **/
	
	private double toAngle(float x, float y){
		double angle = 0;
		if(x > 0){
			angle = Math.atan(y/x);
		}
		else if(x < 0 && y >= 0){
			angle = Math.atan(y/x) + Math.PI;
		}
		else if(x < 0 && y < 0){
			angle = Math.atan(y/x) - Math.PI;
		}
		else if(x == 0 && y > 0){
			angle = Math.PI/2;
		}
		else if(x == 0 && y < 0){
			angle = Math.PI/2*-1;
		}
		return angle;
	}
}
