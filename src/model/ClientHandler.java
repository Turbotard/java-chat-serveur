package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe ClientHandler qui gère les connexions entrantes des clients.
 */
public class ClientHandler implements Runnable {
    /* --------------------- Envoie les messages aux clients -------------------- */
    private Socket socket;
    private PrintWriter printWriter;
    // Lis les messages reçus du client
    private BufferedReader bufferedReader;
    // Instance du serveur qui gère les clients
    private ChatServer chatServer;
    // pseudo du client
    private String username;

    /**
     * Constructeur de la classe ClientHandler.
     * 
     * @param chatServer L'instance du serveur qui gère les clients
     * @param socket     Le socket du client
     */
    public ClientHandler(ChatServer chatServer, Socket socket) {
        this.chatServer = chatServer;
        this.socket = socket;

        try {
            // Initialise le PrintWriter avec le flux de sortie du socket
            printWriter = new PrintWriter(socket.getOutputStream());
            // Initialise le BufferedReader avec le flux d'entrée du socket
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
            e.printStackTrace();
        }
    }

    /**
     * Récupère le pseudo du client.
     * 
     * @return Le pseudo du client
     */
    public String getUsername() {
        return username;
    }

    /**
     * Envoie un message au client.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message) {
        // Envoie le message au client
        printWriter.println(message);
        /*
         * Lorsque tu envoies des données avec PrintWriter, elles sont parfois mises en
         * mémoire tampon pour améliorer les performances d'écriture. Cela signifie que
         * les données ne sont pas toujours immédiatement envoyées au destinataire.
         * 
         * La méthode flush() force l'envoi immédiat de toutes les données mises en
         * mémoire tampon.
         */
        printWriter.flush();
    }

    /**
     * Ferme les flux et le socket.
     */
    public void disconnect() {
        try {

            // Ferme le flux de sortie du socket
            if (printWriter != null) {
                printWriter.close();
            }

            // Ferme le flux d'entrée du socket
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            // Ferme le socket
            if (socket != null) {
                socket.close();
            }

            // Prévient les autres clients que le client s'est déconnecté
            chatServer.broadcastLeavingClient(this);

            // Supprime le ClientHandler de la liste des clients connectés
            chatServer.removeClient(this);

        } catch (IOException e) {
            // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
            e.printStackTrace();
        }
    }

    /**
     * Exécute le code de gestion des messages dans un nouveau thread.
     * Cette méthode est appelée automatiquement lorsqu'un nouvel objet Thread est
     * démarré avec une instance de ClientHandler.
     * 
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            // Lecture du pseudo envoyé par le client
            username = bufferedReader.readLine();

            // Vérifie si le pseudo est déjà utilisé
            if (chatServer.isUsernameTaken(username)) {
                sendMessage("Le pseudo " + username + " est déjà utilisé !");
                disconnect();
                return;
            } else {
                // Ajoute le pseudo à la liste des pseudos utilisés
                chatServer.addUsername(username);
                // Prévient les autres clients que le nouveau client s'est connecté
                chatServer.broadcastNewClient(this);
            }
        } catch (IOException e) {
            // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
            e.printStackTrace();
        }

        // Le code à exécuter dans un nouveau thread sera placé ici
        try {
            while (true) {
                // Lit le message envoyé par le client
                String message = bufferedReader.readLine();

                // Si le message est vide, celui-ci n'est pas partagé
                if (message == null || message.isEmpty()) {
                    continue;
                }

                // Affiche le message reçu
                System.out.println("Message reçu de " + username + " : " + message);

                // Si le message est /quit, le client est déconnecté
                if (message.equals("/quit")) {
                    break;
                }

                // Envoie le message à tous les clients connectés
                chatServer.broadcastMessage(this, message);

            }
        } catch (IOException e) {
            // Affiche la pile d'appels des méthodes qui ont conduit à l'exception
            // e.printStackTrace();
            System.out.println("Le client " + username + " s'est déconnecté.");

        } finally {
            disconnect();
        }
    }
}
