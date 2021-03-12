package application.events;

import security.Access;

public class LoginEvent implements GridFlowEvent {
    private Access access;

    public LoginEvent(Access access) {
        this.access = access;
    }

    public Access getAccess() {
        return access;
    }
}
