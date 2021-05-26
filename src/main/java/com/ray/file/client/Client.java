package client;


import java.net.Socket;

/**
 * Created by zrc on 2021-02-25.
 */
public class Client {
    public static boolean connected = false;

    private final Socket socket;

    public Client(){
        this.socket = connect();
    }

    public Socket getSocket(){
        return socket;
    }
    public static Socket connect() {
        String url = "http://localhost";

        try {
            return new Socket(url,9099);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
