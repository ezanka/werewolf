package model;

import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();

    private int dayCount = 1;
    private int nightCount = 1;
    private int cooldown;

    public enum Winner {
        NONE, VILLAGERS, WEREWOLVES
    }

    public Winner checkWinCondition() {
        long aliveWolves = players.stream().filter(p -> p.isAlive() && p.getRole() == Role.LOUP_GAROU).count();

        long aliveVillagers = players.stream().filter(p -> p.isAlive() && (p.getRole() == Role.VILLAGEOIS || p.getRole() == Role.VOYANTE)).count();

        if (aliveWolves == 0) {
            return Winner.VILLAGERS;
        } else if (aliveWolves >= aliveVillagers) {
            return Winner.WEREWOLVES;
        }

        return Winner.NONE;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void incrementDay() {
        dayCount++;
    }

    public void incrementNight() {
        nightCount++;
    }

    public int getCountDay() {
        return dayCount;
    }

    public int getCountNight() {
        return nightCount;
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
        long werewolves = players.stream().filter(p -> p.isAlive() && p.getRole() == Role.LOUP_GAROU).count();
        long villagers = players.stream().filter(p -> p.isAlive() && p.getRole() != Role.LOUP_GAROU).count();
        return werewolves == 0 || werewolves >= villagers;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
