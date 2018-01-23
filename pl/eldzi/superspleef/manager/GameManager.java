package pl.eldzi.superspleef.manager;

import java.util.ArrayList;

import pl.eldzi.superspleef.game.Game;

public class GameManager {

	private static ArrayList<Game> games = new ArrayList<>();

	private static ArrayList<Game> build_game = new ArrayList<>();

	public static ArrayList<Game> getGames() {
		return games;
	}

	public static ArrayList<Game> getBuildGame() {
		return build_game;
	}
}
