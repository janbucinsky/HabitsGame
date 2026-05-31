import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class Player {
    private static final int GOLD_PER_LEVEL_UP = 50;
    private static final int BASE_ATTACK = 10;
    private static final int BASE_DEFENSE = 0;
    private static final int BASE_HP = 200;

    private int level = 1;
    private int xp = 0;
    private int gold = 0;
    private final Map<String, Integer> inventory = new HashMap<>();

    // Přidá XP a při dosažení novyho levelu zvýší level a přidá goldy
    public void addXp(int amount) {
        if (amount < 0) return;
        xp += amount;

        while (level < RankManager.MAX_LEVEL && xp >= level * 100) {
            xp -= level * 100;
            level++;
            gold += GOLD_PER_LEVEL_UP;
        }
    }

    //Odebere XP (minimum je 0)
    public void removeXp(int amount) {
        if (amount <= 0) {
            return;
        }
        xp = Math.max(0, xp - amount);
    }

    //prida goldy
    public void addGold(int amount) {
        if (amount > 0) {
            gold += amount;
        }
    }

    // Odebere goldy
    public void removeGold(int amount) {
        if (amount <= 0) {
            return;
        }
        gold = Math.max(0, gold - amount);
    }
    
    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getGold() {
        return gold;
    }

    public String getRank() {
        return RankManager.getRank(level);
    }

    // Odecte goldy za nakup
    public boolean spendGold(int amount) {
        if (amount < 0 || gold < amount) {
            return false;
        }
        gold -= amount;
        return true;
    }

    // Přidá predmet do inventáře
    public void addItem(String itemName) {
        inventory.merge(itemName, 1, Integer::sum);
    }

    //ukládání
    // Vrátí kopii inventáře pro uložení.
    public Map<String, Integer> getInventorySnapshot() {
        return Collections.unmodifiableMap(new HashMap<>(inventory));
    }

    // Načte stav hráče z uložené hry.
    public void loadFromSave(int level, int xp, int gold, Map<String, Integer> savedInventory) {
        this.level = Math.min(Math.max(1, level), RankManager.MAX_LEVEL);
        this.xp = xp;
        this.gold = gold;
        inventory.clear();
        if (savedInventory != null) {
            inventory.putAll(savedInventory);
        }
    }
    //konec ukládání

    //Vrati celkove attack staty (základ + bonusy z předmětů).
    public int getTotalAttack() {
        int total = BASE_ATTACK;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            ShopItem item = ShopItem.findByName(entry.getKey());
            if (item != null) {
                total += item.getDamageBonus() * entry.getValue();
            }
        }
        return total;
    }

    // Vrati celkove deffence staty (základ + bonusy z předmětů).
    public int getTotalDefense() {
        int total = BASE_DEFENSE;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            ShopItem item = ShopItem.findByName(entry.getKey());
            if (item != null) {
                total += item.getDefenseBonus() * entry.getValue();
            }
        }
        return total;
    }

    // Vrati celkove HP (základ + bonusy z předmětů).
    public int getTotalHp() {
        int total = BASE_HP;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            ShopItem item = ShopItem.findByName(entry.getKey());
            if (item != null) {
                total += item.getHpBonus() * entry.getValue();
            }
        }
        return total;
    }

    // Vrati text se všemi staty hráče.
    public String getTotalStatsText() {
        return "Celkový útok: " + getTotalAttack()
                + "  |  Celková obrana: " + getTotalDefense()
                + "  |  Celkové HP: " + getTotalHp();
    }

    // Vrati textový přehled inventáře pro zobrazení v UI.
    public String getInventoryDisplay() {
        if (inventory.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            ShopItem item = ShopItem.findByName(entry.getKey());
            builder.append(entry.getKey()).append(" x").append(entry.getValue());
            if (item != null) {
                builder.append(" (").append(item.getStatsText()).append(")");
            }
            builder.append("\n");
        }
        return builder.toString().trim();
    }
}
