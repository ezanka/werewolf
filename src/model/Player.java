package model;

public class Player {
    private String name;
    private Role role;
    private boolean alive = true;
    private int voie = 0;
    private boolean revealed = false;

    public Player(String name) {
        this.name = name;
    }
    


    public String getName() { return name; }
    public int getVoie() { return voie; }
    public void setVoie(int voie) { this.voie = voie; }
    public void addVoie() { this.voie++; }
    public Role getRole() { return role; }
    public boolean isAlive() { return alive; }
    public void kill() { this.alive = false; }
    public boolean isRevealed() { return revealed; }
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    @Override
    public String toString() {
        return name + " (" + role + ") - " + (alive ? "vivant" : "mort");
    }

    public void assignRole(Role role) {
        this.role = role;
    }
}
