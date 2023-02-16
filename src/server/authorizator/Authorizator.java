package server.authorizator;

import server.models.User;

public interface Authorizator {
    public boolean authorizate(User user);
}
