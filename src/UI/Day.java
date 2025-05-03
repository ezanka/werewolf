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
    private JLabel cooldownText;
    private JButton voterButton;
    private JComboBox comboBox1;
    private JPanel votePanel;
    private JPanel expluserPanel;
    private JButton leVillageSEndortButton;
    private JLabel expulserLabel;
    private final Game game;
    private boolean timerFinished = false;

    private boolean deadReveal = false;

    public Day(Game game, JFrame frame) {
        this.game = game;

        revealFinishButton.addActionListener(e -> {
            deadReveal = true;
            updatePanel(frame);
        });

        skip.addActionListener(e -> {
            timerFinished = true;
            votePhase(frame);
            deadPanel.setVisible(false);
            cooldownPanel.setVisible(false);
            votePanel.setVisible(true);
        });

        updatePanel(frame);
    }

    private void goToNightPhase(JFrame frame) {
        game.incrementDay();
        frame.setTitle("WereWolf - " + Phase.Night + " " + game.getCountNight());

        Night night = new Night(game, game.getAlivePlayers(), frame);
        frame.setContentPane(night.getPanel());
        frame.revalidate();
        frame.repaint();
    }

    private void updatePanel(JFrame frame) {
        if (!deadReveal) {
            setWolfDecision();
            deadPanel.setVisible(true);
            cooldownPanel.setVisible(false);
            votePanel.setVisible(false);
            expluserPanel.setVisible(false);
        } else if (!timerFinished) {
            deadPanel.setVisible(false);
            cooldownPanel.setVisible(true);
            votePanel.setVisible(false);
            expluserPanel.setVisible(false);

            int[] remaining = {game.getCooldown()};

            cooldownText.setText(remaining[0] + " secondes");

            Timer timer = new Timer(1000, null);
            timer.addActionListener(e -> {
                remaining[0]--;
                if (remaining[0] <= 0) {
                    timer.stop();
                    timerFinished = true;
                    votePhase(frame);
                    deadPanel.setVisible(false);
                    cooldownPanel.setVisible(false);
                    votePanel.setVisible(true);
                    expluserPanel.setVisible(false);
                } else {
                    cooldownText.setText(remaining[0] + " secondes");
                }
            });

            timer.start();
        }
            leVillageSEndortButton.addActionListener(e -> {
                Game.Winner result = game.checkWinCondition();
                if (result != Game.Winner.NONE) {
                    Finish finish = new Finish(game, frame);
                    frame.setTitle("WereWolf - " + Phase.Finished);
                    frame.setContentPane(finish.getPanel());
                    frame.revalidate();
                    frame.repaint();
                    return;
                } else {
                    goToNightPhase(frame);
                }

            });
    }

    public void votePhase(JFrame frame) {
        for (Player player : game.getAlivePlayers()) {
            comboBox1.addItem(player.getName());
        }



        voterButton.addActionListener(e -> {
            String selected = (String) comboBox1.getSelectedItem();
            int voteIndex = Integer.parseInt(selected.split(" ")[1]);

            for (Player player : game.getAlivePlayers()) {
                if (player.getName().equals(selected)) {
                    player.kill();
                    expulserPhase(player);
                }
            }
        });
    }

    private void expulserPhase(Player player) {
        deadPanel.setVisible(false);
        cooldownPanel.setVisible(false);
        votePanel.setVisible(false);
        expluserPanel.setVisible(true);

        expulserLabel.setText(player.getName() + " a été expulser du village, il était " + player.getRole());
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
            message = eliminatedPlayer.getName() + " a été dévoré par les loups. Il était " + eliminatedPlayer.getRole();
            eliminatedPlayer.kill();
        }

        deadLabel.setText(message);

    }
}
