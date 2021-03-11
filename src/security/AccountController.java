package security;

import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import application.events.OpenAccountsEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;
import java.rmi.server.ExportException;

public class AccountController implements GridFlowEventListener {

    private CredentialManager credentialManager;
    private Scene scene;

    public AccountController(Scene scene) {
        this.credentialManager = new CredentialManager();
        this.scene = scene;
        credentialManager.loadAccounts(); /* Load the accounts from the JSON file */
    }

    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof OpenAccountsEvent) {
            try {
                openAccountWindow();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openAccountWindow() throws IOException {
        Stage dialog = new Stage();
        FXMLLoader createAccountViewLoader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
        Parent createAccountView = createAccountViewLoader.load();

        dialog.setScene(new Scene(createAccountView));
        dialog.setTitle("Account Manager");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(scene.getWindow());
        dialog.show();
    }

    public securityAccess tryAdd(String user, String password, String confirmPassword, Access access) throws IOException {
        return credentialManager.addAccount(user, password, confirmPassword, access);
    }
}
