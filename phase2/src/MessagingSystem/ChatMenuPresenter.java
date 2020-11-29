package MessagingSystem;

/**
 * Contains the text display for Chat Menu.
 *
 * @author Chrisee, Elliot
 */
public class ChatMenuPresenter extends CommandPresenter {

    /**
     * Prints the Chat Menu.
     */
    public String menuDisplay() {
        return "\n1) Send a message\n2) View Chat History\n";
    }

}