import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();

        JFrame frame = new JFrame("HabitsGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel rankLabel = new JLabel("Rank: " + player.getRank(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel levelLabel = new JLabel("Level: " + player.getLevel(), SwingConstants.CENTER);
        JLabel xpLabel = new JLabel("XP: " + player.getXp(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        levelLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        xpLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        topPanel.add(rankLabel);
        topPanel.add(levelLabel);
        topPanel.add(xpLabel);


        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JLabel questLabel = new JLabel("vypít 2 litry vody");
        //dodelat omezeni jednou za 24 hodin
        JButton addXpButton = new JButton("Úkol splněn + 30xp");
        addXpButton.addActionListener((ActionEvent e) -> {
            player.addXp(30);
            rankLabel.setText("Rank: " + player.getRank());
            levelLabel.setText("Level: " + player.getLevel());
            xpLabel.setText("XP: " + player.getXp());
        });
        middlePanel.add(questLabel);
        middlePanel.add(addXpButton);


        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}