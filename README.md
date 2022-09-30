# JPA/EclipseLink Project - OneToMany Bidirectional Relationship

## JPA @OneToMany - Bidirectional

Bidirectional relationships can implement with or without join table.

To don't use Join Table, OneToMany and mappedBy are used on the inverse side and ManyToOne and JoinColumn are used together on the owner side. This way is the better performance.


## JPA @OneToMany - Unidirectional

By default, a unidirectional @OneToMany relationship will use a join table.

OneToMany and JoinColumn is used together to don't use Join Table.

To use join table, it's enough to just only OneToMany annotation. No need to use @JoinTable annotation. In this case, the join table will be created automatically.However, this is a loss of performance because extra sql statements are running in this usage. (2(one and many side) insert/update.. statements + 1 (join table) insert/update..

If we want to use an existing join table, we use it together with @JoinTable.

## FetchType.LAZY vs FetchType.Eager
```
LAZY = fetch when needed

EAGER = fetch immediately
```
We have two entities as Team and Player.
```
public class Team {
 private int id;
 private String name;
 private String address;
 private List<Player> players;
```
Lazy loading is technique to defer initialization of an object until the point at which it is needed. It can contribute to efficiency in the program's operation if properly and appropriately used. When a team has large number of players, it is not efficient to load along when all players are not needed. In such cases, players should be initiated when they are really needed. This is called lazy loading.

The opposite of lazy loading, which defers initialization of an object until the object is needed. 
Eager loading initializes an object upon creation. So in the Team and List<Player> example -> all players are fetched while Team is fetched from the database.

## Team Class
```
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
```
## Player Class
```
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
 ``` 
  ## Player Repository - Update and Delete Methods
  ```
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
  ```
  ## App Class and Output
  ```
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
  ```
  ```
  Teams
Team [id = 1, name = Manchester City players = [Phil Foden, İlkay Gündoğan, Raheem Sterling] ]
Team [id = 2, name = Liverpool players = [Muhammed Salah, Alexander-Arnold, Roberto Firmino] ]
Team [id = 3, name = Manchester United players = [Cristiano Ronaldo, David de Gea] ]
Team [id = 4, name = Chelsea players = [N'Golo Kanté, Pierre Aubameyang] ]
Players
Player [id = 1, name = Raheem Sterling, team = Manchester City ]
Player [id = 2, name = İlkay Gündoğan, team = Manchester City ]
Player [id = 3, name = Phil Foden, team = Manchester City ]
Player [id = 4, name = Muhammed Salah, team = Liverpool ]
Player [id = 5, name = Roberto Firmino, team = Liverpool ]
Player [id = 6, name = Alexander-Arnold, team = Liverpool ]
Player [id = 7, name = David de Gea, team = Manchester United ]
Player [id = 8, name = Cristiano Ronaldo, team = Manchester United ]
Player [id = 9, name = N'Golo Kanté, team = Chelsea ]
Player [id = 10, name = Pierre Aubameyang, team = Chelsea ]

The player with the specified (ID = 2) is deleting..

The team with the specified (ID = 1) is deleting..
The player's name (the specified ID = 5) is updating to 'Test Player'..
The team's name (the specified ID = 4) is updating to 'Test FC'..

All Teams After Delete and Update Operation
Team [id = 2, name = Liverpool players = [Muhammed Salah, Alexander-Arnold, Test Player] ]
Team [id = 3, name = Manchester United players = [Cristiano Ronaldo, David de Gea] ]
Team [id = 4, name = Test FC players = [N'Golo Kanté, Pierre Aubameyang] ]

All Teams After Delete and Update Operation
Player [id = 4, name = Muhammed Salah, team = Liverpool ]
Player [id = 5, name = Test Player, team = Liverpool ]
Player [id = 6, name = Alexander-Arnold, team = Liverpool ]
Player [id = 7, name = David de Gea, team = Manchester United ]
Player [id = 8, name = Cristiano Ronaldo, team = Manchester United ]
Player [id = 9, name = N'Golo Kanté, team = Test FC ]
Player [id = 10, name = Pierre Aubameyang, team = Test FC ]
  ```
