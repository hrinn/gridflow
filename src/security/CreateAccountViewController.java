package security;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class CreateAccountViewController {

    public TextField newUsername;
    public TextField newPassword;
    public TextField confirmPassword;
    public ListView<String> usernamesList;
    public ComboBox accessSelector;

    private AccountController controller;

    public void setController(AccountController controller) {
        this.controller = controller;
    }

    public void setUserItemsList(List<String> userItems) {
        ObservableList<String> items = FXCollections.observableList(userItems);

        usernamesList.setItems(items);
    }

    public void initialize() {
        accessSelector.getItems().setAll("Viewer", "Builder", "God");
    }

    @FXML
    private void tryAdd() throws IOException {

        // Result is determined in the Credential Manager
        AccountResponse result = controller.tryAdd(newUsername.getText(), newPassword.getText(), confirmPassword.getText(),
                Access.valueOf(accessSelector.getValue().toString().toUpperCase()));

        resetFields();


        if (result != AccountResponse.GRANTED) {
            String error;
            if (result == AccountResponse.PASSNOMATCHERR) {
                error = "Password mismatch.";
            } else if (result == AccountResponse.USEREXISTSERR) {
                error = "User exists.";
            } else if (result == AccountResponse.INVALIDNAME) {
                error = "Invalid name.";
            } else {
                error = "Error!";
            }
            displayError(error);
        }
    }

    private void displayError(String error) {
        newUsername.setPromptText(error);
    }

    @FXML
    private void deleteSelected() {
        String userItem = usernamesList.getSelectionModel().getSelectedItem();
        // Parse the username out of the user item string. It is formatted as "name - [permission]"
        int i;
        for (i = 0; i < userItem.length(); i++) {
            if (userItem.charAt(i) == ' ') {
                break;
            }
        }
        // i is the position of the first space, grab the characters before there
        String username = userItem.substring(0, i);

        // Tell the controller to delete this username from the backend
        boolean success = controller.deleteUser(username);

        // Remove the username from the frontend list
        if (success) {
            usernamesList.getItems().remove(userItem);
        }
    }

    private void resetFields() {
        newUsername.clear();
        newPassword.clear();
        confirmPassword.clear();
    }
}
