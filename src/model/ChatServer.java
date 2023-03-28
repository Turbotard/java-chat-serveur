package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe ChatServer représentant le serveur de chat.
 */
public class ChatServer {
    private static ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

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
     * Ajoute un client à la liste des clients connectés.
     *
     * @param client Le client à ajouter
     */
    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    /**
     * Supprime un client de la liste des clients connectés.
     *
     * @param client Le client à supprimer
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcastMessage(String message) {
        // Envoie le message à tous les clients connectés
        for (ClientHandler client : clients) {
            client.sendMessage(message);
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

                // Crée un nouveau ClientHandler pour gérer les messages du client
                ClientHandler clientHandler = new ClientHandler(this, clientSocket);

                // Ajoute le ClientHandler à la liste des clients connectés
                addClient(clientHandler);

                // Crée un nouveau thread pour gérer les messages du client
                Thread clientThread = new Thread(clientHandler);

                // Démarrer le thread
                clientThread.start();

            } catch (IOException e) {
                // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
                e.printStackTrace();
            }
        }
    }
}