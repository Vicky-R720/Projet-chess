import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private static JTextArea messageArea;  // Zone d'affichage des messages
    private static JTextField recipientField;  // Champ de texte pour le destinataire
    private static JTextField messageField;  // Champ de texte pour le message
    private static PrintWriter output;
    private static BufferedReader input;
    private static Socket socket;
    private static String name;

    public static void main(String[] args) {
        String host = "172.50.107.152";  // L'adresse du serveur
        int port = 12345;  // Le même port que celui du serveur

        // Création de l'interface graphique
        JFrame frame = new JFrame("Client de Messagerie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Création des éléments de l'interface
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        recipientField = new JTextField(20);  // Champ de texte pour le destinataire
        messageField = new JTextField(20);    // Champ de texte pour le message

        // Ajouter des labels pour les champs
        JLabel recipientLabel = new JLabel("Destinataire:");
        JLabel messageLabel = new JLabel("Message:");

        // Créer le bouton d'envoi
        JButton sendButton = new JButton("Envoyer");
        sendButton.addActionListener(new SendButtonListener());

        // Créer un panel pour l'envoi du message (champ destinataire et message)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));
        inputPanel.add(recipientLabel);
        inputPanel.add(recipientField);
        inputPanel.add(messageLabel);
        inputPanel.add(messageField);

        // Créer un panel pour le bouton d'envoi
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);

        // Ajouter tout au frame
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);  // Afficher les messages
        frame.add(inputPanel, BorderLayout.NORTH);   // Afficher les champs destinataire et message
        frame.add(buttonPanel, BorderLayout.SOUTH);  // Ajouter le bouton envoyer

        frame.setVisible(true);

        // Connexion au serveur
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // Demander au client son nom
            String prompt = input.readLine();  // Lire "Veuillez entrer votre nom :"
            String response = JOptionPane.showInputDialog(frame, prompt);
            name = response;
            output.println(name);

            // Créer un thread pour lire les messages entrants du serveur
            Thread readThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        messageArea.append(serverMessage + "\n");  // Afficher les messages du serveur dans le JTextArea
                    }
                } catch (IOException e) {
                    System.err.println("Erreur de lecture du serveur.");
                }
            });
            readThread.start();

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur: " + e.getMessage());
        }
    }

    // Action à effectuer lors de l'appui sur le bouton "Envoyer"
    private static class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String recipient = recipientField.getText();
            String message = messageField.getText();

            if (recipient.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un destinataire et un message.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Format du message à envoyer au serveur
            output.println(recipient + ": " + message);
            messageField.setText("");  // Vider le champ du message après envoi
        }
    }
}
