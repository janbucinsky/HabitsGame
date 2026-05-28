import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow {
    private final Player player;
    private final JFrame frame;
    private final DefaultComboBoxModel<String> activityModel;
    private final DefaultListModel<String> customActivityModel;

    public MainWindow(Player player) {
        this.player = player;
        this.frame = new JFrame("HabitsGame");
        this.activityModel = new DefaultComboBoxModel<>();
        this.customActivityModel = new DefaultListModel<>();
        this.activityModel.addElement("Pití vody");
        setupUi();
    }

    private void setupUi() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 6, 10));
        topPanel.setPreferredSize(new Dimension(600, 70));
        JLabel rankLabel = new JLabel("Rank: " + player.getRank(), SwingConstants.LEFT);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel levelLabel = new JLabel("Level: " + player.getLevel(), SwingConstants.CENTER);
        JLabel xpLabel = new JLabel("XP: " + player.getXp(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        xpLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        rankLabel.setToolTipText("Rank: " + player.getRank());
        rankLabel.setVerticalAlignment(SwingConstants.CENTER);
        levelLabel.setVerticalAlignment(SwingConstants.CENTER);
        xpLabel.setVerticalAlignment(SwingConstants.CENTER);
        topPanel.add(rankLabel);
        topPanel.add(levelLabel);
        topPanel.add(xpLabel);

        JPanel middlePanel = new JPanel(new BorderLayout(10, 10));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel questLabel = new JLabel("Aktuální aktivita: Pití vody");
        JLabel chooseQuestLabel = new JLabel("Vyber aktivitu k plnění:");
        JList<String> questList = new JList<>(activityModel);
        questList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questList.setVisibleRowCount(8);
        questList.setSelectedIndex(0);
        questList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            String selectedQuest = questList.getSelectedValue();
            if (selectedQuest != null) {
                questLabel.setText("Aktuální aktivita: " + selectedQuest);
            }
        });
        JScrollPane questListScrollPane = new JScrollPane(questList);

        JButton manageActivitiesButton = new JButton("Správa aktivit");
        manageActivitiesButton.addActionListener(e -> {
            ActivitySelectionWindow activitySelectionWindow = new ActivitySelectionWindow(activityModel, customActivityModel);
            activitySelectionWindow.showWindow();
        });

        //dodelat omezeni jednou za 24 hodin
        JButton addXpButton = new JButton("Úkol splněn + 30xp");
        addXpButton.addActionListener((ActionEvent e) -> {
            player.addXp(30);
            String selectedQuest = questList.getSelectedValue();
            if (selectedQuest != null) {
                questLabel.setText("Aktuální aktivita: " + selectedQuest);
            }
            rankLabel.setText("Rank: " + player.getRank());
            rankLabel.setToolTipText("Rank: " + player.getRank());
            levelLabel.setText("Level: " + player.getLevel());
            xpLabel.setText("XP: " + player.getXp());
        });

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.add(chooseQuestLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.add(questLabel);
        actionPanel.add(addXpButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(manageActivitiesButton, BorderLayout.EAST);

        middlePanel.add(headerPanel, BorderLayout.NORTH);
        middlePanel.add(questListScrollPane, BorderLayout.CENTER);
        middlePanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}
