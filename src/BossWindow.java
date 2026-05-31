import javax.swing.*;
import java.awt.*;

// okno s bossama, podobne jako arena ale tezsi
public class BossWindow {
    // velke odmeny za vyhru
    private static final int[] BOSS_WIN_XP = {250, 600, 1200};
    private static final int[] BOSS_WIN_GOLD = {150, 400, 800};
    private static final int LOSS_XP = 50;
    private static final int LOSS_GOLD = 20;

    private final Player player;
    private final Runnable onPlayerChanged;
    private final JFrame frame;
    private final Opponent opponent;
    private final JLabel playerStatsLabel;
    private final JLabel opponentStatsLabel;
    private final JLabel battleLogLabel;
    private final JLabel rulesLabel;
    private final JButton attackButton;
    private final JButton fightAgainButton;
    private final JButton[] bossButtons;

    private int selectedBoss = 1;
    private int playerCurrentHp;
    private final int playerMaxHp;
    private final int playerAttack;
    private final int playerDefense;
    private boolean battleFinished;

    // vytvori okno bossu a zacne prvni souboj
    public BossWindow(Player player, Runnable onPlayerChanged) {
        this.player = player;
        this.onPlayerChanged = onPlayerChanged;
        this.frame = new JFrame("Bossové");
        this.playerAttack = player.getTotalAttack();
        this.playerDefense = player.getTotalDefense();
        this.playerMaxHp = player.getTotalHp();
        this.opponent = new Opponent(playerAttack, playerDefense, playerMaxHp);
        this.playerStatsLabel = new JLabel();
        this.opponentStatsLabel = new JLabel();
        this.battleLogLabel = new JLabel(" ");
        this.rulesLabel = new JLabel(buildRulesText());
        this.attackButton = new JButton("Zaútočit");
        this.fightAgainButton = new JButton("Bojovat znovu");
        this.fightAgainButton.setEnabled(false);
        this.bossButtons = new JButton[Opponent.getBossCount()];
        for (int i = 0; i < bossButtons.length; i++) {
            int bossNumber = i + 1;
            bossButtons[i] = new JButton("Boss " + bossNumber + " — "
                    + Opponent.getBossHp(bossNumber) + " HP, ~"
                    + Opponent.getBossAttack(bossNumber) + "/"
                    + Opponent.getBossDefense(bossNumber) + " útok/obrana");
            bossButtons[i].setToolTipText("Odměna: +" + BOSS_WIN_XP[i] + " XP, +" + BOSS_WIN_GOLD[i] + " gold");
        }
        startNewBattle();
        setupUi();
    }

    // pravidla bossu nahore v okne
    private String buildRulesText() {
        return "<html>Pravidla bossů:<br>"
                + "• Boss 1: <b>300 HP, ~12/3</b> | Boss 2: <b>700 HP, ~22/8</b> | Boss 3: <b>1500 HP, ~38/15</b> (útok/obrana ± offset)<br>"
                + "• Odměny: Boss 1 <b>+250 XP, +150 gold</b> | Boss 2 <b>+600 XP, +400 gold</b> | Boss 3 <b>+1200 XP, +800 gold</b><br>"
                + "• Prohra: <b>-" + LOSS_XP + " XP</b>, <b>-" + LOSS_GOLD + " gold</b></html>";
    }

    // posklada a vykresli okno
    private void setupUi() {
        frame.setSize(640, 680);
        frame.setMinimumSize(new Dimension(640, 680));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        UiTheme.applyToFrame(frame);

        JPanel contentPanel = UiTheme.createCardPanel(null);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = UiTheme.createTitleLabel("Souboj s bossem");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        rulesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesLabel.setForeground(UiTheme.TEXT_MUTED);
        rulesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(rulesLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(UiTheme.createSectionLabel("Vyber bosse:"));
        contentPanel.add(Box.createVerticalStrut(6));

        JPanel bossPanel = new JPanel();
        bossPanel.setLayout(new BoxLayout(bossPanel, BoxLayout.Y_AXIS));
        bossPanel.setOpaque(false);
        bossPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < bossButtons.length; i++) {
            int bossNumber = i + 1;
            UiTheme.styleSecondaryButton(bossButtons[i]);
            bossButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            bossButtons[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            bossButtons[i].addActionListener(e -> selectBoss(bossNumber));
            bossPanel.add(bossButtons[i]);
            bossPanel.add(Box.createVerticalStrut(6));
        }
        contentPanel.add(bossPanel);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(UiTheme.createSectionLabel("Tvoje statistiky:"));
        contentPanel.add(Box.createVerticalStrut(4));
        playerStatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerStatsLabel.setForeground(UiTheme.TEXT);
        playerStatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(playerStatsLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(UiTheme.createSectionLabel("Boss:"));
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
        fightAgainButton.addActionListener(e -> startNewBattle());
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

        updateBossButtonStyles();
    }

    // prepne na jinyho bossa
    private void selectBoss(int bossNumber) {
        if (battleFinished || !attackButton.isEnabled()) {
            selectedBoss = bossNumber;
            updateBossButtonStyles();
            startNewBattle();
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Opravdu chceš změnit bosse? Probíhající souboj se zruší.",
                "Změna bosse",
                JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        selectedBoss = bossNumber;
        updateBossButtonStyles();
        startNewBattle();
    }

    private void updateBossButtonStyles() {
        for (int i = 0; i < bossButtons.length; i++) {
            if (i + 1 == selectedBoss) {
                UiTheme.stylePrimaryButton(bossButtons[i]);
            } else {
                UiTheme.styleSecondaryButton(bossButtons[i]);
            }
        }
    }

    // nova bitva, plne HP
    private void startNewBattle() {
        playerCurrentHp = playerMaxHp;
        battleFinished = false;
        attackButton.setEnabled(true);
        fightAgainButton.setEnabled(false);
        opponent.resetAsBoss(selectedBoss);
        battleLogLabel.setText("Souboj s " + opponent.getName() + " začíná. Klikni na Zaútočit.");
        refreshStats();
    }

    // utok hrace, pak boss protitutok
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
                "Zasadil jsi " + damageToOpponent + " poškození. Boss ti vrátil " + damageToPlayer + " poškození."
        );
        refreshStats();

        if (playerCurrentHp <= 0) {
            finishBattle(false);
        }
    }

 //utok minus obrana, minimum 1
    private int calculateDamage(int attack, int defense) {
        return Math.max(1, attack - defense);
    }

    // ukonci souboj, da odmenu nebo minus
    private void finishBattle(boolean playerWon) {
        battleFinished = true;
        attackButton.setEnabled(false);
        fightAgainButton.setEnabled(true);

        if (playerWon) {
            int winXp = BOSS_WIN_XP[selectedBoss - 1];
            int winGold = BOSS_WIN_GOLD[selectedBoss - 1];
            player.addGold(winGold);
            player.addXp(winXp);
            if (onPlayerChanged != null) {
                onPlayerChanged.run();
            }
            JOptionPane.showMessageDialog(
                    frame,
                    "Porazil jsi " + opponent.getName() + "! +" + winXp + " XP, +" + winGold + " gold",
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

// odebere xp a gold po prohre, bez goldu 2x vic minus
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

    // aktualizuje staty hrace a bosse
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
