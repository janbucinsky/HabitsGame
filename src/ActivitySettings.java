public class ActivitySettings {
    private final String unitLabel;
    private final int dailyTarget;

    public ActivitySettings(String unitLabel, int dailyTarget) {
        this.unitLabel = unitLabel;
        this.dailyTarget = dailyTarget;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public int getDailyTarget() {
        return dailyTarget;
    }

    public boolean isUnlimited() {
        return dailyTarget <= 0;
    }
}
