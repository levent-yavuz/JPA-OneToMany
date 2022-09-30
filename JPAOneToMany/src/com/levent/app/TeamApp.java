package com.levent.app;

import com.levent.repository.PlayerRepository;
import com.levent.repository.PlayerRepositoryImpl;
import com.levent.repository.TeamRepository;
import com.levent.repository.TeamRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TeamApp {

	public static void main(String[] args) {
		//Entity manager
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAOneToManyUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
	
		TeamRepository teamRepository = new TeamRepositoryImpl(entityManager);
		PlayerRepository playerRepository = new PlayerRepositoryImpl(entityManager);	
		playerRepository.prepareData();
		
		System.out.println("Teams");
		teamRepository.getAllTeams().stream().forEach(System.out::println);
		System.out.println("Players");
		playerRepository.getAllPlayers().stream().forEach(System.out::println);
		
		System.out.println("\nThe player with the specified (ID = 2) is deleting..");
		playerRepository.deletePlayer(2);
		
		System.out.println("\nThe team with the specified (ID = 1) is deleting..");
		teamRepository.deleteTeam(1);
		
		System.out.println("The player's name (the specified ID = 5) is updating to 'Test Player'..");
		playerRepository.updatePlayerName(5, "Test Player");
		
		System.out.println("The team's name (the specified ID = 4) is updating to 'Test FC'..");
		teamRepository.updateTeamName(4, "Test FC");
		
		System.out.println("\nAll Teams After Delete and Update Operation");
		teamRepository.getAllTeams().stream().forEach(System.out::println);
		
		System.out.println("\nAll Teams After Delete and Update Operation");
		playerRepository.getAllPlayers().stream().forEach(System.out::println);
		
		//Close the entity manager and associated factory
        entityManager.close();
        entityManagerFactory.close();
	}
}
