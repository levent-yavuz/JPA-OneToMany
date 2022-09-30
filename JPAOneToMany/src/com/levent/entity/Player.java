package com.levent.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private int  birthYear;
	private String country;
	
	@Enumerated(EnumType.STRING)
	private PlayerType playerTypes;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	//@JoinColumn(name = "team_id")
	private Team team = new Team();
	
	public Player() {
		super();
	}

	public Player(String name, int birthDate, String country, PlayerType playerTypes) {
		super();
		this.name = name;
		this.birthYear = birthDate;
		this.country = country;
		this.playerTypes = playerTypes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public PlayerType getPlayerTypes() {
		return playerTypes;
	}

	public void setPlayerTypes(PlayerType playerTypes) {
		this.playerTypes = playerTypes;
	}

	public Team getTeam() {
		return team;
	}

	public void addTeam(Team team) {
		this.team = team;
		team.getPlayers().add(this);
	}

	@Override
	public String toString() {
		return "Player [id = " + id + ", name = " + name + ", team = " + team.getName() + " ]";
	}
}
