import server.authenticator.AEAuthenticator;
import server.authorizator.AEAuthorizator;
import server.models.*;
import server.services.commands.AECommandService;
import server.services.commands.CommandService;
import server.services.database.AEDataBase;
import server.services.database.DataBase;
import server.services.proxy.AEProxyDB;
import server.services.proxy.ProxyDB;

import java.lang.reflect.Proxy;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        ProxyDB proxyDB = new AEProxyDB(AEDataBase.getDataBase(),
                new AEAuthenticator(AEDataBase.getDataBase()),
                new AEAuthorizator(AEDataBase.getDataBase()));
        CommandService commandService = new AECommandService(proxyDB);
        commandService.handleCommand("register --username kalo --password dsnjls --first-name kaloyan --last-name jsk --email ajksajk");
        commandService.handleCommand("register --username kalo --password dsnjls --first-name kaloyan --last-name jsk --email ajksajk");
        commandService.handleCommand("register --username balo --password dsnjls --first-name kaloyan --last-name jsk --email ajksajk");
    }
}


