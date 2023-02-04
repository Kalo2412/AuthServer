package server.authorizator;

import server.models.User;

public interface Authorizator {
    public void authorizate(User user, String blueprint);
}
