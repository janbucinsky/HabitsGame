# 🎮 HabitsGame (hra pro osobní rozvoj)

## 📝 Popis projektu
HabitsGame je jednoduchá 2D aplikace napsaná v Javě, která funguje jako kombinace "to-do listu" a RPG hry. Jejím cílem je motivovat uživatele ke zlepšování v reálném životě. Za splněné úkoly (např. pití vody, cvičení, čtení) získává uživatel zkušenosti (XP) a herní měnu. Postupně získává nové levely a může nakupovat lepší vybavení, které následně používá v soubojích s protivníky.

## 🌟 Hlavní funkcionality
* **Initial Setup:** Při prvním spuštění si uživatel vybere oblasti, na které se chce zaměřit.
* **RPG Prvky:** Získávání XP, zvyšování levelu, odemykání nových hrdinů a kupování předmětů.
* **Obchod (Shop):** Možnost utrácet získanou herní měnu za vybavení pro hrdiny.
* **Ukládání dat:** Veškerý progres se ukládá do textového souboru, takže uživatel může pokračovat tam, kde přestal.

## 🕹️ Ovládání aplikace
1. **Výběr cílů:** V prvním okně zaškrtněte oblasti, které chcete v rámci svých návyků sledovat.
2. **Plnění úkolů:** Na hlavní obrazovce uvidíte seznam návyků. Pokud jste Váš cíl dodrželi, můžete kliknout na tlačítko u úkolu pro získání XP a herní měny.
3. **Upgrade:** V okně obchodu můžete nakupovat vybavení a nové hrdiny.
4. **Uložení:** Aplikace ukládá data automaticky při změnách, nebo při zavření okna.

## 💻 OOP a technické detaily
* **Více oken:** Aplikace využívá minimálně 3 samostatná okna (JFrame) pro nastavení, hlavní přehled a obchod.
* **Dědičnost a rozhraní:** Použití základní třídy pro úkoly a hrdiny, implementace rozhraní pro prodejné položky.
* **Kolekce:** Správa seznamu úkolů a inventáře pomocí `ArrayList`.
* **Zabezpečení:** Ošetření vstupů a práce se soubory pomocí bloků `try-catch`.

## 🚀 Jak aplikaci spustit
1. Stáhněte si zdrojové kódy z tohoto repozitáře.
2. Otevřete projekt v libovolném Java IDE (např. IntelliJ IDEA).
3. Spusťte hlavní třídu `Main.java`.

## 🛠️ Použité technologie
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Swing-blue?style=for-the-badge)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
