package com.levent.repository;

import java.util.List;
import com.levent.entity.Player;
import com.levent.entity.PlayerType;
import com.levent.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Implements persistence methods for players.
 */
public class PlayerRepositoryImpl implements PlayerRepository{

	private EntityManager entityManager;

	public PlayerRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	 /**
     * Saves the specified player to the database.
     *
     * @param player The Player to save to the database.
     */
	@Override
	public void savePlayer(Player player) {
	
		entityManager.getTransaction().begin();
		entityManager.persist(player);
		entityManager.getTransaction().commit();
	}
	
	/**
     * Returns a list of all players in the database.
     *
     * @return A list of all players in the database.
     */
	@Override
	public List<Player> getAllPlayers() {
		
		List<Player> players =  entityManager.createQuery("Select p from Player p", Player.class).getResultList();
		
		return players;
	}
	
	/**
     * Finds the player with the specified ID.
     *
     * @param id The ID of the player to find.
     * @return The requested player
     */
	@Override
	public Player findPlayerById(int id) {
		
		Player player = entityManager.find(Player.class, id);	
		return player;
	}
	
	
	/**
     * Updates the player with the specified ID. This method updates the players's name and all references to its teams,
     *
     * @param id The ID of the player to update.
     * @param name The name of the player's updated new name.
     */
	@Override
	public void updatePlayerName(int id, String name) {
		
		entityManager.getTransaction().begin();
		
		Query updateQuery = entityManager.createQuery("Update Player p Set p.name =:p1 where p.id =:p2",Player.class);
		updateQuery.setParameter("p1", name);
		updateQuery.setParameter("p2", id);
		updateQuery.executeUpdate();
		
		Player player = entityManager.find(Player.class, id);
		player.setName(name);
		
		player.getTeam().getPlayers().remove(findPlayerById(id));
		player.getTeam().getPlayers().add(player);
		
		entityManager.getTransaction().commit();
	}
	
	/**
     * Deletes the player with the specified ID.
     *
     * @param id The ID of the player to delete.
     */
	@Override
	public void deletePlayer(int id) {
		
		Player p = findPlayerById(id);
		
		entityManager.getTransaction().begin();
		
		p.getTeam().getPlayers().remove(p);
		
		entityManager.remove(p);
		entityManager.getTransaction().commit();	
	}
	
	public void prepareData() {
		
		Team mCity = new Team("Manchester City", "The Sky Blues", "Blue and White", 6);
		Team lpool = new Team("Liverpool", "The Reds", "Red", 19);
		Team mUtd = new Team("Manchester United", "The Red Devils ", "Red and White", 20);
		Team che = new Team("Chelsea", "The Blues", "White and Blue", 6);
		
		Player pFoden = new Player("Phil Foden", 2000, "England", PlayerType.Midfielder);
		Player pSterling = new Player("Raheem Sterling", 1994, "England", PlayerType.Midfielder);
		Player pSalah = new Player("Muhammed Salah", 1992, "Egypt", PlayerType.Striker);
		Player pFirmino = new Player("Roberto Firmino", 1991, "Brasil", PlayerType.Striker);
		Player pKanté = new Player("N'Golo Kanté", 1991, "France", PlayerType.Midfielder);
		Player pArnold = new Player("Alexander-Arnold", 1998, "England", PlayerType.Defender);
		Player pAubameyang = new Player("Pierre Aubameyang", 1989, "France", PlayerType.Striker);
		Player pDeGea = new Player("David de Gea", 1990, "Spain", PlayerType.Goalkeeper);
		Player pRonaldo = new Player("Cristiano Ronaldo", 1985, "Portugal", PlayerType.Striker);
		Player pİlkay = new Player("İlkay Gündoğan", 1990, "Germany", PlayerType.Midfielder);
			
		pFoden.addTeam(mCity);
		pSterling.addTeam(mCity);
		pRonaldo.addTeam(mUtd);
		pAubameyang.addTeam(che);
		pDeGea.addTeam(mUtd);
		pArnold.addTeam(lpool);
		pSalah.addTeam(lpool);
		pİlkay.addTeam(mCity);
		pKanté.addTeam(che);
		pFirmino.addTeam(lpool);
		
		savePlayer(pFoden);
		savePlayer(pSterling);
		savePlayer(pSalah);
		savePlayer(pRonaldo);
		savePlayer(pKanté);
		savePlayer(pArnold);
		savePlayer(pAubameyang);
		savePlayer(pDeGea);
		savePlayer(pFirmino);
		savePlayer(pİlkay);
	}
}
