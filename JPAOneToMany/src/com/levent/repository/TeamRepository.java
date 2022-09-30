package com.levent.repository;

import java.util.List;
import com.levent.entity.Team;

public interface TeamRepository {
	
	void saveTeam(Team team);
	List<Team> getAllTeams(); 
	Team findTeamById(int id);
	void deleteTeam(int id);
	void updateTeamName(int id, String name);
}
