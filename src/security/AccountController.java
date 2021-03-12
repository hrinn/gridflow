package security;

import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import application.events.OpenAccountsEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;

public class AccountController implements GridFlowEventListener {

    private CredentialManager credentialManager;
    private Scene scene;
    private Stage accountWindowDialog;
    private CreateAccountViewController viewController;

    public AccountController(Scene scene) {
        this.credentialManager = new CredentialManager();
        this.scene = scene;
        // Initialize the accounts window, but don't show it yet
        try {
            initAccountWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        credentialManager.loadAccounts(); /* Load the accounts from the JSON file */
    }

    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof OpenAccountsEvent) {
            openAccountWindow();
        }
    }

    public void initAccountWindow() throws IOException {
        Stage dialog = new Stage();
        FXMLLoader createAccountViewLoader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
        Parent createAccountView = createAccountViewLoader.load();
        CreateAccountViewController vc = createAccountViewLoader.getController();
        vc.setController(this);
        this.viewController = vc;

        dialog.setScene(new Scene(createAccountView));
        dialog.setTitle("Account Manager");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(scene.getWindow());
        accountWindowDialog = dialog;
    }

    public void openAccountWindow() {
        accountWindowDialog.show();
        viewController.setUserItemsList(credentialManager.getUserItems());
    }

    public boolean deleteUser(String username) {
        return credentialManager.deleteAccount(username);
    }

    public AccountResponse tryAdd(String user, String password, String confirmPassword, Access access) throws IOException {
        // receives the new acccount info, and adds it in the credential manager
        AccountResponse response = credentialManager.addAccount(user, password, confirmPassword, access);
        if (response == AccountResponse.GRANTED) {
            // New user added, send a list to the view controller
            viewController.setUserItemsList(credentialManager.getUserItems());
        }
        return response;
    }
}
