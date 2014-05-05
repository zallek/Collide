package com.zallek.collide.db.level;

public class Level {
	// Notez que l'identifiant est un long
	private long id;
	private long category_id;
	private long number;
	private int version;
	private Integer score;
	private Integer nbStars;
	
	public Level(long id, long category_id, long number, int version, Integer score, Integer nbStars) {
		super();
		this.id = id;
		this.category_id = category_id;
		this.number = number;
		this.version = version;
		this.score = score;
		this.nbStars = nbStars;
	}
	
	public Level(long id, long category_id, long number, int version) {
		this(id, category_id, number, version, null, null);
	}

	public long getId() {
		return id;
	}

	public long getCategory_id() {
		return category_id;
	}

	public long getNumber() {
		return number;
	}

	public int getVersion() {
		return version;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getNbStars() {
		return nbStars;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
}
