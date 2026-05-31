import javax.swing.*;
import java.awt.*;

// Okno obchodu
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

    // Okno obchodu
    private void setupUi() {
        frame.setSize(540, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        UiTheme.applyToFrame(frame);

        JPanel contentPanel = UiTheme.createCardPanel(null);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = UiTheme.createTitleLabel("Předměty pro souboje");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        refreshGoldLabel();
        goldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        goldLabel.setFont(UiTheme.emojiCapableFont(Font.BOLD, 14));
        goldLabel.setForeground(UiTheme.TEXT);
        contentPanel.add(goldLabel);
        contentPanel.add(Box.createVerticalStrut(6));

        refreshTotalStatsLabel();
        totalStatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalStatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        totalStatsLabel.setForeground(UiTheme.TEXT_MUTED);
        contentPanel.add(totalStatsLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        JList<ShopItem> itemList = new JList<>(shopItemModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setVisibleRowCount(8);
        UiTheme.styleList(itemList);
        itemList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(UiTheme.htmlText("  " + value.getName() + " (" + value.getPrice() + " goldů)"));
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            UiTheme.applyListCellStyle(label, isSelected);
            return label;
        });
        JLabel statsLabel = new JLabel(" ");
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statsLabel.setForeground(UiTheme.TEXT_MUTED);
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

        JScrollPane itemScrollPane = UiTheme.wrapList(itemList);
        itemScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        contentPanel.add(itemScrollPane);
        contentPanel.add(statsLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        JButton buyButton = new JButton("Koupit");
        UiTheme.stylePrimaryButton(buyButton);
        buyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        buyButton.addActionListener(e -> buySelectedItem(itemList));
        contentPanel.add(buyButton);
        contentPanel.add(Box.createVerticalStrut(14));

        inventoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshInventoryLabel();
        inventoryLabel.setForeground(UiTheme.TEXT);
        inventoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(UiTheme.createSectionLabel("Tvůj inventář:"));
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(inventoryLabel);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        wrapper.add(contentPanel, BorderLayout.CENTER);
        frame.add(wrapper, BorderLayout.CENTER);
    }

    // Koupi vybraný předmět
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

    // Aktualizuje pocet goldu
    private void refreshGoldLabel() {
        goldLabel.setText(UiTheme.formatOwnedGoldLabel(player.getGold()));
    }

    //Aktualizuje text se staty.
    private void refreshTotalStatsLabel() {
        totalStatsLabel.setText(player.getTotalStatsText());
    }

    // Aktualizuje inventar 
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
