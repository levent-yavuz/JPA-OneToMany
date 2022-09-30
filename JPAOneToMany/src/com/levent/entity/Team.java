package com.levent.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String nickName;
	private String shirtColor;
	private int championshipsWon;
	
	@OneToMany(mappedBy="team",cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<Player> players = new HashSet<>();
	
	public Team() {
		super();
	}

	public Team(String name, String nickName, String shirtColor, int championshipsWon) {
		super();
		this.name = name;
		this.nickName = nickName;
		this.shirtColor = shirtColor;
		this.championshipsWon = championshipsWon;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getShirtColor() {
		return shirtColor;
	}

	public void setShirtColor(String shirtColor) {
		this.shirtColor = shirtColor;
	}

	public int getChampionshipsWon() {
		return championshipsWon;
	}

	public void setChampionshipsWon(int championshipsWon) {
		this.championshipsWon = championshipsWon;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void addPlayers(Player player) {
		this.players.add(player);
		player.addTeam(this);
	}

	@Override
	public String toString() {
		return "Team [id = " + id + ", name = " + name + " players = " + players.stream().map(Player::getName ).collect(Collectors.toList())+  " ]";
	}
}
