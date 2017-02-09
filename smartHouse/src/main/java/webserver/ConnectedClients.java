package webserver;

import java.util.ArrayList;
import java.util.List;

public class ConnectedClients {
	private static final ConnectedClients INSTANCE = new ConnectedClients();

	public static ConnectedClients getInstance() {
		return INSTANCE;
	}

	private List<WebSocketHandler> members = new ArrayList();

	public void join(WebSocketHandler socket) {
		members.add(socket);
	}

	public void part(WebSocketHandler socket) {
		members.remove(socket);
	}

	public void writeAllMembers(String message) {
		for (WebSocketHandler member : members) {
			member.session.getRemote().sendStringByFuture(message);
		}
	}

//    public void writeSpecificMember(String memberName, String message) {
//        WebSocketHandler member = findMemberByName(memberName);
//        member.session.getRemote().sendStringByFuture(message);
//    }

//    public WebSocketHandler findMemberByName(String memberName) {
//
//    }
}