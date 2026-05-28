import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActivitySelectionWindow {
    private final JFrame frame;
    private final DefaultComboBoxModel<String> activityModel;
    private final DefaultListModel<String> customActivityModel;

    public ActivitySelectionWindow(DefaultComboBoxModel<String> activityModel, DefaultListModel<String> customActivityModel) {
        this.activityModel = activityModel;
        this.customActivityModel = customActivityModel;
        this.frame = new JFrame("Správa aktivit");
        setupUi();
    }

    private void setupUi() {
        frame.setSize(420, 360);
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
                label.setText("✓ " + value);
            } else {
                label.setText("  " + value);
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
                label.setText("✓ " + value);
            } else {
                label.setText("  " + value);
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


            customActivityModel.addElement(customActivity);
            addActivityIfMissing(customActivity);
            customActivityField.setText("");
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
        contentPanel.add(customActivityField);
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
}
