package UI;

import model.Game;

import javax.swing.*;

public class Finish {
    private JPanel panel1;
    private JLabel winnerLabel;
    private final Game game;


    public Finish(Game game, JFrame frame) {
        this.game = game;
        panel1.setVisible(true);

        Game.Winner result = game.checkWinCondition();
        if (result != Game.Winner.NONE) {
            showWinner(result);
            return;
        }
    }

    private void showWinner(Game.Winner result) {
        String message = (result == Game.Winner.WEREWOLVES)
                ? "Les loups-garous ont gagné !"
                : "Les villageois ont gagné !";

        winnerLabel.setText(message);
    }

    public JPanel getPanel() {
        return panel1;
    }
}
