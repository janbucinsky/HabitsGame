import java.util.Random;

//Protivník v aréně – staty se náhodně mění podle hráče.
public class Opponent {
    private static final int STAT_OFFSET_DOWN = 7;
    private static final int STAT_OFFSET_UP = 5;
    private static final int[] BOSS_HP = {300, 700, 1500};
    private static final int[] BOSS_ATTACK = {12, 22, 38};
    private static final int[] BOSS_DEFENSE = {3, 8, 15};

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
    private boolean bossMode;
    private int bossAttackBase;
    private int bossDefenseBase;

    // Vytvoří soupeře podle statu hráče
    public Opponent(int playerAttack, int playerDefense, int playerHp) {
        this.playerAttackBase = playerAttack;
        this.playerDefenseBase = playerDefense;
        resetOpponent(playerHp);
    }

    // boss ma pevny HP a vlastni utok/obranu
    public void resetAsBoss(int bossNumber) {
        int index = bossNumber - 1;
        if (index < 0 || index >= BOSS_HP.length) {
            index = 0;
        }
        this.bossMode = true;
        this.name = "Boss " + bossNumber;
        this.maxHp = BOSS_HP[index];
        this.currentHp = maxHp;
        this.bossAttackBase = BOSS_ATTACK[index];
        this.bossDefenseBase = BOSS_DEFENSE[index];
        rollStats();
    }

    public static int getBossAttack(int bossNumber) {
        int index = bossNumber - 1;
        if (index < 0 || index >= BOSS_ATTACK.length) {
            return BOSS_ATTACK[0];
        }
        return BOSS_ATTACK[index];
    }

    public static int getBossDefense(int bossNumber) {
        int index = bossNumber - 1;
        if (index < 0 || index >= BOSS_DEFENSE.length) {
            return BOSS_DEFENSE[0];
        }
        return BOSS_DEFENSE[index];
    }

    public static int getBossHp(int bossNumber) {
        int index = bossNumber - 1;
        if (index < 0 || index >= BOSS_HP.length) {
            return BOSS_HP[0];
        }
        return BOSS_HP[index];
    }

    public static int getBossCount() {
        return BOSS_HP.length;
    }

    // Nastaví nového soupeře s náhodným jménem a staty.
    public void resetOpponent(int playerHp) {
        this.bossMode = false;
        this.name = NAMES[random.nextInt(NAMES.length)];
        this.maxHp = Math.max(1, randomStat(playerHp));
        this.currentHp = maxHp;
        rollStats();
    }

    //náhodný posun statu.
    private int randomOffset() {
        return random.nextInt(STAT_OFFSET_DOWN + STAT_OFFSET_UP + 1) - STAT_OFFSET_DOWN;
    }

    // Vrátí stat + náhodný posun.
    private int randomStat(int baseStat) {
        return baseStat + randomOffset();
    }

    // přehodí útok a obranu (boss: vlastní základ ± offset, aréna: staty hráče ± offset).
    public void rollStats() {
        if (bossMode) {
            currentAttack = Math.max(1, randomStat(bossAttackBase));
            currentDefense = Math.max(0, randomStat(bossDefenseBase));
            return;
        }
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
