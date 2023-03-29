import model.ChatServer;

/**
 * Classe App qui sert de point d'entrée pour l'application serveur de chat.
 */
public class App {
    static final int port = 1234;

    /**
     * Méthode main qui sert de point d'entrée pour l'application serveur de chat.
     * 
     * @param args Les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        // Création d'une instance de ChatServer avec le numéro de port spécifié
        ChatServer chatServer = new ChatServer(port);

        // Ajoute un événement de fermeture du serveur lors de l'arrêt de l'application
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                chatServer.stopServer();
            }
        });

        // Démarrage du serveur de chat
        chatServer.startServer();
    }
}
