package server.services.session;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

public class AESession {
    public static final int SESSION_TIMEOUT = 30;
    private final String uniqueID = UUID.randomUUID().toString();
    private Timestamp lastCommandTimestamp;
    public AESession() {
        this.lastCommandTimestamp = new Timestamp(System.currentTimeMillis());
    }
    private String getUniqueID() {
        return this.uniqueID;
    }
    public boolean validate() {
        Timestamp currentTimeStamp  = new Timestamp(System.currentTimeMillis());
        double differenceInMinutes = (currentTimeStamp.getTime() - lastCommandTimestamp.getTime()) / 60d;
        if (differenceInMinutes > AESession.SESSION_TIMEOUT) {
            return false;
        } else {
            lastCommandTimestamp = currentTimeStamp;
            return true;
        }
    }

    @Override
    public int hashCode() {
        return this.uniqueID.hashCode();
    }
}
