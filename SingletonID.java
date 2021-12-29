import java.util.UUID;

public class SingletonID {
    
    private static SingletonID instance = new SingletonID();

    private SingletonID() {}

    public static SingletonID getInstance(){
        return instance;
    }

    public String getID() {
        return UUID.randomUUID().toString();
    }
}
