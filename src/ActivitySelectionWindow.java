import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class ActivitySelectionWindow {
    private final JFrame frame;
    private final DefaultComboBoxModel<String> activityModel;
    private final DefaultListModel<String> customActivityModel;
    private final Map<String, ActivitySettings> activitySettingsMap;

    public ActivitySelectionWindow(
            DefaultComboBoxModel<String> activityModel,
            DefaultListModel<String> customActivityModel,
            Map<String, ActivitySettings> activitySettingsMap
    ) {
        this.activityModel = activityModel;
        this.customActivityModel = customActivityModel;
        this.activitySettingsMap = activitySettingsMap;
        this.frame = new JFrame("Správa aktivit");
        setupUi();
    }

    private void setupUi() {
        frame.setSize(520, 460);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel predefinedLabel = new JLabel("Předdefinované aktivity:");
        String[] predefinedActivities = {"Pití vody", "Čtení", "Cvičení", "Běhání"};
        JList<String> predefinedList = new JList<>(predefinedActivities);
        predefinedList.setVisibleRowCount(5);
        predefinedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        predefinedList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setFont(list.getFont());
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            if (isActivityAlreadySelected(value)) {
                label.setText("✓ " + formatActivityDisplay(value));
            } else {
                label.setText("  " + formatActivityDisplay(value));
            }
            return label;
        });
        predefinedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedIndex = predefinedList.locationToIndex(e.getPoint());
                if (clickedIndex < 0) {
                    return;
                }
                String activity = predefinedList.getModel().getElementAt(clickedIndex);
                toggleActivitySelection(activity);
                predefinedList.repaint();
            }
        });
        JScrollPane predefinedScrollPane = new JScrollPane(predefinedList);

        JLabel customLabel = new JLabel("Vlastní aktivita:");
        JList<String> customList = new JList<>(customActivityModel);
        customList.setVisibleRowCount(6);
        customList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setFont(list.getFont());
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            if (isActivityAlreadySelected(value)) {
                label.setText("✓ " + formatActivityDisplay(value));
            } else {
                label.setText("  " + formatActivityDisplay(value));
            }
            return label;
        });
        customList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedIndex = customList.locationToIndex(e.getPoint());
                if (clickedIndex < 0) {
                    return;
                }
                String activity = customList.getModel().getElementAt(clickedIndex);
                toggleActivitySelection(activity);
                customList.repaint();
            }
        });
        JScrollPane customScrollPane = new JScrollPane(customList);

        JTextField customActivityField = new JTextField();
        customActivityField.setToolTipText("Název vlastní aktivity");
        JTextField customUnitField = new JTextField();
        customUnitField.setToolTipText("Např. 1 stránka, 15 minut, 1 lekce");
        JTextField customDailyTargetField = new JTextField();
        customDailyTargetField.setToolTipText("Kolikrát denně chceš aktivitu splnit (1-30)");
        JButton addCustomButton = new JButton("Přidat vlastní");
        addCustomButton.addActionListener(e -> {
            String customActivity = customActivityField.getText().trim();
            if (customActivity.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Název aktivity nesmí být prázdný.",
                        "Neplatná hodnota",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (containsInCustomList(customActivity)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Tato vlastní aktivita už existuje.",
                        "Duplicitní aktivita",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String customUnit = customUnitField.getText().trim();
            if (customUnit.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Jednotka splnění nesmí být prázdná.",
                        "Neplatná hodnota",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String dailyTargetText = customDailyTargetField.getText().trim();
            int dailyTarget;
            try {
                dailyTarget = Integer.parseInt(dailyTargetText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Počet splnění denně musí být celé číslo.",
                        "Neplatná hodnota",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (dailyTarget <= 0) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Počet splnění denně musí být větší než 0.",
                        "Neplatná hodnota",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (dailyTarget > 30) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Počet splnění denně může být maximálně 30.",
                        "Překročen limit",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            customActivityModel.addElement(customActivity);
            activitySettingsMap.put(customActivity, new ActivitySettings(customUnit, dailyTarget));
            addActivityIfMissing(customActivity);
            customActivityField.setText("");
            customUnitField.setText("");
            customDailyTargetField.setText("");
            customList.repaint();
        });

        contentPanel.add(predefinedLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(predefinedScrollPane);
        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(customLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(customScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        JPanel customFormPanel = new JPanel(new GridLayout(3, 2, 8, 6));
        customFormPanel.add(new JLabel("Název vlastní aktivity:"));
        customFormPanel.add(customActivityField);
        customFormPanel.add(new JLabel("Jednotka splnění:"));
        customFormPanel.add(customUnitField);
        customFormPanel.add(new JLabel("Kolikrát denně (max 30):"));
        customFormPanel.add(customDailyTargetField);
        contentPanel.add(customFormPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(addCustomButton);

        frame.add(contentPanel, BorderLayout.CENTER);
    }

    private void addActivityIfMissing(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return;
            }
        }
        activityModel.addElement(activity);
    }

    private boolean isActivityAlreadySelected(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsInCustomList(String activity) {
        for (int i = 0; i < customActivityModel.size(); i++) {
            if (customActivityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
    }

    private void toggleActivitySelection(String activity) {
        if (isActivityAlreadySelected(activity)) {
            removeActivityFromSelection(activity);
        } else {
            addActivityIfMissing(activity);
        }
    }

    private void removeActivityFromSelection(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                activityModel.removeElementAt(i);
                return;
            }
        }
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private String formatActivityDisplay(String activityName) {
        ActivitySettings settings = activitySettingsMap.get(activityName);
        if (settings == null) {
            return activityName;
        }
        return activityName + " - " + settings.getUnitLabel();
    }
}
