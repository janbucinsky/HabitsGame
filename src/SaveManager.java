//ukládání
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Ukládá a načítá hru do souboru save.dat.
public class SaveManager {
    private static final String SAVE_DIR_NAME = "HabitsGame";
    private static final String SAVE_FILE_NAME = "save.dat";

    private SaveManager() {
    }

    // Zapíše stav hry do souboru.
    public static void save(GameSaveData data) {
        if (data == null) {
            return;
        }

        File saveFile = getSaveFile();
        File parentDir = saveFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            output.writeObject(data);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    // Načte hru ze souboru, nebo vrátí null pokud soubor neexistuje.
    public static GameSaveData load() {
        File saveFile = getSaveFile();
        if (!saveFile.exists()) {
            return null;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile))) {
            Object loaded = input.readObject();
            if (loaded instanceof GameSaveData) {
                return (GameSaveData) loaded;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
        return null;
    }

    // Vrátí cestu k souboru save.dat v domovské složce uživatele.
    private static File getSaveFile() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, SAVE_DIR_NAME + File.separator + SAVE_FILE_NAME);
    }
}
//konec ukládání
