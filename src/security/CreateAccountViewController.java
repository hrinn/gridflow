package security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
        securityAccess result;
        if (godButton.isSelected()) {
            result = controller.tryAdd(newUsername.getText(), newPassword.getText(), confirmPassword.getText(), Access.GOD);
        }
        else if (builderButton.isSelected()) {
            result = controller.tryAdd(newUsername.getText(), newPassword.getText(), confirmPassword.getText(), Access.BUILDER);
        }
        else if (viewerButton.isSelected()) {
            result = controller.tryAdd(newUsername.getText(), newPassword.getText(), confirmPassword.getText(), Access.VIEWER);
        }
        else
        {
            godButton.setSelected(false);
            builderButton.setSelected(false);
            viewerButton.setSelected(false);
            newUsername.clear();
            newPassword.clear();
            confirmPassword.clear();
            newUsername.setPromptText("Choose Perms!");
            newPassword.setPromptText("Choose Perms!");
            confirmPassword.setPromptText("Choose Perms!");
            return;
        }
        if (result != securityAccess.GRANTED) {
            godButton.setSelected(false);
            builderButton.setSelected(false);
            viewerButton.setSelected(false);
            newUsername.clear();
            newPassword.clear();
            confirmPassword.clear();
            if (result == securityAccess.PASSNOMATCHERR)
            {
                newUsername.setPromptText("PASSWD Match!");
                newPassword.setPromptText("PASSWD Match!");
                confirmPassword.setPromptText("PASSWD Match!");
            }
            else if (result == securityAccess.USEREXISTSERR)
            {
                newUsername.setPromptText("User Exists!");
                newPassword.setPromptText("User Exists!");
                confirmPassword.setPromptText("User Exists!");
            }
        }
    }
}
