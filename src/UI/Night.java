package UI;

import model.Game;
import model.Phase;
import model.Player;
import model.Role;

import javax.swing.*;
import java.util.List;

public class Night {
    private boolean isIdentityConfirmed = false;
    private int currentIndex = 0;
    private final List<Player> players;
    private Player currentPlayer;
    private boolean hasSeenAsSeer = false;

    private JPanel panel1;
    private JComboBox<String> comboBox1;
    private JButton voterButton;
    private JButton ouiButton;
    private JPanel IdentityPanel;
    private JPanel VotePanel;
    private JLabel confirmIdentity;
    private JLabel revealRole;
    private JButton joueurSuivantButton;
    private JPanel seerPanel;
    private JLabel seerRevealLabel;
    private JFrame frame;
    private final Game game;

    private void handleWerewolfVote(String voteName) {
        for (Player p : players) {
            if (p.getName().equals(voteName)) {
                p.addVoie();
            }
        }
    }

    private void handleSeerAction(String cibleName) {
        Player cible = players.stream()
                .filter(p -> p.getName().equals(cibleName))
                .findFirst()
                .orElse(null);

        if (cible != null) {
            cible.setRevealed(true);
            seerRevealLabel.setText(cible.getName() + " est un " + cible.getRole());
        }
    }

    public Night(Game game, List<Player> players, JFrame frame) {
        this.game = game;
        this.players = players;
        this.frame = frame;

        ouiButton.addActionListener(e -> {
            isIdentityConfirmed = true;
            updatePanel(frame);
        });

        joueurSuivantButton.addActionListener(e -> {
            hasSeenAsSeer = false;
            isIdentityConfirmed = false;
            updatePanel(frame);
        });



        voterButton.addActionListener(e -> {
            String selected = (String) comboBox1.getSelectedItem();
//          a rajouter dans le if pour faire le vote blanc  && !selected.equals("---")
            if (selected != null ) {
                String voteName = selected.split(" - ")[0];

                if (currentPlayer.getRole() == Role.WEREWOLF) {
                    handleWerewolfVote(voteName);
                } else if (currentPlayer.getRole() == Role.SEER) {
                    hasSeenAsSeer = true;
                    handleSeerAction(voteName);
                }

                currentIndex++;
                isIdentityConfirmed = false;
                updatePanel(frame);
            }
        });

        updatePanel(frame);
    }

    private void updatePanel(JFrame frame) {
        if (currentIndex >= players.size()) {
            if (!hasSeenAsSeer) {

                Game.Winner result = game.checkWinCondition();
                if (result != Game.Winner.NONE) {
                    goToFinishScreen(result);
                    return;
                } else {
                    IdentityPanel.setVisible(false);
                    VotePanel.setVisible(false);
                    seerPanel.setVisible(false);

                    game.incrementNight();
                    frame.setTitle("WereWolf - " + Phase.Day + " " + game.getCountDay());

                    Day day = new Day(game, frame);
                    frame.setContentPane(day.getPanel());
                    frame.revalidate();
                    frame.repaint();
                    return;
                }


            } else {
                IdentityPanel.setVisible(false);
                VotePanel.setVisible(false);
                seerPanel.setVisible(true);
                return;
            }
        }

        currentPlayer = players.get(currentIndex);

        if (hasSeenAsSeer) {
            IdentityPanel.setVisible(false);
            VotePanel.setVisible(false);
            seerPanel.setVisible(true);
            return;
        }

        if (!isIdentityConfirmed) {
            IdentityPanel.setVisible(true);
            VotePanel.setVisible(false);
            seerPanel.setVisible(false);
            confirmIdentity.setText("Êtes-vous bien " + currentPlayer.getName() + " ?");
        } else {
            IdentityPanel.setVisible(false);
            VotePanel.setVisible(true);
            seerPanel.setVisible(false);
            revealRole.setText("Vous êtes " + currentPlayer.getRole());

            comboBox1.removeAllItems();
            comboBox1.addItem("---");
            for (Player p : players) {
                if (!p.equals(currentPlayer) && p.isAlive()) {
                    if (currentPlayer.getRole() == Role.WEREWOLF && p.getRole() != Role.WEREWOLF) {
                        comboBox1.addItem(p.getName() + " - " + p.getVoie() + " voie(s) contre lui.");
                    } else if (currentPlayer.getRole() == Role.SEER && p.isAlive()) {
                        if (p.isRevealed()) {
                            comboBox1.addItem(p.getName() + " - " + p.getRole());
                        } else {
                            comboBox1.addItem(p.getName());
                        }
                    } else if (currentPlayer.getRole() == Role.VILLAGER) {
                        comboBox1.addItem(p.getName());
                    }
                }
            }
        }

        panel1.revalidate();
        panel1.repaint();
    }

    private void goToFinishScreen(Game.Winner result) {
        IdentityPanel.setVisible(false);
        VotePanel.setVisible(false);
        seerPanel.setVisible(false);

        frame.setTitle("WereWolf - " + Phase.Finished);
        Finish finish = new Finish(game, frame);
        frame.setContentPane(finish.getPanel());
        frame.revalidate();
        frame.repaint();
    }


    public JPanel getPanel() {
        return panel1;
    }
}
