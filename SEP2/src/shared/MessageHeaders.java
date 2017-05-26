package shared;

/**
 * Created by Nicolai Onov on 5/10/2017.
 * This utility class contains String constants for specifying purpose of each update message sent from server to client.
 */
public interface MessageHeaders {
    String CREATE = "CREATE";
    String DELETE = "DELETE";
    String UPDATE = "UPDATE";
    String MEMBER_CREATE = "MEMBER_CREATE";
    String MEMBER_DELETE = "MEMBER_CREATE";
    String MEMBER_UPDATE = "MEMBER_CREATE";
    String CHAT_MESSAGE = "NEW MESSAGE";
}
