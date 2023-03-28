package model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    /* --------------------- Envoie les messages aux clients -------------------- */
    Socket socket;
    PrintWriter printWriter;
    // Lis les messages reçus du client
    BufferedReader bufferedReader;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }
}
