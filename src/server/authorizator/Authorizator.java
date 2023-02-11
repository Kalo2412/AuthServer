package server.authorizator;

import server.models.AEAuthenticatedUser;
import server.models.User;

public interface Authorizator {
    public AEAuthenticatedUser authorizate(User user);
}
