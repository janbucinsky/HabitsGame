//ukládání
import java.io.Serializable;

public class CustomActivityData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String unitLabel;
    private final int dailyTarget;

    public CustomActivityData(String name, String unitLabel, int dailyTarget) {
        this.name = name;
        this.unitLabel = unitLabel;
        this.dailyTarget = dailyTarget;
    }

    public String getName() {
        return name;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public int getDailyTarget() {
        return dailyTarget;
    }
}
//konec ukládání
