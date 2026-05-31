import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

// Okno pro výběr aktivit a přidání vlastních
public class ActivitySelectionWindow {
    private final JFrame frame;
    private final DefaultComboBoxModel<String> activityModel;
    private final DefaultListModel<String> customActivityModel;
    private final Map<String, ActivitySettings> activitySettingsMap;

    // Vytvoří okno správy aktivit
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

    // Vlastnosti okna
    private void setupUi() {
        frame.setSize(540, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        UiTheme.applyToFrame(frame);

        JPanel contentPanel = UiTheme.createCardPanel(null);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = UiTheme.createTitleLabel("Správa aktivit");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        JLabel predefinedLabel = UiTheme.createSectionLabel("Předdefinované aktivity:");
        predefinedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] predefinedActivities = {
                "Pití vody", "Čtení", "Cvičení", "Běhání",
                "Meditace", "Procházka", "Studium", "Úklid", "Spánek"
        };
        JList<String> predefinedList = new JList<>(predefinedActivities);
        predefinedList.setVisibleRowCount(9);
        predefinedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiTheme.styleList(predefinedList);
        predefinedList.setCellRenderer(createActivityCellRenderer());
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
        JScrollPane predefinedScrollPane = UiTheme.wrapList(predefinedList);
        predefinedScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        predefinedScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel customLabel = UiTheme.createSectionLabel("Vlastní aktivita:");

        customLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JList<String> customList = new JList<>(customActivityModel);
        customList.setVisibleRowCount(6);
        customList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiTheme.styleList(customList);
        customList.setCellRenderer(createActivityCellRenderer());
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
        JScrollPane customScrollPane = UiTheme.wrapList(customList);
        customScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        customScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JTextField customActivityField = new JTextField();
        customActivityField.setToolTipText("Název vlastní aktivity");
        JTextField customUnitField = new JTextField();
        customUnitField.setToolTipText("Např. 1 stránka, 15 minut, 1 lekce");
        JTextField customDailyTargetField = new JTextField();
        customDailyTargetField.setToolTipText("Kolikrát denně chceš aktivitu splnit (1-30)");
        JButton addCustomButton = new JButton("Přidat vlastní");
        UiTheme.stylePrimaryButton(addCustomButton);
        addCustomButton.setAlignmentX(Component.LEFT_ALIGNMENT);
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

                        "Tato aktivita už existuje",
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
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(predefinedScrollPane);
        contentPanel.add(Box.createVerticalStrut(14));
        contentPanel.add(customLabel);
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(customScrollPane);
        contentPanel.add(Box.createVerticalStrut(12));
        JPanel customFormPanel = new JPanel(new GridLayout(3, 2, 8, 6));
        customFormPanel.setOpaque(false);
        customFormPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        customFormPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        addFormLabel(customFormPanel, "Název vlastní aktivity:");
        customFormPanel.add(customActivityField);
        addFormLabel(customFormPanel, "Jednotka splnění:");
        customFormPanel.add(customUnitField);
        addFormLabel(customFormPanel, "Kolikrát denně (max 30):");
        customFormPanel.add(customDailyTargetField);
        contentPanel.add(customFormPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(addCustomButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        wrapper.add(contentPanel, BorderLayout.CENTER);
        frame.add(wrapper, BorderLayout.CENTER);
    }

    /** Přidá popisek do formuláře vlastní aktivity. */
    private void addFormLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UiTheme.TEXT_MUTED);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label);
    }

    /** Vrátí vzhled řádku v seznamu (✓ u aktivních aktivit). */
    private ListCellRenderer<String> createActivityCellRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            boolean activeInGame = isActivityAlreadySelected(value);
            String prefix = activeInGame ? UiTheme.CHECK_MARK : "  ";
            JLabel label = new JLabel(prefix + formatActivityDisplay(value));
            label.setFont(UiTheme.emojiCapableFont(Font.PLAIN, 13));
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            UiTheme.applyListCellStyle(label, isSelected, activeInGame);
            return label;
        };
    }

    /** Přidá aktivitu do hlavního seznamu, pokud tam ještě není. */
    private void addActivityIfMissing(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return;
            }
        }
        activityModel.addElement(activity);
    }

    /** True, pokud je aktivita už v hlavním seznamu hry. */
    private boolean isActivityAlreadySelected(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
    }

    /** True, pokud vlastní aktivita se stejným názvem už existuje. */
    private boolean containsInCustomList(String activity) {
        for (int i = 0; i < customActivityModel.size(); i++) {
            if (customActivityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
    }

    /** Kliknutím přidá nebo odebere aktivitu z hlavního seznamu. */
    private void toggleActivitySelection(String activity) {
        if (isActivityAlreadySelected(activity)) {
            removeActivityFromSelection(activity);
        } else {
            addActivityIfMissing(activity);
        }
    }

    /** Odebere aktivitu z hlavního seznamu. */
    private void removeActivityFromSelection(String activity) {
        for (int i = 0; i < activityModel.getSize(); i++) {
            if (activityModel.getElementAt(i).equalsIgnoreCase(activity)) {
                activityModel.removeElementAt(i);
                return;
            }
        }
    }

    /** Zobrazí okno správy aktivit. */
    public void showWindow() {
        frame.setVisible(true);
    }

    /** Vrátí text aktivity včetně jednotky, např. „Pití vody - 1 litr“. */
    private String formatActivityDisplay(String activityName) {
        ActivitySettings settings = activitySettingsMap.get(activityName);
        if (settings == null) {
            return activityName;
        }
        return activityName + " - " + settings.getUnitLabel();
    }
}
