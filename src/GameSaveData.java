//ukládání
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Všechna data hry pro uložení do souboru.
public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int level;
    private int xp;
    private int gold;
    private Map<String, Integer> inventory;
    private List<String> activeActivities;
    private List<CustomActivityData> customActivities;
    private Map<String, Integer> dailyProgress;
    private String progressDate;
    private int arenaGamesToday;
    private String arenaGamesDate;

    // Uloží kompletní stav hry.
    public GameSaveData(
            int level,
            int xp,
            int gold,
            Map<String, Integer> inventory,
            List<String> activeActivities,
            List<CustomActivityData> customActivities,
            Map<String, Integer> dailyProgress,
            String progressDate,
            int arenaGamesToday,
            String arenaGamesDate
    ) {
        this.level = level;
        this.xp = xp;
        this.gold = gold;
        this.inventory = new HashMap<>(inventory);
        this.activeActivities = new ArrayList<>(activeActivities);
        this.customActivities = new ArrayList<>(customActivities);
        this.dailyProgress = new HashMap<>(dailyProgress);
        this.progressDate = progressDate;
        this.arenaGamesToday = arenaGamesToday;
        this.arenaGamesDate = arenaGamesDate;
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

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    // Vrátí seznam aktivit v hlavním okně.
    public List<String> getActiveActivities() {
        return activeActivities;
    }

    // Vrátí vlastní aktivity hráče.
    public List<CustomActivityData> getCustomActivities() {
        return customActivities;
    }

    // Vrátí kolikrát dnes byly aktivity splněny.
    public Map<String, Integer> getDailyProgress() {
        return dailyProgress;
    }

    // Vrátí datum posledního denního progressu.
    public String getProgressDate() {
        return progressDate;
    }

    // Vrátí kolik her v aréně hráč dnes odehrál.
    public int getArenaGamesToday() {
        return arenaGamesToday;
    }

    // Vrátí datum poslední hry v aréně.
    public String getArenaGamesDate() {
        return arenaGamesDate;
    }
}
//konec ukládání
