SETUP: Follow the instructions in SETUP INSTRUCTIONS.mp4 to setup mongodb. Make sure to hit apply.
You can find the driver pre downloaded in our phase 2 folder (mongo-java-driver-3.12.7.jar).
Alternatively, if you cannot setup mongodb, watch the video demo of our program instead.
[https://youtu.be/b-LFwrDeQXw]

Additionally, you will need to set up a MongoDB database and set the environment variable DBPATH as the link to said database.

1. Run main. This will create an application window. Login music will play, turn your volume down!
   NOTE: Please do not resize the window to be bigger/smaller if possible. A maximum/minimum window size is not set
   because JavaFX is known to have display issues on devices with high DPI if a maximum/minimum window size is set.

2. Follow prompts to Login as existing user. Alternatively, you can Create Account to make your own attendee account.
   NOTE: If you create your own attendee account, you will not have access to all of the options listed below.

We have created an administrator account with access to all features for you:
username: admin
password: 123

3. As Admin:
    a) Choose Events. On the left side is the list of events you are signed up for. On the right side is the list of all events.
       You can Join, Leave, or get Info on an event by inputting its id (in the form 1607644938098, for example).
       You can also Download Event Schedule to produce an html schedule of the events you are signed up for.
       Click the Back button to return to the options home screen.

	b) Choose Friends. This will display a list of your friends.
	   You can Add Friend by inputting their username, if known.
	   You can also Remove Friend by inputting their username from your displayed friends list.
	   Click the Back button to return to the options home screen.

	c) Choose Password. This will allow you to change your password.
	   You must correctly enter your Username, Current Password, New Password, and Confirm New Password to change your password.
	   Click the Go Back button to return to the options home screen.

	d) Choose Messages. See the note below "ON USING MESSAGE INBOX/OUTBOX".
	   Click the red "X" at the top left corner to return to the options home screen.

	e) Choose Manage Events. NOTE: This option is only available to organizers and administrator user types.
	   There will be a list of events displayed.
	   You can Create an event. Follow the prompts given.
	   You can Modify the name and capacity of an existing event by inputting its id.
	   You can Remove an event by inputting its id.
       Click the Back button to return to the options home screen.

	f) Choose Manage Rooms. NOTE: This option is only available to administrator user types.
	   There will be a list of rooms.
	   You can Create a room by inputting any room name (ID) and a capacity.
	   You can Remove a room by selecting it from the list that appears.
	   Click the Back button to return to the options home screen.

	g) Choose Manage Accounts. NOTE: This option is only available to organizer and administrator user types.
       You can view a list of users by type by selection an option from the drop-downmenu.
       You can Create a user by following the prompts.
       You can Delete a user by inputting their username.
       You can Modify a user by following the prompts.
       Click the Back button to return to the options home screen.

	h) Choose Save. This will save any changes you have made to the database.
	i) Alternatively, choose Logout. This will return you to the login screen, and also save any changes made simultaneously.

    j) At the login or account creation screen, you may Quit to exit the program.

4. We've included javadoc for each public class and method.

5. You can find UML diagrams in the folder "design". There is a diagram for package interactions (design.pdf).
   There are also diagrams for interactions within each package (eg. DatabaseSystemDesign.pdf).

6. There is Phase 2 Features.txt, which is a list of features we implemented for phase 2.

7. There is List of Design Patterns.txt, which are the design patterns we implemented.

8. Again, here is a video of us demoing our program [https://youtu.be/b-LFwrDeQXw].



===============================
 ON USING MESSAGE INBOX/OUTBOX
===============================
NOTE: Please add a friend to chat with that user.
1. Select a chatroom from the left to access chat history.
2. Enter text in the text box and click the "send" button to send it. Multi-line messages are allowed.
    a) Most profanities will be filtered out automatically and replaced with math terms.
3. For each message, click "Delete" to delete the message from chat, "Pin Message/Unpin Message"
   to pin or unpin the message, and "Mark As Read/Mark as Unread" to mark the message as read or unread.
    a) Message deletion is available to every user, but only admins can delete other users' messages.
    b) Pinned messages in a specific chatroom can be accessed by clicking the "Pins" button while in that chatroom.
    c) Only other users' messages can be marked as read/unread.
4. Click the "Search user..." to look up a chatroom.
5. Click the blue "+" button to open a message outbox that can send messages to multiple people at once.
    a) This feature is NOT AVAILABLE TO ATTENDEES.
    b) Select "All speakers" to send a message to every speaker who is hosting an event. (This does not include
       speakers whose accounts exist, but are not giving talks.
    c) Select an event to send a message to all attendees of that event.
