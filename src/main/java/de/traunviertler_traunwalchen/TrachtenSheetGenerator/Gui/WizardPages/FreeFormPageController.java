package de.traunviertler_traunwalchen.TrachtenSheetGenerator.Gui.WizardPages;

import bayern.steinbrecher.wizard.WizardableController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Optional;
import java.util.ResourceBundle;

public class FreeFormPageController extends WizardableController<Optional<String>> {
    @FXML
    private TextArea freeFormContent;

    @Override
    protected Optional<ResourceBundle> getResourceBundle() {
        return Optional.empty();
    }

    @Override
    protected Optional<String> calculateResult() {
        return Optional.of(freeFormContent.getText());
    }
}
