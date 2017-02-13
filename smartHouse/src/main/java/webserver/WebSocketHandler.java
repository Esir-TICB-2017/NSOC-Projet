package webserver;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.google.api.client.auth.openidconnect.IdToken;
import database.ReadInDatabase;
<<<<<<< HEAD
import database.WriteInDatabase;
import database.data.DataRecord;
import indicators.Indicator;
import indicators.Indicators;
=======
>>>>>>> 52082f1e8dd8b323f941b4cbe1c1835b69fe41ac
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
<<<<<<< HEAD
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Constants;
=======
import utils.Utils;
>>>>>>> 52082f1e8dd8b323f941b4cbe1c1835b69fe41ac


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
		String idTokenString = session.getUpgradeRequest().getQueryString();
		if (SessionManager.checkAuthentication(idTokenString)) {
			Timestamp currentDate = Utils.getCurrentTimeStamp();
			Timestamp expirationDate = SessionManager.getExpirationTime(idTokenString);
			long remainingTime = expirationDate.getTime() - currentDate.getTime();
			session.setIdleTimeout(remainingTime);
			ConnectedClients.getInstance().join(this);
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

	    
	    JSONObject result = new JSONObject(message);

        if(result.get("key").equals("settings")) {
            WriteInDatabase.writeUserSettings(userId, result);
        }

    }
}