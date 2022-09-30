package com.levent.repository;

import java.util.List;

import com.levent.entity.Player;

public interface PlayerRepository {

	void savePlayer(Player player);
	List<Player> getAllPlayers();
	Player findPlayerById(int id);
	void updatePlayerName(int id, String name);
	void deletePlayer(int id);
	void prepareData();
}
