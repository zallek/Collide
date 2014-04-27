package com.zallek.collide.db.category;

public class Category {
	// Notez que l'identifiant est un long
	private long id;
	private String name;
	
	public Category(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
