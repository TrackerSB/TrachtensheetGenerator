package de.traunviertler_traunwalchen.trachtenSheetGenerator.gui.screens;

import java.util.ResourceBundle;

public class MainMenu extends Screen<MainMenuController> {

    public MainMenu() {
        super(MainMenu.class.getResource("mainMenu.fxml"),
                ResourceBundle.getBundle("de.traunviertler_traunwalchen.trachtenSheetGenerator.gui.screens.MainMenu"));
    }
}
