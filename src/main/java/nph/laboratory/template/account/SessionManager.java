package nph.laboratory.template.account;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SessionManager {

    private final ConcurrentMap<String, String> sessionStore;

    public SessionManager() {
        sessionStore = new ConcurrentHashMap<>();
    }

    public String newSession() {
        String uuid = UUID.randomUUID().toString();
        String session = Base64.getEncoder().encodeToString(uuid.getBytes());
        sessionStore.put(session, "");
        return session;
    }

    public boolean checkSessionExist(String session) {
        return sessionStore.containsKey(session);
    }
}
