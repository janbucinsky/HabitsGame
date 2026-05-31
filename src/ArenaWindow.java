import javax.swing.*;
import java.awt.*;
import java.util.function.IntSupplier;

public class ArenaWindow {
    private static final int WIN_GOLD = 25;
    private static final int WIN_XP = 50;
    private static final int LOSS_XP = 50;
    private static final int LOSS_GOLD = 20;

    private final Player player;
    private final Runnable onPlayerChanged;
    private final IntSupplier consumeArenaGame;
    private final JFrame frame;
    private Opponent opponent;
    private final JLabel playerStatsLabel;
    private final JLabel opponentStatsLabel;
    private final JLabel battleLogLabel;
    private final JLabel remainingGamesLabel;
    private final JLabel rulesLabel;
    private final JButton attackButton;
    private final JButton fightAgainButton;

    private int playerCurrentHp;
    private final int playerMaxHp;
    private final int playerAttack;
    private final int playerDefense;
    private boolean battleFinished;

    public ArenaWindow(Player player, Runnable onPlayerChanged, int remainingGames, IntSupplier consumeArenaGame) {
        this.player = player;
        this.onPlayerChanged = onPlayerChanged;
        this.consumeArenaGame = consumeArenaGame;
        this.frame = new JFrame("Aréna");
        this.playerAttack = player.getTotalAttack();
        this.playerDefense = player.getTotalDefense();
        this.playerMaxHp = player.getTotalHp();

        this.opponent = new Opponent(playerAttack, playerDefense, playerMaxHp);
        this.playerStatsLabel = new JLabel();
        this.opponentStatsLabel = new JLabel();
        this.battleLogLabel = new JLabel(" ");

        this.remainingGamesLabel = new JLabel("Zbývá her dnes: " + remainingGames);
        this.rulesLabel = new JLabel(buildRulesText());
        this.attackButton = new JButton("Zaútočit");
        this.fightAgainButton = new JButton("Bojovat znovu");
        this.fightAgainButton.setEnabled(false);
        startNewBattle();
        setupUi();
    }

    private String buildRulesText() {
        
        return "<html>Pravidla arény:<br>"
                + "• Můžeš bojovat pouze <b>10× denně</b><br>"
                + "• Výhra: <b>+" + WIN_XP + " XP</b>, <b>+" + WIN_GOLD + " gold</b><br>"
                + "• Prohra: <b>-" + LOSS_XP + " XP</b>, <b>-" + LOSS_GOLD + " gold</b><br>"
                + "• Pokud nemáš goldy: <b>2× více minus XP</b> (-" + (LOSS_XP * 2) + " XP)</html>";
    }

    private void setupUi() {
        frame.setSize(560, 520);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        UiTheme.applyToFrame(frame);

        JPanel contentPanel = UiTheme.createCardPanel(null);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = UiTheme.createTitleLabel("Souboj v aréně");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        rulesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesLabel.setForeground(UiTheme.TEXT_MUTED);
        rulesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(rulesLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        remainingGamesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        remainingGamesLabel.setForeground(UiTheme.TEXT);
        remainingGamesLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contentPanel.add(remainingGamesLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(UiTheme.createSectionLabel("Tvoje statistiky:"));
        contentPanel.add(Box.createVerticalStrut(4));
        playerStatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerStatsLabel.setForeground(UiTheme.TEXT);
        playerStatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(playerStatsLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(UiTheme.createSectionLabel("Soupeř:"));
        contentPanel.add(Box.createVerticalStrut(4));
        opponentStatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        opponentStatsLabel.setForeground(UiTheme.TEXT);
        opponentStatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(opponentStatsLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        battleLogLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel logPanel = UiTheme.createLogPanel(battleLogLabel);
        logPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        contentPanel.add(logPanel);
        contentPanel.add(Box.createVerticalStrut(14));

        attackButton.addActionListener(e -> playerAttackTurn());
        fightAgainButton.addActionListener(e -> fightAgain());
        UiTheme.stylePrimaryButton(attackButton);
        UiTheme.styleSecondaryButton(fightAgainButton);
        attackButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        fightAgainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(attackButton);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(fightAgainButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        wrapper.add(contentPanel, BorderLayout.CENTER);
        frame.add(wrapper, BorderLayout.CENTER);
    }

    private void startNewBattle() {
        playerCurrentHp = playerMaxHp;
        battleFinished = false;
        attackButton.setEnabled(true);
        fightAgainButton.setEnabled(false);
        opponent.resetOpponent(playerMaxHp);
        battleLogLabel.setText("Souboj začíná. Klikni na Zaútočit.");
        refreshStats();
    }

    private void fightAgain() {
        int remaining = consumeArenaGame.getAsInt();
        if (remaining < 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Dnes už jsi odehrál maximálně 10 her v aréně.",
                    "Denní limit arény",
                    JOptionPane.INFORMATION_MESSAGE
            );
            fightAgainButton.setEnabled(false);
            return;
        }

        remainingGamesLabel.setText("Zbývá her dnes: " + remaining);
        startNewBattle();
    }

    private void playerAttackTurn() {
        if (battleFinished) {
            return;
        }

        int damageToOpponent = calculateDamage(playerAttack, opponent.getDefense());
        opponent.takeDamage(damageToOpponent);
        battleLogLabel.setText("Zasadil jsi " + damageToOpponent + " poškození.");
        refreshStats();

        if (!opponent.isAlive()) {
            finishBattle(true);
            return;
        }

        opponent.rollStats();
        int damageToPlayer = calculateDamage(opponent.getAttack(), playerDefense);
        playerCurrentHp = Math.max(0, playerCurrentHp - damageToPlayer);
        battleLogLabel.setText(
                "Zasadil jsi " + damageToOpponent + " poškození. Soupeř ti vrátil " + damageToPlayer + " poškození."
        );
        refreshStats();

        if (playerCurrentHp <= 0) {
            finishBattle(false);
        }
    }

    private int calculateDamage(int attack, int defense) {
        return Math.max(1, attack - defense);
    }

    private void finishBattle(boolean playerWon) {
        battleFinished = true;
        attackButton.setEnabled(false);
        fightAgainButton.setEnabled(true);

        if (playerWon) {
            player.addGold(WIN_GOLD);
            player.addXp(WIN_XP);
            if (onPlayerChanged != null) {
                onPlayerChanged.run();
            }
            JOptionPane.showMessageDialog(
                    frame,
                    "Vyhrál jsi! +" + WIN_XP + " XP, +" + WIN_GOLD + " gold",
                    "Vítězství",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            applyLossPenalty();
            if (onPlayerChanged != null) {
                onPlayerChanged.run();
            }
        }
    }

    private void applyLossPenalty() {
        boolean noGold = player.getGold() <= 0;
        int xpLoss = noGold ? LOSS_XP * 2 : LOSS_XP;

        player.removeXp(xpLoss);
        if (!noGold) {
            player.removeGold(LOSS_GOLD);
        }

        String message;
        if (noGold) {
            message = "Prohrál jsi souboj. Nemáš goldy, takže -" + xpLoss + " XP (2× více).";
        } else {
            message = "Prohrál jsi souboj. -" + xpLoss + " XP, -" + LOSS_GOLD + " gold.";
        }

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Prohra",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void refreshStats() {
        playerStatsLabel.setText(
                "Útok: " + playerAttack + "  |  Obrana: " + playerDefense
                        + "  |  HP: " + playerCurrentHp + "/" + playerMaxHp
        );
        opponentStatsLabel.setText(opponent.getName() + " — " + opponent.getStatsText());
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}
