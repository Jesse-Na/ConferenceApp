import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InboxController {
    private ChatController cc = new ChatController();
    private InboxPresenter ip = new InboxPresenter();
    private Scanner sc = new Scanner(System.in);
    private EventManager em;
    private User user;

    public InboxController(User user, EventManager em) {
        this.user = user;
        this.em = em; // TODO: update this when read events is implemented
    }
    private ArrayList<String> getUsersTalkto(User user, ChatroomManager cm) {
        ArrayList<String> users = new ArrayList<>();
        HashMap<ArrayList<String>, Chatroom> cms = cm.getAllChatrooms(user);
        for (ArrayList<String> key : cms.keySet()) {
            if (key.contains(user.getUserName())) {
                for (String person : key) {
                    if (!person.equals(user.getUserName())) {
                        users.add(person);
                    }
                }
            }
        }
        return users;
    }

    public void promptChatChoice() {
        ChatPull pull = new ChatPull();
        ChatroomManager cm = pull.getChatroomManager();
        ArrayList<String> friends = getUsersTalkto(user, cm);
        ip.menuDisplay(friends);
        ip.commandPrompt("chat");
        String recipient = sc.nextLine();
        while (!recipient.equals("$q")) {
            if (friends.contains(recipient)) {
                chatViewer(cm, recipient);
            } else {
                ip.invalidCommand("username");
                ip.menuDisplay(friends);
                recipient = sc.nextLine();
            }
        }
    }

    public void chatViewer(ChatroomManager cm, String recipient) {
        ip.chatView(cm.getChatroom(user, recipient));
        String e = "";
        while (!e.equals("$q")) {
            if (cc.canReply(user, recipient, cm)) {
                promptReply(user, recipient);
            }
            ip.exitMessage();
            e = sc.nextLine();
        }
    }

    public void promptReply(User user, String recipient) {
        OutboxController oc = new OutboxController(user, em);
        oc.promptMessage(recipient);
        ChatPull pull = new ChatPull();
        ChatroomManager cm = pull.getChatroomManager();
        ip.chatView(cm.getChatroom(user, recipient));
    }
}
