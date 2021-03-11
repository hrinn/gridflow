package security;

public class SecurityController {

    CredentialManager credentialManager;

    public SecurityController() {
        this.credentialManager = new CredentialManager();
        credentialManager.loadAccounts();
    }

    public boolean tryLogin(String username, String password) {
        Access result = credentialManager.checkPerms(username, password);
        if (result == Access.DENIED) {
            return false;
        }

        /* User logged in, boot rest of application */

        return true;
    }

}
