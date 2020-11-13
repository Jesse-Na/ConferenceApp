import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * (please describe)
 *
 * @author
 * @version %I%, %G%
 */
public class ReadUsers {
    private String filepath;

    /**
     * (please describe)
     *
     * @param filepath      (please describe)
     */
    public ReadUsers(String filepath) {
        // Reads a txt/ser file then stores it
        this.filepath = filepath;

    }

    /**
     * (please describe)
     *
     * @return      (please describe)
     */
    public ArrayList<User> read() {
        ArrayList<User> users = new ArrayList<>();
        boolean isEmpty = false;
        try {
            FileInputStream fileIn = new FileInputStream(this.filepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while (!isEmpty) {
                User user = (User) in.readObject();
                if (user == null) {
                    isEmpty = true;
                } else {
                    users.add(user);
                }
            }
            in.close();
            fileIn.close();
        } catch (IOException e) {
            System.out.println("Error reading users from file");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
        return users;
    }
}
