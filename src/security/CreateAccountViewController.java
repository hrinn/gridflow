package security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CreateAccountViewController {

    public StackPane CreateAccountStackPane;
    public TextField newUsername;
    public TextField newPassword;
    public TextField confirmPassword;
    public Button createAccountButton;
    public RadioButton godButton;
    public RadioButton builderButton;
    public RadioButton viewerButton;

    private AccountController controller;

    public void setController(AccountController controller) {
        this.controller = controller;
    }

    @FXML
    private void tryAdd() throws IOException {
        SecurityAccess result;
        Access access;

        if (godButton.isSelected()) {
            access = Access.GOD;
        } else if (builderButton.isSelected()) {
            access = Access.BUILDER;
        } else if (viewerButton.isSelected()) {
            access = Access.VIEWER;
        } else {
            resetFields();
            displayError("Choose perms!");
            return;
        }

        // Result is determined in the Credential Manager
        result = controller.tryAdd(newUsername.getText(), newPassword.getText(), confirmPassword.getText(), access);

        resetFields();


        if (result != SecurityAccess.GRANTED) {
            String error;
            if (result == SecurityAccess.PASSNOMATCHERR) {
                error = "Password mismatch.";
            } else if (result == SecurityAccess.USEREXISTSERR) {
                error = "User exists!";
            } else {
                error = "Error!";
            }
            displayError(error);
        }
    }

    private void displayError(String error) {
        newUsername.setPromptText(error);
        newPassword.setPromptText(error);
        confirmPassword.setPromptText(error);
    }

    private void resetFields() {
        newUsername.clear();
        newPassword.clear();
        confirmPassword.clear();
        godButton.setSelected(false);
        builderButton.setSelected(false);
        viewerButton.setSelected(false);
    }
}
