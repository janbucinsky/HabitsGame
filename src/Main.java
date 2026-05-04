import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int currentLevel = 1;
        String rank = RankManager.getRank(currentLevel);

        JFrame frame = new JFrame("HabitsGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new GridBagLayout());


        JLabel label = new JLabel("LEVEL " + currentLevel + rank);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 50));

        frame.add(label);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}