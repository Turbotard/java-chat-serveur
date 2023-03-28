package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe ChatServer représentant le serveur de chat.
 */
public class ChatServer {
    private ServerSocket serverSocket;

    /**
     * Constructeur de la classe ChatServer.
     *
     * @param port Le numéro de port sur lequel le serveur écoutera les connexions
     *             entrantes.
     */
    public ChatServer(int port) {
        try {
            // Initialisation du serveur avec le numéro de port spécifié
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour démarrer le serveur et accepter les connexions entrantes des
     * clients.
     */
    public void startServer() {
        System.out.println("Le serveur est en attente de connexions...");
        while (true) {
            try {
                // Attend une connexion entrante et crée un objet Socket pour représenter la
                // connexion établie avec le client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Un nouveau client s'est connecté : " + clientSocket.getInetAddress());
            } catch (IOException e) {
                // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
                e.printStackTrace();
            }
        }
    }
}