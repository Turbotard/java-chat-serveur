import model.ChatServer;

/**
 * Classe App qui sert de point d'entrée pour l'application serveur de chat.
 */
public class App {
    static final int port = 5000;

    public static void main(String[] args) {
        // Création d'une instance de ChatServer avec le numéro de port spécifié
        ChatServer chatServer = new ChatServer(port);

        // Démarrage du serveur de chat
        chatServer.startServer();
    }
}
