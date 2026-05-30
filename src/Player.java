public class Player {
    private static final int GOLD_PER_LEVEL_UP = 50;

    private int level = 1;
    private int xp = 0;
    private int gold = 0;

    public void addXp(int amount) {
        if (amount < 0) return;
        xp += amount;

        while (level < 30 && xp >= level * 100) {
            xp -= level * 100;
            level++;
            gold += GOLD_PER_LEVEL_UP;
        }
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
}
