import java.util.ArrayList;

public class ChatController {
    Chatter chatter = new Chatter();
    public boolean canSendOne(User user, String recipient, Message message) {
        if ((user instanceof Attendee || user instanceof Organizer) && user.getFriends().contains(recipient)) {
            ArrayList<String> recipients = new ArrayList<>();
            recipients.add(user.getUserName());
            recipients.add(recipient);
            chatter.sendOne(recipients, message);
            return true;
        }
        return false;
    }

}
