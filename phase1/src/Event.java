import java.util.ArrayList;
import java.time.*;

public class Event {

    private String name;
    private String room;
    private LocalDateTime time;
    private Speaker speaker;
    private int capacity;
    private ArrayList<String> signedUpUsers = new ArrayList<String>();

    public Event(String name, String room, LocalDateTime time, Speaker speaker, int capacity){
        this.name = name;
        this.room = room;
        this.time = time;
        this.speaker = speaker;
        this.capacity = 2;
    }

    public String getName(){
        return name;
    }

    public String getRoom(){
        return room;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public Speaker getSpeaker(){
        return speaker;
    }

    public int getCapacity(){
        return capacity;
    }

    public boolean addUser(String username){
        if (signedUpUsers.contains(username)){
            return false;
        }
        signedUpUsers.add(username);
        return true;
    }

    public boolean removeUser(String username){
        if (signedUpUsers.contains(username)){
            signedUpUsers.remove(username);
            return true;
        }
        return false;
    }

    public boolean hasUser(String username){
        return this.signedUpUsers.contains(username);
    }

    public int getNumberOfSignedUpUsers() {
        return this.signedUpUsers.size();
    }

    public ArrayList<String> getSignedUpUsers(){
        return signedUpUsers;
    }

}