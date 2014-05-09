package com.zallek.collide.util.score;

public class Score 
{
	public int score;
	public long timeElapsed; //Sec
	
	public Score() {
		score = 0;
		timeElapsed = 0;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
    /**
     * Set time elapsed 
     * @param time in seconds
     */
	public void setTimeElapsed(long timeSec, boolean add) {
		this.timeElapsed = add ? timeElapsed + timeSec : timeSec;
	}
	
	public long getFinalScore(){
		return score + timeElapsed/10;
	}
}
