import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
//ukládání
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//konec ukládání
import java.util.Map;

public class MainWindow {
    private final Player player;
    private final JFrame frame;
    private final DefaultComboBoxModel<String> activityModel;
    private final DefaultListModel<String> customActivityModel;
    private final Map<String, ActivitySettings> activitySettingsMap;
    private final Map<String, Integer> dailyProgressMap;
    private LocalDate progressDate;

    private JLabel rankLabel;
    private JLabel levelLabel;
    private JLabel xpLabel;
    private JLabel goldLabel;
    private JLabel questLabel;
    private JLabel activityRuleLabel;
    private JLabel dailyProgressLabel;
    private JList<String> questList;

    public MainWindow(Player player) {
        this.player = player;
        this.frame = new JFrame("HabitsGame");
        this.activityModel = new DefaultComboBoxModel<>();
        this.customActivityModel = new DefaultListModel<>();
        this.activitySettingsMap = new HashMap<>();
        this.dailyProgressMap = new HashMap<>();
        this.progressDate = LocalDate.now();
        initPredefinedActivitySettings();
        activityModel.addElement("Pití vody");
        setupUi();
    }

    private void initPredefinedActivitySettings() {
        activitySettingsMap.put("Pití vody", new ActivitySettings("1 litr", 5));
        activitySettingsMap.put("Čtení", new ActivitySettings("1 kapitola", 30));
        activitySettingsMap.put("Běhání", new ActivitySettings("1 km", 42));
        activitySettingsMap.put("Cvičení", new ActivitySettings("10 minut", 30));
    }

