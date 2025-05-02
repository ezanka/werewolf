package model;

import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private boolean gameOver = false;

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public List<Player> getAlivePlayers() {
        List<Player> alive = new ArrayList<>();
        for (Player player : players) {
            if (player.isAlive()) alive.add(player);
        }
        return alive;
    }

    public void killPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && player.isAlive()) {
                player.kill();
                break;
            }
        }
    }

    public boolean isGameOver() {
        long werewolves = players.stream().filter(p -> p.isAlive() && p.getRole() == Role.WEREWOLF).count();
        long villagers = players.stream().filter(p -> p.isAlive() && p.getRole() != Role.WEREWOLF).count();
        return werewolves == 0 || werewolves >= villagers;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
