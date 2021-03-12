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

        dialog.setScene(new Scene(createAccountView));
        dialog.setTitle("Account Manager");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(scene.getWindow());
        accountWindowDialog = dialog;
    }

    public void openAccountWindow() {
        accountWindowDialog.show();
    }

    public SecurityAccess tryAdd(String user, String password, String confirmPassword, Access access) throws IOException {
        // receives the new acccount info, and adds it in the credential manager

        return credentialManager.addAccount(user, password, confirmPassword, access);
    }
}
