package com.levent.repository;


import java.util.List;
import com.levent.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Implements persistence methods for team.
 */
public class TeamRepositoryImpl implements TeamRepository {

	private EntityManager entityManager;
	
	public TeamRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	public TeamRepositoryImpl() {
		super();
	}
	
	/**
     * Saves the specified team to the database.
     *
     * @param team The Team to save to the database.
     */
	@Override
	public void saveTeam(Team team) {
		
		entityManager.getTransaction().begin();
		entityManager.persist(team);
		entityManager.getTransaction().commit();
	}
	
	/**
     * Returns a list of all teams in the database.
     *
     * @return A list of all teams in the database.
     */
	@Override
	public List<Team> getAllTeams() {
		
		List<Team> teams =  entityManager.createQuery("Select t from Team t", Team.class).getResultList();
		
		return teams;
	}

	/**
     * Finds the team with the specified ID.
     *
     * @param id The ID of the team to find.
     * @return The requested team
     */
	@Override
	public Team findTeamById(int id) {
		
		Team team = entityManager.find(Team.class, id);	
		
		return team;
	}

	/**
     * Updates the team with the specified ID. This method updates the team's name and all references to its players,
     *
     * @param id The ID of the team to update.
     * @param name The name of the team's updated new name.
     */
	@Override
	public void updateTeamName(int id, String name) {
		
		entityManager.getTransaction().begin();
		
		Query updateQuery = entityManager.createQuery("Update Team t Set t.name = :p1 Where t.id = :p2", Team.class).setParameter("p1", name)
				.setParameter("p2",id);
		updateQuery.executeUpdate();
		
		Team t = findTeamById(id);
		t.getPlayers().forEach( p -> {
			p.getTeam().setName(name);
		});
		
		entityManager.getTransaction().commit();
	}
	
	/**
     * Deletes the team with the specified ID.
     *
     * @param id The ID of the team to delete.
     */
	@Override
	public void deleteTeam(int id) {
			
		entityManager.getTransaction().begin();
		entityManager.remove(findTeamById(id));
		entityManager.getTransaction().commit();
	}
}
