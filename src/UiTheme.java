import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.Enumeration;

// Společný vzhled aplikace – barvy, tlačítka, seznamy.
public class UiTheme {
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color CARD = Color.WHITE;
    public static final Color ACCENT = new Color(76, 110, 245);
    public static final Color ACCENT_HOVER = new Color(58, 90, 210);
    public static final Color TEXT = new Color(35, 42, 52);
    public static final Color TEXT_MUTED = new Color(100, 110, 122);
    public static final Color BORDER = new Color(218, 224, 232);
    public static final Color LIST_SELECTED = new Color(76, 110, 245);
    public static final Color LIST_SELECTED_TEXT = Color.WHITE;
    public static final Color LIST_ACTIVE = new Color(214, 232, 255);
    public static final Color LOG_BACKGROUND = new Color(248, 250, 252);
    public static final String CHECK_MARK = "\u2714 ";
    public static final String GOLD_EMOJI = "\uD83D\uDCB0";
    private static final String FONT_STACK = "Segoe UI Emoji, Segoe UI Symbol, Segoe UI, sans-serif";

    private UiTheme() {
    }

    // Text s podporou emoji a fajfek (pro JLabel).
    public static String htmlText(String text) {
        return "<html><span style='font-family:" + FONT_STACK + "'>" + escapeHtml(text) + "</span></html>";
    }

    // Rank na jednom řádku
    public static String htmlRankLabel(String rank) {
        return "<html><nobr><span style='font-family:" + FONT_STACK + "; font-size:12px'>Rank: <b>"
                + escapeHtml(rank) + "</b></span></nobr></html>";
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    //Nastaví zhled při startu aplikace.
    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Font baseFont = new Font("Segoe UI", Font.PLAIN, 13);
        setGlobalFont(baseFont);

        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        
        UIManager.put("List.background", CARD);
        UIManager.put("List.selectionBackground", LIST_SELECTED);
        UIManager.put("List.selectionForeground", LIST_SELECTED_TEXT);
        UIManager.put("ScrollPane.background", CARD);
    }

    // Nastaví pozadí okna
    public static void applyToFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND);
    }

    public static JPanel createCardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(CARD);
        panel.setBorder(cardBorder());
        return panel;
    }


    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
    }

    // Velký nadpis okna
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(TEXT);
        return label;
    }

    //Vytvori menší popisek
    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    // statistiky nahoře v hlavním okně
    public static JLabel createStatLabel(String text, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(emojiCapableFont(Font.BOLD, 17));
        label.setForeground(TEXT);
        label.setOpaque(false);
        return label;
    }

    // Font s fallbackem pro emoji a fajfky (logický font, ne fyzický Segoe UI).
    public static Font emojiCapableFont(int style, int size) {
        return new Font(Font.DIALOG, style, size);
    }

    // Text goldu s emoji (💰 má lepší podporu než 🪙 ve Windows fontech).
    public static String formatGoldLabel(int gold) {
        return "Gold " + GOLD_EMOJI + ":\u00A0" + gold;
    }

    // stejny gold text ale pro obchod
    public static String formatOwnedGoldLabel(int gold) {
        return "Tvoje goldy " + GOLD_EMOJI + ":\u00A0" + gold;
    }

    // Nastyguje modré hlavní tlačítko.
    public static void stylePrimaryButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBackground(ACCENT);
        button.setForeground(LIST_SELECTED_TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // tlacitka
    public static void styleSecondaryButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBackground(CARD);
        button.setForeground(TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // seznam aktivit nebo predmetu
    public static void styleList(JList<?> list) {
        list.setBackground(CARD);
        list.setForeground(TEXT);
        list.setSelectionBackground(LIST_SELECTED);
        list.setSelectionForeground(LIST_SELECTED_TEXT);
        list.setFixedCellHeight(28);
        list.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    // Zmena barvy vybranych radku
    public static void applyListCellStyle(JLabel label, boolean selected) {
        label.setOpaque(true);
        if (selected) {
            label.setBackground(LIST_SELECTED);
            label.setForeground(LIST_SELECTED_TEXT);
        } else {
            label.setBackground(CARD);
            label.setForeground(TEXT);
        }
    }

    public static void applyListCellStyle(JLabel label, boolean selected, boolean activeInGame) {
        label.setOpaque(true);
        if (selected) {
            label.setBackground(LIST_SELECTED);
            label.setForeground(LIST_SELECTED_TEXT);
        } else if (activeInGame) {
            label.setBackground(LIST_ACTIVE);
            label.setForeground(TEXT);
        } else {
            label.setBackground(CARD);
            label.setForeground(TEXT);
        }
    }

    // scroll pane.
    public static JScrollPane wrapList(JList<?> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(CARD);
        return scrollPane;
    }

    // panel pro souboj
    public static JPanel createLogPanel(JLabel logLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LOG_BACKGROUND);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        logLabel.setForeground(TEXT);
        logLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(logLabel, BorderLayout.CENTER);
        return panel;
    }

    //Nastaví stejný font (kromě Label/List – tam necháme fallback pro emoji).
    private static void setGlobalFont(Font font) {
        FontUIResource fontResource = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key instanceof String keyName
                    && (keyName.endsWith("Label.font") || keyName.endsWith("List.font") || keyName.endsWith("ComboBox.font"))) {
                continue;
            }
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontResource);
            }
        }
    }
}
