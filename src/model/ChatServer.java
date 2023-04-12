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
    private static ArrayList<String> usernames = new ArrayList<String>();

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
     * Vérifie si un pseudo est déjà pris par un autre client.
     * 
     * @param username Le pseudo du client
     * @return true si le pseudo est déjà pris, false sinon
     */
    public boolean isUsernameTaken(String username) {
        return usernames.contains(username);
    }

    /**
     * Ajoute un pseudo à la liste des pseudos.
     * 
     * @param username Le pseudo à ajouter
     */
    public void addUsername(String username) {
        usernames.add(username);
    }

    /**
     * Supprime un client de la liste des clients connectés et son pseudo de la
     * liste des pseudos.
     *
     * @param client Le client à supprimer
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        usernames.remove(client.getUsername());
    }

    /**
     * Envoie un message à tous les clients connectés.
     *
     * @param senderUsername Le pseudo de l'expéditeur
     * @param message        Le message à envoyer
     */
    public void broadcastMessage(ClientHandler sender, String message) {
        // Envoie le message à tous les clients connectés
        for (ClientHandler client : clients) {

            if (sender != client) {
                String senderUsername = sender.getUsername();
                client.sendMessage(senderUsername + " : " + message);
            } else {
                client.sendMessage("Vous : " + message);
            }
        }
    }

    /**
     * Envoie un message à tous les clients connectés pour indiquer qu'un nouveau
     * client s'est connecté.
     * 
     * @param clientName Le pseudo du nouveau client
     */
    public void broadcastNewClient(ClientHandler newClient) {
        // Envoie le message à tous les clients connectés
        for (ClientHandler client : clients) {
            if (client != newClient) {
                String newClientUsername = newClient.getUsername();
                client.sendMessage(newClientUsername + " s'est connecté.");
                System.out.println("Usernames : " + usernames);
            }
        }
    }

    /**
     * Envoie un message à tous les clients connectés pour indiquer qu'un client
     * s'est
     * déconnecté.
     * 
     * @param clientName Le pseudo du client qui s'est déconnecté
     */
    public void broadcastClientDisconnected(ClientHandler client) {
        // Envoie le message à tous les clients connectés
        for (ClientHandler c : clients) {
            if (c != client) {
                String clientUsername = client.getUsername();
                c.sendMessage(clientUsername + " s'est déconnecté.");
            }
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

    /**
     * Méthode pour arrêter le serveur et déconnecter tous les clients connectés.
     */
    public void stopServer() {
        try {
            // Fermez le ServerSocket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            // Déconnectez tous les clients connectés
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.disconnect();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}