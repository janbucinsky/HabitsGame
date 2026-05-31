// Nastavení jedné aktivity – jednotka splnění a denní limit.
public class ActivitySettings {
    private final String unitLabel;
    private final int dailyTarget;

    // Vytvoří nastavení aktivity.
    public ActivitySettings(String unitLabel, int dailyTarget) {
        this.unitLabel = unitLabel;
        this.dailyTarget = dailyTarget;
    }

    // Vrátí text jednotky, např. „1 litr“.
    public String getUnitLabel() {
        return unitLabel;
    }

    // Vrátí kolikrát denně lze aktivitu splnit.
    public int getDailyTarget() {
        return dailyTarget;
    }

    // True, pokud aktivita nemá denní limit.
    public boolean isUnlimited() {
        return dailyTarget <= 0;
    }
}
