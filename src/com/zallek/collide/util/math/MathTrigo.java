package com.zallek.collide.util.math;

import java.lang.Math;

public class MathTrigo {
	
	public static double cartesianToNorm(float x, double y)
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static CartesianCoordinates polarToCatesian(double norm, double angle){
		return new CartesianCoordinates(Math.cos(angle) * norm, Math.sin(angle) * norm);
	}
	
	
	public static double cartesianToAngle(float x, float y){
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
