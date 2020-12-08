package MainProgram;

import DatabaseSystem.DatabaseInteractor;
import DatabaseSystem.Savable;
import EventSystem.EventCreatorPresenter;
import EventSystem.EventManager;
import EventSystem.EventSignup;
import EventSystem.EventSignupPresenter;
import GUISystem.*;
import LoginSystem.LoginOptionsFacade;
import MainProgram.AdminCreationScript;
import MessagingSystem.*;
import UserSystem.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConferenceBuilder {
    // For database saving
    private DatabaseInteractor databaseInteractor;
    // For local saving
    // private MainProgram.LocalSave localSave;

    private Registrar registrar;
    private EventManager eventManager;
    private ChatroomManager chatroomManager;
    private HashMap<String, String> profanities;

    private EventSignup eventSignup = new EventSignup();
    private ChatMenuPresenter chatMenuPresenter = new ChatMenuPresenter();
    private FriendsPresenter friendsPresenter = new FriendsPresenter();

    private LoginOptionsFacade loginFacade;
    private EventCreatorPresenter eventCreatorPresenter;
    private EventSignupPresenter eventSignupPresenter;
    private FriendsController friendsController;

    // For database saving
    public ConferenceBuilder(DatabaseInteractor databaseInteractor){
        this.databaseInteractor = databaseInteractor;
    }

    // For local saving
    // public MainProgram.ConferenceBuilder(MainProgram.LocalSave localSave){
        // this.localSave = localSave;
    // }

    private void getSavables(){
        // For database saving
        this.registrar = (Registrar) databaseInteractor.readFromDatabase(new Registrar());
        this.eventManager = (EventManager) databaseInteractor.readFromDatabase(new EventManager());
        this.chatroomManager = (ChatroomManager) databaseInteractor.readFromDatabase(new ChatroomManager());
        this.profanities = databaseInteractor.getProfanityList();

        // For local saving
        // Registrar registrar = localSave.getRegistrar();
        // EventManager eventManager = localSave.getEventManager();
        // ChatroomManager chatroomManager = localSave.getChatroomManager();
        // HashMap<String, String> profanities = localSave.getProfanities();
    }

    private void setSavables(){
        ArrayList<Savable> savables = new ArrayList<>(Arrays.asList(registrar, eventManager, chatroomManager));
        databaseInteractor.setSavables(savables);
    }

    private void makeAdmins(){
        AdminCreationScript adminCreationScript = new AdminCreationScript();
        adminCreationScript.createAdmin(registrar);
    }

    private void makeOrganizers(){
        OrganizerCreationScript organizerCreationScript = new OrganizerCreationScript();
        organizerCreationScript.createOrganizers(registrar);
    }

    private void makeControllersPresenters(){
        this.loginFacade = new LoginOptionsFacade(registrar, eventManager, chatroomManager);
        this.eventCreatorPresenter = new EventCreatorPresenter(eventManager, registrar);
        this.eventSignupPresenter = new EventSignupPresenter(eventSignup, eventManager);
        this.friendsController = new FriendsController(registrar, friendsPresenter);
    }

    private void makeMenus(){
        // Create menus and dependency inject necessary classes
        MessageOutboxDataCollector outboxDateCollector = new MessageOutboxDataCollector("", registrar, eventManager);
        MessageOutboxController outboxController = new MessageOutboxController("", registrar, eventManager, chatroomManager);
        MessageOutboxPresenter outboxPresenter = new MessageOutboxPresenter(outboxController, outboxDateCollector);
        MessageOutboxGUI outboxGUI = new MessageOutboxGUI();
        outboxPresenter.setView(outboxGUI);
        outboxGUI.setOutboxElements(outboxPresenter);
        MessageInboxDataCollector inboxDataCollector = new MessageInboxDataCollector(registrar, "", chatroomManager, profanities);
        MessageInboxController inboxController = new MessageInboxController(registrar, "", chatroomManager);
        MessageInboxPresenter inboxPresenter = new MessageInboxPresenter(inboxDataCollector, inboxController);
        MessageInboxGUI inboxGUI = new MessageInboxGUI();
        inboxPresenter.setView(inboxGUI);
        inboxGUI.setInboxElements(inboxPresenter, outboxGUI);

        EventMenuGUI eventMenu = new EventMenuGUI();
        ManageEventMenu manageEventMenu = new ManageEventMenu();
        FriendsMenuGUI friendsMenuGUI = new FriendsMenuGUI();
        ManageAccountMenu manageAccountMenu = new ManageAccountMenu();
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setLogin(loginFacade);
        AccountCreationMenu accountCreationMenu = new AccountCreationMenu();
        accountCreationMenu.setLogin(loginFacade);
        PasswordMenu passwordMenu = new PasswordMenu();
        passwordMenu.setLogin(loginFacade);
        HomeMenuGUI homeMenuGUI = new HomeMenuGUI();
        homeMenuGUI.setLogin(loginFacade);
        homeMenuGUI.setMessageMenu(inboxGUI);
        homeMenuGUI.setEventMenu(eventMenu);
        homeMenuGUI.setManageEventMenu(manageEventMenu);
        homeMenuGUI.setFriendsMenu(friendsMenuGUI);
        homeMenuGUI.setManageAccountMenu(manageAccountMenu);
        homeMenuGUI.setPasswordMenu(passwordMenu);

        // For database saving
        homeMenuGUI.setSave(databaseInteractor);
        // For local saving
        // homeMenuGUI.setSave(MainProgram.LocalSave localSave);

        eventSignupPresenter.setInterface(eventMenu);
        eventCreatorPresenter.setInterface(manageEventMenu);
        friendsController.setInterface(friendsMenuGUI);
        eventMenu.setEventElements(eventSignupPresenter);
        manageEventMenu.setEventCreatorElements(eventCreatorPresenter);
        manageEventMenu.setFacade(loginFacade);
        manageAccountMenu.setFacade(loginFacade);
        friendsMenuGUI.setFriendsElements(friendsController);
        friendsMenuGUI.setFacade(loginFacade);

        // Create menu facade and DI menus
        MenuFacade menuFacade = new MenuFacade();
        menuFacade.set(loginGUI, accountCreationMenu, homeMenuGUI);

        //  Dependency inject userMenuGetter into various submenus
        inboxGUI.setUserMenuGetter(homeMenuGUI);
        passwordMenu.setUserMenuGetter(homeMenuGUI);
        eventMenu.setUserMenuGetter(homeMenuGUI);
        manageEventMenu.setUserMenuGetter(homeMenuGUI);
        friendsMenuGUI.setUserMenuGetter(homeMenuGUI);
        manageAccountMenu.setUserMenuGetter(homeMenuGUI);

        // Dependency inject MenuGetter into menus
        loginGUI.setMenuGetter(menuFacade);
        accountCreationMenu.setMenuGetter(menuFacade);
        homeMenuGUI.setMenuGetter(menuFacade);

        // Add menuFacade to the application
        LaunchMenu.setMenuFacade(menuFacade);
    }



    public void buildAConference(){
        getSavables();
        setSavables();
        makeAdmins();
        makeOrganizers();
        makeControllersPresenters();
        makeMenus();
    }

}