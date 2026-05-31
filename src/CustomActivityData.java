//ukládání
import java.io.Serializable;

// Data vlastní aktivity pro uložení do souboru.
public class CustomActivityData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String unitLabel;
    private final int dailyTarget;

    // Uloží název, jednotku a denní cíl vlastní aktivity.
    public CustomActivityData(String name, String unitLabel, int dailyTarget) {
        this.name = name;
        this.unitLabel = unitLabel;
        this.dailyTarget = dailyTarget;
    }

    // Vrátí název aktivity.
    public String getName() {
        return name;
    }

    // Vrátí jednotku splnění.
    public String getUnitLabel() {
        return unitLabel;
    }

    // Vrátí denní cíl.
    public int getDailyTarget() {
        return dailyTarget;
    }
}
//konec ukládání
