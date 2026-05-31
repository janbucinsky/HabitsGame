public class ShopItem {
    private final String name;
    private final int price;
    private final int damageBonus;
    private final int defenseBonus;
    private final int hpBonus;

    private static final ShopItem[] CATALOG = {
            new ShopItem("Dýka", 45, 6, 0, 0),
            new ShopItem("Meč", 100, 12, 0, 0),
            new ShopItem("Těžká sekera", 150, 18, 0, 0),
            new ShopItem("Štít", 80, 0, 10, 0),
            new ShopItem("Ochranný amulet", 120, 0, 8, 0),
            new ShopItem("Bojové boty", 90, 3, 5, 0),
            new ShopItem("Lektvar síly", 50, 8, 0, 0),
            new ShopItem("Lektvar života", 60, 0, 0, 20),
            new ShopItem("Prsten vitality", 70, 0, 0, 10),
            new ShopItem("Kožená zbroj", 110, 0, 3, 15),
            new ShopItem("Bojová přilba", 95, 0, 2, 12)
    };

    public ShopItem(String name, int price, int damageBonus, int defenseBonus, int hpBonus) {
        this.name = name;
        this.price = price;
        this.damageBonus = damageBonus;
        this.defenseBonus = defenseBonus;
        this.hpBonus = hpBonus;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public String getStatsText() {
        StringBuilder stats = new StringBuilder();
        if (damageBonus > 0) {
            stats.append("Útok: +").append(damageBonus);
        }
        if (defenseBonus > 0) {
            if (stats.length() > 0) {
                stats.append("  |  ");
            }
            stats.append("Obrana: +").append(defenseBonus);
        }
        if (hpBonus > 0) {
            if (stats.length() > 0) {
                stats.append("  |  ");
            }
            stats.append("HP: +").append(hpBonus);
        }
        if (stats.length() == 0) {
            return "Bez bonusů";
        }
        return stats.toString();
    }

    public static ShopItem[] getCatalog() {
        return CATALOG;
    }

    public static ShopItem findByName(String name) {
        for (ShopItem item : CATALOG) {
            if (item.name.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + " - " + price + " gold";
    }
}
