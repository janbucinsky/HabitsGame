import javax.swing.*;
import java.awt.*;

public class ShopWindow {
    private final Player player;
    private final Runnable onGoldChanged;
    private final JFrame frame;
    private final JLabel goldLabel;
    private final JLabel totalStatsLabel;
    private final JLabel inventoryLabel;
    private final DefaultListModel<ShopItem> shopItemModel;
    private final ShopItem[] shopItems = ShopItem.getCatalog();

    public ShopWindow(Player player, Runnable onGoldChanged) {
        this.player = player;
        this.onGoldChanged = onGoldChanged;
        this.frame = new JFrame("Obchod");
        this.goldLabel = new JLabel();
        this.totalStatsLabel = new JLabel();
        this.inventoryLabel = new JLabel();
        this.shopItemModel = new DefaultListModel<>();
        for (ShopItem item : shopItems) {
            shopItemModel.addElement(item);
        }
        setupUi();
    }

    private void setupUi() {
        frame.setSize(520, 480);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Předměty pro souboje");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        refreshGoldLabel();
        goldLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(goldLabel);
        contentPanel.add(Box.createVerticalStrut(6));

        refreshTotalStatsLabel();
        totalStatsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(totalStatsLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        JList<ShopItem> itemList = new JList<>(shopItemModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setVisibleRowCount(8);
        itemList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setFont(list.getFont());
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            label.setText(value.getName() + " (" + value.getPrice() + " goldů)");
            return label;
        });
        JLabel statsLabel = new JLabel(" ");
        statsLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            ShopItem selected = itemList.getSelectedValue();
            if (selected != null) {
                statsLabel.setText(selected.getStatsText());
            }
        });
        if (shopItemModel.getSize() > 0) {
            itemList.setSelectedIndex(0);
        }

        contentPanel.add(new JScrollPane(itemList));
        contentPanel.add(statsLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        JButton buyButton = new JButton("Koupit");
        buyButton.addActionListener(e -> buySelectedItem(itemList));
        contentPanel.add(buyButton);
        contentPanel.add(Box.createVerticalStrut(12));

        inventoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshInventoryLabel();
        contentPanel.add(new JLabel("Tvůj inventář:"));
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(inventoryLabel);

        frame.add(contentPanel, BorderLayout.CENTER);
    }

    private void buySelectedItem(JList<ShopItem> itemList) {
        ShopItem selected = itemList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Nejdřív vyber předmět, který chceš koupit.",
                    "Nic není vybráno",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!player.spendGold(selected.getPrice())) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Nemáš dostatek goldů. Potřebuješ " + selected.getPrice() + " goldů.",
                    "Nedostatek goldů",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        player.addItem(selected.getName());
        refreshGoldLabel();
        refreshTotalStatsLabel();
        refreshInventoryLabel();
        if (onGoldChanged != null) {
            onGoldChanged.run();
        }

        JOptionPane.showMessageDialog(
                frame,
                "Zakoupil jsi: " + selected.getName(),
                "Nákup úspěšný",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void refreshGoldLabel() {
        goldLabel.setText("Tvoje goldy: " + player.getGold());
    }

    private void refreshTotalStatsLabel() {
        totalStatsLabel.setText(player.getTotalStatsText());
    }

    private void refreshInventoryLabel() {
        String inventoryText = player.getInventoryDisplay();
        if (inventoryText.isEmpty()) {
            inventoryLabel.setText("(zatím prázdný)");
        } else {
            inventoryLabel.setText("<html>" + inventoryText.replace("\n", "<br>") + "</html>");
        }
    }

    public void showWindow() {
        refreshGoldLabel();
        refreshTotalStatsLabel();
        refreshInventoryLabel();
        frame.setVisible(true);
    }
}
