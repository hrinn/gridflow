package security;

import application.events.GridFlowEventManager;
import application.events.LoginEvent;

import java.io.IOException;

public class LoginController {

    CredentialManager credentialManager;
    GridFlowEventManager eventManager;

    public LoginController(GridFlowEventManager eventManager) {
        this.credentialManager = new CredentialManager();
        this.eventManager = eventManager;
        credentialManager.loadAccounts(); /* Load the accounts from the JSON file */
    }

    public boolean tryLogin(String username, String password) {
        Access result = credentialManager.checkPerms(username, password);
        if (result == Access.DENIED) {
            return false;
        }
        /* User logged in, boot rest of application */
        eventManager.sendApplicationOnlyEvent(new LoginEvent(result));
        return true;
    }
}
