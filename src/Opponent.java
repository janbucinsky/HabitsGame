import java.util.Random;

public class Opponent {
    private static final int STAT_OFFSET_DOWN = 7;
    private static final int STAT_OFFSET_UP = 5;

    private static final String[] NAMES = {
            "Gladiátor", "Boxer", "Temný poutník", "Šampion",
            "Rytíř", "Bandita", "Lovec", "Zuřivý berserker",
            "Stínový duelant", "Kamenný obr", "Mistr meče", "Strážce arény"
    };

    private final Random random = new Random();
    private String name;
    private final int playerAttackBase;
    private final int playerDefenseBase;
    private int currentAttack;
    private int currentDefense;
    private int currentHp;
    private int maxHp;

    public Opponent(int playerAttack, int playerDefense, int playerHp) {
        this.playerAttackBase = playerAttack;
        this.playerDefenseBase = playerDefense;
        resetOpponent(playerHp);
    }

    public void resetOpponent(int playerHp) {
        this.name = NAMES[random.nextInt(NAMES.length)];
        this.maxHp = Math.max(1, randomStat(playerHp));
        this.currentHp = maxHp;
        rollStats();
    }

    private int randomOffset() {
        return random.nextInt(STAT_OFFSET_DOWN + STAT_OFFSET_UP + 1) - STAT_OFFSET_DOWN;
    }

    private int randomStat(int playerStat) {
        return playerStat + randomOffset();
    }

    public void rollStats() {
        currentAttack = Math.max(1, randomStat(playerAttackBase));
        currentDefense = Math.max(0, randomStat(playerDefenseBase));
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return currentAttack;
    }

    public int getDefense() {
        return currentDefense;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
    }

    public String getStatsText() {
        return "Útok: " + currentAttack + "  |  Obrana: " + currentDefense
                + "  |  HP: " + currentHp + "/" + maxHp + "  (staty se mění)";
    }
}
