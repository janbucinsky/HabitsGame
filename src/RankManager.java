// Převádí level hráče na textový rank (název).
public class RankManager {
    // Hodnosti AČR – emoji za názvem kvůli zobrazení na jednom řádku.
    public static final String[] RANKS = {
            "Vojín\u00A0\uD83E\uDE96",
            "Svobodník\u00A0\uD83C\uDF96\uFE0F",
            "Desátník\u00A0\u2B50",
            "Četař\u00A0\u2B50\u2B50",
            "Rotmistr\u00A0\uD83D\uDEE1\uFE0F",
            "Rotný\u00A0\uD83C\uDFAF",
            "Nadrotmistr\u00A0\uD83D\uDCE3",
            "Praporčík\u00A0\uD83D\uDEA9",
            "Nadpraporčík\u00A0\uD83C\uDF97\uFE0F",
            "Štábní praporčík\u00A0\u2694\uFE0F",
            "Poručík\u00A0\uD83C\uDF96\uFE0F",
            "Nadporučík\u00A0\uD83D\uDCBC",
            "Kapitán\u00A0\u2693",
            "Major\u00A0\uD83C\uDF1F",
            "Podplukovník\u00A0\uD83E\uDD85",
            "Plukovník\u00A0\uD83C\uDFF0",
            "Brigádní generál\u00A0\u2B50\u2B50\u2B50",
            "Generálmajor\u00A0\uD83C\uDFC5",
            "Generálporučík\u00A0\uD83C\uDF96\uFE0F",
            "Generál\u00A0\uD83D\uDC51"
    };

    public static final int MAX_LEVEL = RANKS.length;

    public static String getRank(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            return "Neznámý level";
        }
        return RANKS[level - 1];
    }
}
