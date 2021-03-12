package security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateAccountViewController {

    public StackPane CreateAccountStackPane;
    public TextField newUsername;
    public TextField newPassword;
    public TextField confirmPassword;
    public Button createAccountButton;
    public RadioButton godButton;
    public RadioButton builderButton;
    public RadioButton viewerButton;
    public VBox usernamesVBox;

    private AccountController controller;

    public void setController(AccountController controller) {
        this.controller = controller;
    }

    public void setUsernameList(Set<String> usernames) {
        usernamesVBox.getChildren().clear();
        for (String username : usernames) {
            Label usernameLabel = new Label(username);
            usernamesVBox.setPrefHeight(usernamesVBox.getPrefHeight() + usernameLabel.getPrefHeight());
            usernamesVBox.getChildren().add(usernameLabel);
        }
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
