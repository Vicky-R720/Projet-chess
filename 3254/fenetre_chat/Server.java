import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    // Une liste pour garder une trace des clients connectés
    private static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = 12345; // Port d'écoute
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré, en attente de connexions sur le port " + port);

            // Accepter plusieurs clients dans des threads séparés
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Un nouveau client est connecté : " + clientSocket.getInetAddress());

                // Créer un nouveau thread pour gérer ce client
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            System.err.println("Erreur lors du démarrage du serveur: " + e.getMessage());
        }
    }

    // Classe qui gère chaque client connecté
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Créer les flux pour communiquer avec le client
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Demander au client son nom
                out.println("Veuillez entrer votre nom : ");
                clientName = in.readLine();
                synchronized (clients) {
                    clients.put(clientName, out);
                }

                System.out.println(clientName + " a rejoint le serveur.");

                // Lire les messages du client et les transmettre aux autres clients ou au destinataire spécifique
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message reçu de " + clientName + ": " + message);

                    // Si le message contient un ":", il est supposé être destiné à un autre client
                    if (message.contains(":")) {
                        String[] parts = message.split(":", 2);
                        String recipientName = parts[0].trim();
                        String actualMessage = parts[1].trim();

                        // Vérifier si le destinataire existe
                        PrintWriter recipientOut = clients.get(recipientName);
                        if (recipientOut != null) {
                            recipientOut.println(clientName + " vous dit : " + actualMessage);
                        } else {
                            out.println("Erreur : Le client " + recipientName + " n'est pas connecté.");
                        }
                    } else {
                        out.println("Message mal formé. Utilisez 'DESTINATAIRE: message'.");
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur de communication avec le client " + clientName);
            } finally {
                try {
                    socket.close();
                    synchronized (clients) {
                        clients.remove(clientName);
                    }
                    System.out.println(clientName + " a quitté le serveur.");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion pour " + clientName);
                }
            }
        }
    }
}
