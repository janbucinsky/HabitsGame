// Vstupní bod aplikace – spustí hru.
public class Main {
    // Spustí aplikaci, načte uloženou hru a zobrazí hlavní okno.
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            UiTheme.apply();
            Player player = new Player();
            MainWindow mainWindow = new MainWindow(player);

            //ukládání
            GameSaveData save = SaveManager.load();
            if (save != null) {
                mainWindow.applySaveData(save);
            }
            
            mainWindow.showWindow();
        });
    }
}
