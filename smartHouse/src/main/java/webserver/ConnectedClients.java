package webserver;

import javax.json.Json;
import java.util.ArrayList;
import java.util.List;

public class ConnectedClients {
    private static final ConnectedClients INSTANCE = new ConnectedClients();

    public static ConnectedClients getInstance() {
        return INSTANCE;
    }

    private List<MyWebSocketHandler> members = new ArrayList<>();

    public void join(MyWebSocketHandler socket) {
        members.add(socket);
    }

    public void part(MyWebSocketHandler socket) {
        members.remove(socket);
    }

    public void writeAllMembers(String message) {
        for(MyWebSocketHandler member: members) {
            member.session.getRemote().sendStringByFuture(message);
        }
    }

//    public void writeSpecificMember(String memberName, String message) {
//        MyWebSocketHandler member = findMemberByName(memberName);
//        member.session.getRemote().sendStringByFuture(message);
//    }

//    public MyWebSocketHandler findMemberByName(String memberName) {
//
//    }
}