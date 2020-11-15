import java.util.*;

/**
 * OutboxController handles user requests for sending messages.
 *
 * @author  Chrisee Zhu
 * @version %I%, %G%
 */
public class OutboxController {
    private ChatController cc;
    private OutboxPresenter op;
    private EventManager em;
    private Scanner sc = new Scanner(System.in);
    private String username;
    private Registrar reg;

    /**
     * initializes a new OutboxController.
     *
     * @param reg       Registrar
     * @param username  the username of the sender
     * @param em        EventManager
     * @param op        OutboxPresenter
     */
    public OutboxController(Registrar reg, String username, EventManager em, OutboxPresenter op) {
        this.username = username;
        this.reg = reg;
        this.em = em;
        this.op = op;
        this.cc = new ChatController(username, reg, em);
    }

    /**
     * Prompts and processes a user's request for message composition options by target.
     */
    public void promptChatChoice() {
        op.menuDisplay();
        op.commandPrompt("prompt");
        String choice = sc.nextLine();
        while (!choice.equals("$q")) {
            switch (choice) {
                case "1":
                    promptRecipient();
                    break;
                case "2":
                    promptEvent();
                    break;
                case "3":
                    promptSpeaker();
                    break;
                default:
                    op.invalidCommand("prompt");
                    break;
            }
            op.menuDisplay();
            op.commandPrompt("prompt");
            choice = sc.nextLine();
        }
    }

    /**
     * Prompts and processes a user's request to message a specific friend by username.
     */
    public void promptRecipient() {
        op.friendMenu(reg, username);
        op.commandPrompt("username");
        String recipient = sc.nextLine();
        while (!recipient.equals("$q")) {
            recipient = recipient.replace("@", "");
            if (cc.canMessage(recipient)) {
                promptMessage(recipient);
            } else {
                op.invalidCommand("username");
            }
            op.friendMenu(reg, username);
            op.commandPrompt("username");
            recipient = sc.nextLine();

        }
    }

    private ArrayList<String> convertSpeakers(String speakers) {
        String[] speakerArray = speakers.split(" ", 0);
        return new ArrayList<>(Arrays.asList(speakerArray));
    }

    /**
     * Prompts and processes a user's request to message one or more speakers by username.
     */
    public void promptSpeaker() {
        op.speakerMenu(reg, em);
        op.commandPrompt("speaker username (separate usernames with a space)");
        String speakers = sc.nextLine();
        while (!speakers.equals("$q")) {
            speakers = speakers.replace("@", "");
            ArrayList<String> speakerArrayList = convertSpeakers(speakers);
            if (cc.canSendSpeakers(speakerArrayList)) {
                promptMessage(speakerArrayList);
            } else {
                op.invalidCommand("username");
            }
            op.speakerMenu(reg, em);
            op.commandPrompt("speaker username (string after the @, separate usernames with a space)");
            speakers = sc.nextLine();
        }
    }

    private ArrayList<Long> convertLong(String longs) {
        String[] longArray = longs.split(" ", 0);
        ArrayList<Long> longArrayList = new ArrayList<>();
        for (String s : longArray) {
            longArrayList.add(Long.valueOf(s));
        }
        return longArrayList;
    }

    private void loadEventMenu() {
        if (reg.isSpeaker(username)) {
            op.eventMenu(username, em, reg);
        } else if (reg.isOrganizer(username)) {
            op.eventMenu(em);
        }
    }

    /**
     * Prompts and processes a user's request to message all attendees of a specific event by ID.
     */
    public void promptEvent() {
        loadEventMenu();
        op.commandPrompt("event ID (separate IDs with a space)");
        String evt = sc.nextLine();
        while (!evt.equals("$q")) {
            if (evt.replace(" ", "").matches("^[0-9]+$")) {
                ArrayList<Long> event_ids = convertLong(evt);
                if (cc.canSendEvents(event_ids)) {
                    promptEventMessage(event_ids);
                } else {
                    op.invalidCommand("event ID (make sure they are valid)");
                }
            } else {
                op.invalidCommand("event ID (unexpected character)");
            }
            loadEventMenu();
            op.commandPrompt("event ID (separate IDs with a space)");
            evt = sc.nextLine();
        }
    }

    /**
     * Prompts and processes a user's request to send a message to a user.
     *
     * @param destination     the username of the target user; target user must be a friend of the sender
     */
    public void promptMessage(String destination) {
        op.commandPrompt("message (requires at least 1 character)");
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (cc.validateMessage(message)) {
                cc.sendMessage(destination, message);
                op.success();
                message = "$q";
            } else {
                op.invalidCommand("message");
                op.commandPrompt("message (requires at least 1 character)");
                message = sc.nextLine();
            }
        }
    }

    /**
     * Prompts and processes a user's request to send a message to all attendees of an event.
     *
     * @param event_ids        the ID of the event; events must exist in EventManager
     */
    public void promptEventMessage(ArrayList<Long> event_ids) {
        op.commandPrompt("message (requires at least 1 character)");
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (cc.validateMessage(message)) {
                ArrayList<String> recipients = new ArrayList<String>();
                for (Long event_id: event_ids){
                    try {
                        for(String recipient: em.getSignedUpUsers(event_id)){
                            recipients.add(recipient);
                            System.out.println("@"+recipient);
                        }
                    } catch (EventNotFoundException e) {
                        System.out.println("Some events are not yet registered. Please try again");
                    }
                }
                System.out.println("Type usernames separated by a space, or select * to message all users");
                String response = sc.nextLine().replace("@", "");
                if (response.equals("")|response.equals(" ")){
                    System.out.println("No users specified.");
                    return;
                }
                if (response.equals("*")){
                for (Long event_id : event_ids) {
                    cc.sendMessage(event_id, em.getEvent(event_id).getName() + ": " + message);
                }}
                else{
                    for(String recipient: response.split(" ")) {
                        if (recipients.contains(recipient)){
                            cc.sendMessage(recipient, message);}
                        else{
                            System.out.println("Message could not be delivered to " + recipient + ". They are not registered in your specified events.");
                        }
                    }
                    }

                op.success();
                message = "$q";
            } else {
                op.invalidCommand("message");
                op.commandPrompt("message (requires at least 1 character)");
                message = sc.nextLine();
            }
        }
    }

    /**
     * Prompts and processes a user's request to send a message to one or more speakers.
     *
     * @param speakers      the username(s) of the speakers; speakers must exist in Registrar
     */
    public void promptMessage(ArrayList<String> speakers) {
        op.commandPrompt("message (requires at least 1 character)");
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (cc.validateMessage(message)) {
                for (String speaker : speakers) {
                    cc.sendMessage(speaker, message);
                }
                op.success();
                message = "$q";
            } else {
                op.invalidCommand("message");
                op.commandPrompt("message (requires at least 1 character)");
                message = sc.nextLine();
            }
        }
    }
}
