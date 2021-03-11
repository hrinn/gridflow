package security;

import application.events.GridFlowEventManager;
import application.events.LoginEvent;

public class SecurityController {

    CredentialManager credentialManager;
    GridFlowEventManager eventManager;

    public SecurityController(GridFlowEventManager eventManager) {
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
        eventManager.sendLoginEvent(new LoginEvent());
        return true;
    }

    public void testLogin() {
        tryLogin("lefty", "powerball"); // test
    }

}
