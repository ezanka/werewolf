package UI;

import model.Player;
import model.Role;

import javax.swing.*;
import java.util.List;

public class Night {
    private boolean isIdentityConfirmed = false;
    private int currentIndex = 0;
    private final List<Player> players;
    private Player currentPlayer;

    private JPanel panel1;
    private JComboBox<String> comboBox1;
    private JButton voterButton;
    private JButton ouiButton;
    private JPanel IdentityPanel;
    private JPanel VotePanel;
    private JLabel confirmIdentity;
    private JLabel revealRole;

    public Night(List<Player> players) {
        this.players = players;

        ouiButton.addActionListener(e -> {
            isIdentityConfirmed = true;
            updatePanel();
        });

        voterButton.addActionListener(e -> {
            String selected = (String) comboBox1.getSelectedItem();

            if (selected != null && !selected.equals("---")) {
                String voteName = selected.split(" - ")[0];

                if (currentPlayer.getRole() == Role.WEREWOLF) {
                    for (Player p : players) {
                        if (p.getName().equals(voteName)) {
                            p.addVoie(); // ✅ méthode propre
                            System.out.println(p.getName() + " a maintenant " + p.getVoie() + " voie(s)");
                        }
                    }
                }

                // Ici tu peux aussi gérer la voyante ou le villageois
            }

            currentIndex++;
            isIdentityConfirmed = false;
            updatePanel();
        });

        updatePanel();
    }

    private void updatePanel() {
        if (currentIndex >= players.size()) {
            System.out.println("Tous les joueurs sont passés.");
            IdentityPanel.setVisible(false);
            VotePanel.setVisible(false);
            return;
        }

        currentPlayer = players.get(currentIndex);

        if (!isIdentityConfirmed) {
            IdentityPanel.setVisible(true);
            VotePanel.setVisible(false);

            Player currentPlayer = players.get(currentIndex);
            confirmIdentity.setText("Êtes-vous bien " + currentPlayer.getName() + " ?");
        } else {
            IdentityPanel.setVisible(false);
            VotePanel.setVisible(true);

            Player currentPlayer = players.get(currentIndex);
            revealRole.setText("Vous êtes " + currentPlayer.getRole());

            comboBox1.removeAllItems();
            comboBox1.addItem("---");
            for (Player p : players) {
                if(currentPlayer.getRole() == Role.WEREWOLF) {
                    if (!p.equals(currentPlayer) && p.getRole() != Role.WEREWOLF) {
                        comboBox1.addItem(p.getName() + " - " + p.getVoie() + " voie(s) contre lui.");
                    }
                } else if (currentPlayer.getRole() == Role.SEER) {
                    if (!p.equals(currentPlayer)) {
                        comboBox1.addItem(p.getName() + " - " + p.getRole());
                    }
                } else {
                    if (!p.equals(currentPlayer)) {
                        comboBox1.addItem(p.getName());
                    }
                }

            }
        }

        panel1.revalidate();
        panel1.repaint();
    }

    public JPanel getPanel() {
        return panel1;
    }
}
