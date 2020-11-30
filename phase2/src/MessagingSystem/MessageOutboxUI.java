package MessagingSystem;

import UserSystem.*;
import EventSystem.*;
import java.util.*;

/**
 * OutboxController handles user requests for sending messages.
 *
 * @author  Chrisee Zhu
 */
public class MessageOutboxUI implements MessageUI{
    private MessageOutboxController oc;
    private MessageOutboxPresenter op;
    private Scanner sc = new Scanner(System.in);

    /**
     * initializes a new OutboxController.
     *
     * @param oc        OutboxController
     * @param op        OutboxPresenter
     */
    public MessageOutboxUI(MessageOutboxController oc, MessageOutboxPresenter op) {
        this.op = op;
        this.oc = oc;
    }

    /**
     * Prompts and processes a user's request for message composition options by target.
     */
    public void promptChatChoice() {
        System.out.println(op.menuDisplay());
        System.out.println(op.commandPrompt("prompt"));
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
            System.out.println(op.menuDisplay());
            System.out.println(op.commandPrompt("prompt"));
            choice = sc.nextLine();
        }
    }

    /**
     * Prompts and processes a user's request to message a specific friend by username.
     */
    public void promptRecipient() {
        System.out.println(op.friendMenu());
        System.out.println(op.commandPrompt("username"));
        String recipient = sc.nextLine();
        while (!recipient.equals("$q")) {
            recipient = recipient.replace("@", "");
            if (oc.canMessage(recipient)) {
                promptMessage(recipient);
            } else {
                System.out.println(op.invalidCommand("username"));
            }
            System.out.println(op.friendMenu());
            System.out.println(op.commandPrompt("username"));
            recipient = sc.nextLine();

        }
    }

    private ArrayList<String> convertSpeakers(String speakers) {
        String[] speakerArray = speakers.split("[ ]+", 0);
        return new ArrayList<>(Arrays.asList(speakerArray));
    }

    /**
     * Prompts and processes a user's request to message one or more speakers by username.
     */
    public void promptSpeaker() {
        System.out.println(op.speakerMenu());
        System.out.println(op.commandPrompt("speaker username (separate usernames with a space, enter * to send all)"));
        String speakers = sc.nextLine();
        while (!speakers.equals("$q")) {
            speakers = speakers.replace("@", "");
            ArrayList<String> speakerArrayList = convertSpeakers(speakers);
            if (speakers.equals("*")) {
                ArrayList<String> s = oc.getMessageSpeakers();
                if (oc.canSendSpeakers(s)) {
                    promptMessage(s);
                }
            } else if (oc.canSendSpeakers(speakerArrayList)) {
                promptMessage(speakerArrayList);
            } else {
                System.out.println(op.invalidCommand("username"));
            }
            System.out.println(op.speakerMenu());
            System.out.println(op.commandPrompt("speaker username (string after the @, separate usernames with a space)"));
            speakers = sc.nextLine();
        }
    }

    private ArrayList<Long> convertLong(String longs) {
        String[] longArray = longs.split("[ ]+", 0);
        ArrayList<Long> longArrayList = new ArrayList<>();
        for (String s : longArray) {
            longArrayList.add(Long.valueOf(s));
        }
        return longArrayList;
    }

    /**
     * Prompts and processes a user's request to message all attendees of a specific event by ID.
     */
    public void promptEvent() {
        System.out.println(op.eventMenu());
        System.out.println(op.commandPrompt("event ID (separate IDs with a space, enter * to send all)"));
        String evt = sc.nextLine();
        while (!evt.equals("$q")) {
            if (evt.equals("*")) {
                if (oc.canSendAllEvents()) {
                    promptEventMessage(oc.getAllEventIDs());
                }
            } else if (evt.replace(" ", "").matches("^[0-9]+$")) {
                ArrayList<Long> event_ids = convertLong(evt);
                if (oc.canSendEvents(event_ids)) {
                    promptEventMessage(event_ids);
                } else {
                    System.out.println(op.invalidCommand("event ID (make sure they are valid)"));
                }
            } else {
                System.out.println(op.invalidCommand("event ID (unexpected character)"));
            }
            System.out.println(op.eventMenu());
            System.out.println(op.commandPrompt("event ID (separate IDs with a space)"));
            evt = sc.nextLine();
        }
    }

    /**
     * Prompts and processes a user's request to send a message to a user.
     *
     * @param recipient     the username of the target user; target user must be a friend of the sender
     */
    public void promptMessage(String recipient) {
        System.out.println(op.commandPrompt("message (requires at least 1 character)"));
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (oc.validateMessage(message)) {
                oc.sendMessage(recipient, message);
                System.out.println(op.success(recipient));
                message = "$q";
            } else {
                System.out.println(op.invalidCommand("message"));
                System.out.println(op.commandPrompt("message (requires at least 1 character)"));
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
        System.out.println(op.commandPrompt("message (requires at least 1 character)"));
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (oc.validateMessage(message)) {
                for (Long event_id: event_ids) {
                    if (oc.sendMessage(event_id, message)) {
                        System.out.println(op.success("event: " + event_id));
                    } else {
                        System.out.println(op.invalidCommand("event id, message not sent to event: " + event_id));
                    }
                }
                message = "$q";
            } else {
                System.out.println(op.invalidCommand("message"));
                System.out.println(op.commandPrompt("message (requires at least 1 character)"));
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
        System.out.println(op.commandPrompt("message (requires at least 1 character)"));
        String message = sc.nextLine();
        while (!message.equals("$q")) {
            if (oc.validateMessage(message)) {
                for (String speaker : speakers) {
                    oc.sendMessage(speaker, message);
                    System.out.println(op.success("@" + speaker));
                }
                message = "$q";
            } else {
                System.out.println(op.invalidCommand("message"));
                System.out.println(op.commandPrompt("message (requires at least 1 character)"));
                message = sc.nextLine();
            }
        }
    }
}
