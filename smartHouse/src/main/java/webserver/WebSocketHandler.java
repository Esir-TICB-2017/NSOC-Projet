package webserver;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.api.client.auth.openidconnect.IdToken;
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
import utils.Utils;


@WebSocket
public class WebSocketHandler {
	private final static HashMap<String, WebSocketHandler> sockets = new HashMap();
	public Session session;
	private String userId;
	private String role;
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
		IdToken idToken = SessionManager.getGoogleIdToken(idTokenString);
		if (idToken != null) {
			String email = (String) idToken.getPayload().get("email");
			String currentToken = ReadInDatabase.getCurrentToken(email);
			if (currentToken != null) {
				Timestamp sessionExpiration = SessionManager.getExpirationTime(currentToken);
				Timestamp currentDate = Utils.getCurrentTimeStamp();
				long remainingTime = sessionExpiration.getTime() - currentDate.getTime();
				if (remainingTime > 0) {
					this.userId = idToken.getPayload().getSubject();
					this.role = ReadInDatabase.getUserRole(email);
					session.setIdleTimeout(remainingTime);
					ConnectedClients.getInstance().join(this);
				}
				else {
					disconnect(session);
				}
			} else {
				disconnect(session);
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

		/*receiving a string like:
		{
		 key:
		 setting_id:
		 value:
		}
		*/

		JSONObject result = new JSONObject(message);
		if (result.has("key")) {
			String key = result.get("key").toString();
			String id;
			switch (key){
				case "settings" :
					id = getMyUniqueId();
					Boolean authorize = false;
					ArrayList<String> roles = ReadInDatabase.getSettingRole(result);
					for (String role : roles){
						if (role.equals(this.role)){
							authorize = true;
						}
					}
					if (authorize){
						WriteInDatabase.writeUserSetting(this.userId, result);
						ConnectedClients.getInstance().writeSpecificMember(id, "Setting "+result.get("setting_id")+" sent. ");
					}
					else{
						ConnectedClients.getInstance().writeSpecificMember(id, "impossible");
					}
					break;
				case "userRole" :
					id = getMyUniqueId();
					if (this.role.equals("admin")){
						WriteInDatabase.writeNewRole(result);
						ConnectedClients.getInstance().writeSpecificMember(id, "Role changed. ");
					}
					else {
						ConnectedClients.getInstance().writeSpecificMember(id, "impossible");
					}
					break;
				case "deleteUser" :
					id = getMyUniqueId();
					if (this.role.equals("admin")){
						WriteInDatabase.deleteUser(result);
						ConnectedClients.getInstance().writeSpecificMember(id, "User deleted. ");
					}
					else {
						ConnectedClients.getInstance().writeSpecificMember(id, "impossible");
					}
					break;
				case "addUser" :
					id = getMyUniqueId();
					if (this.role.equals("admin")){
						WriteInDatabase.addUser(result);
						ConnectedClients.getInstance().writeSpecificMember(id, "User added. ");
					}
					else {
						ConnectedClients.getInstance().writeSpecificMember(id, "impossible");
					}
					break;
				default:
					break;
			}
		}
	}
}