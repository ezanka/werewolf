package UI;

import model.Game;
import model.Role;

import javax.swing.*;
import java.util.*;

public class Initial {
    private JButton button1;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JPanel MainPanel;

    public Initial(JFrame frame) {
        SpinnerModel playerSpinner = new SpinnerNumberModel(8, 8, 18, 1);
        SpinnerModel timeSpinner = new SpinnerNumberModel(60, 10, 300, 10);
        spinner1.setModel(playerSpinner);
        spinner2.setModel(timeSpinner);

        button1.addActionListener(e -> {
            Game game = new Game();
            int nbPlayers = (int) spinner1.getValue();

            for (int i = 1; i <= nbPlayers; i++) {
                game.addPlayer("Joueur " + i);
            }

            assignrole(game);

            Night night = new Night(game.getPlayers());
            frame.setContentPane(night.getPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    public void assignrole(Game game) {
        List<model.Player> players = game.getPlayers();
        int n = players.size();

        int nbWolf, nbVillager;
        int nbSeer = 1;

        switch (n) {
            case 8:  nbVillager = 5; nbWolf = 2; break;
            case 9:  nbVillager = 6; nbWolf = 2; break;
            case 10: nbVillager = 7; nbWolf = 2; break;
            case 11: nbVillager = 8; nbWolf = 2; break;
            case 12: nbVillager = 8; nbWolf = 3; break;
            case 13: nbVillager = 9; nbWolf = 3; break;
            case 14: nbVillager = 10; nbWolf = 3; break;
            case 15: nbVillager = 11; nbWolf = 3; break;
            case 16: nbVillager = 12; nbWolf = 3; break;
            case 17: nbVillager = 13; nbWolf = 3; break;
            case 18: nbVillager = 14; nbWolf = 3; break;
            default:
                throw new IllegalArgumentException("Nombre de joueurs non pris en charge : " + n);
        }

        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < nbWolf; i++) roles.add(Role.WEREWOLF);
        for (int i = 0; i < nbSeer; i++) roles.add(Role.SEER);
        for (int i = 0; i < nbVillager; i++) roles.add(Role.VILLAGER);

        Collections.shuffle(roles);

        for (int i = 0; i < n; i++) {
            players.get(i).assignRole(roles.get(i));
        }
    }


    public JPanel getPanel() {
        return MainPanel;
    }
}
