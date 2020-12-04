package DatabaseSystem;

import com.mongodb.DB;

import java.util.ArrayList;

public class DatabaseWriter {

    public void save(ArrayList<Savable> savables, DB db) {
        DB database = db;

        for (Savable sv : savables) {
            CollectionManager collectionManager = new CollectionManager(database, sv);
            collectionManager.insertToCollection(sv);
        }

    }

}