    private void setupUi() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(760, 580);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 4, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 6, 10));
        topPanel.setPreferredSize(new Dimension(760, 70));
        rankLabel = new JLabel("Rank: " + player.getRank(), SwingConstants.LEFT);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 17));
        levelLabel = new JLabel("Level: " + player.getLevel(), SwingConstants.CENTER);
        xpLabel = new JLabel("XP: " + player.getXp(), SwingConstants.CENTER);
        goldLabel = new JLabel("Gold: " + player.getGold(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        xpLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        goldLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        rankLabel.setToolTipText("Rank: " + player.getRank());
        rankLabel.setVerticalAlignment(SwingConstants.CENTER);
        levelLabel.setVerticalAlignment(SwingConstants.CENTER);
        xpLabel.setVerticalAlignment(SwingConstants.CENTER);
        goldLabel.setVerticalAlignment(SwingConstants.CENTER);
        topPanel.add(rankLabel);
        topPanel.add(levelLabel);
        topPanel.add(xpLabel);
        topPanel.add(goldLabel);

        JPanel middlePanel = new JPanel(new BorderLayout(10, 10));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questLabel = new JLabel("Aktuální aktivita: Pití vody");
        activityRuleLabel = new JLabel();
        dailyProgressLabel = new JLabel();
        JLabel chooseQuestLabel = new JLabel("Vyber aktivitu k plnění:");
        questList = new JList<>(activityModel);
        questList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questList.setVisibleRowCount(8);
        questList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setFont(list.getFont());
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            label.setText(formatActivityDisplay(value));
            return label;
        });
        questList.setSelectedIndex(0);
        questList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            String selectedQuest = questList.getSelectedValue();
            if (selectedQuest != null) {
                questLabel.setText("Aktuální aktivita: " + formatActivityDisplay(selectedQuest));
                updateActivityInfo(selectedQuest, activityRuleLabel, dailyProgressLabel);
            }
        });
        JScrollPane questListScrollPane = new JScrollPane(questList);
        updateActivityInfo("Pití vody", activityRuleLabel, dailyProgressLabel);

        JButton manageActivitiesButton = new JButton("Správa aktivit");
        manageActivitiesButton.addActionListener(e -> {
            ActivitySelectionWindow activitySelectionWindow = new ActivitySelectionWindow(
                    activityModel,
                    customActivityModel,
                    activitySettingsMap
            );
            activitySelectionWindow.showWindow();
        });

        //dodelat omezeni jednou za 24 hodin
        JButton addXpButton = new JButton("Úkol splněn + 30xp");
        addXpButton.addActionListener((ActionEvent e) -> {
            String selectedQuest = questList.getSelectedValue();
            if (selectedQuest != null) {
                resetDailyProgressIfNeeded();
                ActivitySettings settings = activitySettingsMap.get(selectedQuest);
                if (settings == null) {
                    settings = new ActivitySettings("1 splnění", 1);
                    activitySettingsMap.put(selectedQuest, settings);
                }

                int currentCount = dailyProgressMap.getOrDefault(selectedQuest, 0);
                if (!settings.isUnlimited() && currentCount >= settings.getDailyTarget()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Denní limit pro aktivitu \"" + selectedQuest + "\" je už splněný.",
                            "Denní limit dosažen",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    updateActivityInfo(selectedQuest, activityRuleLabel, dailyProgressLabel);
                    return;
                }

                player.addXp(getXpRewardForActivity(selectedQuest));
                currentCount++;
                dailyProgressMap.put(selectedQuest, currentCount);
                questLabel.setText("Aktuální aktivita: " + formatActivityDisplay(selectedQuest));
                updateActivityInfo(selectedQuest, activityRuleLabel, dailyProgressLabel);
            } else {
                player.addXp(30);
            }
            rankLabel.setText("Rank: " + player.getRank());
            rankLabel.setToolTipText("Rank: " + player.getRank());
            levelLabel.setText("Level: " + player.getLevel());
            xpLabel.setText("XP: " + player.getXp());
            goldLabel.setText("Gold: " + player.getGold());
        });

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.add(chooseQuestLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.add(questLabel);
        actionPanel.add(addXpButton);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(activityRuleLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(dailyProgressLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(manageActivitiesButton, BorderLayout.EAST);

        JButton shopButton = new JButton("Obchod");
        shopButton.addActionListener(e -> {
            ShopWindow shopWindow = new ShopWindow(player, () -> goldLabel.setText("Gold: " + player.getGold()));
            shopWindow.showWindow();
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        footerPanel.add(shopButton);

        middlePanel.add(headerPanel, BorderLayout.NORTH);
        middlePanel.add(questListScrollPane, BorderLayout.CENTER);
        middlePanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        //ukládání
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SaveManager.save(collectSaveData());
            }
        });
        //konec ukládání
    }

    //ukládání
    public GameSaveData collectSaveData() {
        List<String> activeActivities = new ArrayList<>();
        for (int i = 0; i < activityModel.getSize(); i++) {
            activeActivities.add(activityModel.getElementAt(i));
        }

        List<CustomActivityData> customActivities = new ArrayList<>();
        for (int i = 0; i < customActivityModel.size(); i++) {
            String name = customActivityModel.getElementAt(i);
            ActivitySettings settings = activitySettingsMap.get(name);
            if (settings != null) {
                customActivities.add(new CustomActivityData(
                        name,
                        settings.getUnitLabel(),
                        settings.getDailyTarget()
                ));
            }
        }

        return new GameSaveData(
                player.getLevel(),
                player.getXp(),
                player.getGold(),
                new HashMap<>(player.getInventorySnapshot()),
                activeActivities,
                customActivities,
                new HashMap<>(dailyProgressMap),
                progressDate.toString()
        );
    }

    public void applySaveData(GameSaveData save) {
        if (save == null) {
            return;
        }

        player.loadFromSave(
                save.getLevel(),
                save.getXp(),
                save.getGold(),
                save.getInventory()
        );

        activityModel.removeAllElements();
        customActivityModel.clear();
        activitySettingsMap.clear();
        dailyProgressMap.clear();
        initPredefinedActivitySettings();

        for (CustomActivityData custom : save.getCustomActivities()) {
            customActivityModel.addElement(custom.getName());
            activitySettingsMap.put(
                    custom.getName(),
                    new ActivitySettings(custom.getUnitLabel(), custom.getDailyTarget())
            );
        }

        for (String activity : save.getActiveActivities()) {
            if (!isActivityInModel(activity)) {
                activityModel.addElement(activity);
            }
        }

        if (activityModel.getSize() == 0) {
            activityModel.addElement("Pití vody");
        }

        dailyProgressMap.putAll(save.getDailyProgress());
        try {
            progressDate = LocalDate.parse(save.getProgressDate());
        } catch (Exception e) {
            progressDate = LocalDate.now();
        }
        resetDailyProgressIfNeeded();

        refreshPlayerLabels();
        if (questList != null && activityModel.getSize() > 0) {
            questList.setSelectedIndex(0);
            String selected = questList.getSelectedValue();
            if (selected != null) {
                questLabel.setText("Aktuální aktivita: " + formatActivityDisplay(selected));
                updateActivityInfo(selected, activityRuleLabel, dailyProgressLabel);
            }
        }
    }

    private boolean isActivityInModel(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
    }

    private void refreshPlayerLabels() {
        if (rankLabel == null) {
            return;
        }
        rankLabel.setText("Rank: " + player.getRank());
        rankLabel.setToolTipText("Rank: " + player.getRank());
        levelLabel.setText("Level: " + player.getLevel());
        xpLabel.setText("XP: " + player.getXp());
        goldLabel.setText("Gold: " + player.getGold());
    }
    //konec ukládání

    public void showWindow() {
        frame.setVisible(true);
    }

    private void updateActivityInfo(String activityName, JLabel activityRuleLabel, JLabel dailyProgressLabel) {
        resetDailyProgressIfNeeded();
        ActivitySettings settings = activitySettingsMap.get(activityName);
        if (settings == null) {
            settings = new ActivitySettings("1 splnění", 1);
            activitySettingsMap.put(activityName, settings);
        }

        String targetText = settings.isUnlimited() ? "bez limitu" : settings.getDailyTarget() + "x denně";
        activityRuleLabel.setText("Jednotka: " + settings.getUnitLabel() + " | Denní cíl: " + targetText);

        int progress = dailyProgressMap.getOrDefault(activityName, 0);
        if (settings.isUnlimited()) {
            dailyProgressLabel.setText("Dnes splněno: " + progress + "x");
        } else {
            dailyProgressLabel.setText("Dnes splněno: " + progress + "/" + settings.getDailyTarget());
        }
    }

    private void resetDailyProgressIfNeeded() {
        LocalDate today = LocalDate.now();
        if (!today.equals(progressDate)) {
            dailyProgressMap.clear();
            progressDate = today;
        }
    }

    private String formatActivityDisplay(String activityName) {
        ActivitySettings settings = activitySettingsMap.get(activityName);
        if (settings == null) {
            return activityName;
        }
        return activityName + " - " + settings.getUnitLabel();
    }

    private int getXpRewardForActivity(String activityName) {
        if ("Běhání".equalsIgnoreCase(activityName)) {
            return 15;
        }
        return 30;
    }
}
