public class Player {
    private int level = 1;
    private int xp = 0;
    private int gold = 0;

    public void addXp(int amount) {
        if (amount < 0) return;
        xp += amount;

        while (level < 30 && xp >= level * 100) {
            xp -= level * 100;
            level++;
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
