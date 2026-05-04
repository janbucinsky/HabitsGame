public class RankManager {
    //levely
    public static final String[] RANKS = {
            "💩 Nováček", "🐌 Nýmand", "🐛 Amatér", "🥚 Učeň", "🐣 Pomocník",
            "🐭 Průzkumník", "🌱 Sledovač", "🎯 Hledač", "🦴 Dobrodruh", "🧱 Poutník",
            "🏹 Bojovník", "🛡️ Obránce", "⚔️ Válečník", "🐎 Veterán", "🦅 Důstojník",
            "🐺 Velitel", "🐅 Strážce", "⚒️ Ochránce", "🏰 Rytíř", "👺 Elita",
            "😎 Mistr", "🧙 Velmistr", "🦾 Expert", "🌋 Šampion", "💎 Hrdina",
            "👑 Vládce", "🐲 Dobyvatel", "👼 Panovník", "🌌 Legenda", "☀️ BŮH"
    };


    public static String getRank(int level) {
        if (level < 1 || level > 30) return "Neznámý Level";
        return RANKS[level];


    }

}

