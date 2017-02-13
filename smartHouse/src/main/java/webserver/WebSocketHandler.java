package webserver;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import database.ReadInDatabase;
import database.WriteInDatabase;
import database.data.DataRecord;
import indicators.Indicator;
import indicators.Indicators;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Constants;

import static database.Database.getCurrentTimeStamp;

@WebSocket
public class WebSocketHandler {
    private final static HashMap<String, WebSocketHandler> sockets = new HashMap();
    public Session session;
    private String myUniqueId;

    private String getMyUniqueId() {
        // unique ID from this class' hash code
        return Integer.toHexString(this.hashCode());
    }

    private void disconnect(Session session) {
        try {
            session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        String tokenId = session.getUpgradeRequest().getQueryString();
        if (tokenId != null) {
            try {
                HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
                JsonFactory jsonFactory = new JacksonFactory();
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(Collections.singletonList(Constants.CLIENT_ID))
                        .build();

                GoogleIdToken idToken = null;
                try {
                    idToken = verifier.verify(tokenId);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (idToken != null) {
                    IdToken.Payload payload = idToken.getPayload();
                    String userId = payload.getSubject();
                    HashMap userSession = ReadInDatabase.getUserSession(userId).get(0);
                    Timestamp currentDate = getCurrentTimeStamp();
                    Timestamp expirationDate = (Timestamp) userSession.get("expiration_date");
                    long remainingTime = expirationDate.getTime() - currentDate.getTime();
                    if (userSession.isEmpty() || !userSession.containsKey("token") || remainingTime < 0) {
                        disconnect(session);
                    } else {
                        session.setIdleTimeout(remainingTime);
                        ConnectedClients.getInstance().join(this);
                    }
                } else {
                    disconnect(session);
                }
            } catch (Exception e) {
                disconnect(session);
                e.printStackTrace();
            }
        } else {
            disconnect(session);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        ConnectedClients.getInstance().part(this);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketMessage
    public void onText(String message) {
        this.session = session;
        String tokenId = session.getUpgradeRequest().getQueryString();
        if (tokenId != null) {
            try {
                HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
                JsonFactory jsonFactory = new JacksonFactory();
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(Collections.singletonList(Constants.CLIENT_ID))
                        .build();

                GoogleIdToken idToken = null;
                try {
                    idToken = verifier.verify(tokenId);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (idToken != null) {
                    IdToken.Payload payload = idToken.getPayload();
                    String userId = payload.getSubject();

                    JSONObject result = new JSONObject(message);

                    if(result.get("key").equals("settings")) {
                        WriteInDatabase.writeUserSettings(userId, result);
                    }

                } else {
                    disconnect(session);
                }

            } catch (Exception e) {
                disconnect(session);
                e.printStackTrace();
            }
        } else {
            disconnect(session);
        }
    }
}