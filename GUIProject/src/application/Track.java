package application;

public class Track {
	
	private int id;
	private String name;
	private String country;
	private int length;
	
	public Track(int id, String name, String country, int length) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.length = length;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public int getLength() {
		return length;
	}
	
	

}
