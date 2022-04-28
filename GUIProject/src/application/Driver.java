package application;

import java.util.Date;

public class Driver {

	private int id;
	private String name;
	private Date dob;
	private String country;
	private String team;
	
	public Driver(int id, String name, Date dob, String country, String team) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.country = country;
		this.team = team;
	}
	
	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}
	
}

