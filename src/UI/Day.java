package UI;

import model.Game;
import model.Phase;
import model.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Day {
    private JPanel panel1;
    private JButton skip;
    private JPanel cooldownPanel;
    private JButton revealFinishButton;
    private JLabel deadLabel;
    private JPanel deadPanel;
    private final Game game;

    private boolean deadReveal = false;

    public Day(Game game, JFrame frame) {
        this.game = game;

        revealFinishButton.addActionListener(e -> {
            deadReveal = true;
            updatePanel();
        });

        skip.addActionListener(e -> {
            game.incrementDay();
            frame.setTitle("WereWolf - " + Phase.Night + " " + game.getCountNight());

            Night night = new Night(game, game.getAlivePlayers(), frame);
            frame.setContentPane(night.getPanel());
            frame.revalidate();
            frame.repaint();
        });

        updatePanel();
    }

    private void updatePanel() {
        if (!deadReveal) {
            setWolfDecision();
            deadPanel.setVisible(true);
            cooldownPanel.setVisible(false);
        } else {
            deadPanel.setVisible(false);
            cooldownPanel.setVisible(true);
        }
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void setWolfDecision() {
        int maxVotes = -1;
        List<Player> topVotedPlayers = new ArrayList<>();

        for (Player p : game.getAlivePlayers()) {
            if (p.getVoie() > maxVotes) {
                maxVotes = p.getVoie();
                topVotedPlayers.clear();
                topVotedPlayers.add(p);
            } else if (p.getVoie() == maxVotes) {
                topVotedPlayers.add(p);
            }
        }

        String message;

        if (maxVotes == 0 || topVotedPlayers.size() > 1) {
            message = "Aucun joueur n'a été dévoré cette nuit.";
        } else {
            Player eliminatedPlayer = topVotedPlayers.get(0);
            message = eliminatedPlayer.getName() + " a été dévoré par les loups.";
            eliminatedPlayer.kill();
        }

        deadLabel.setText(message);
    }
}
