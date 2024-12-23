package Windowing;

import java.util.*;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Listening.*;
import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Win extends JFrame {
    private int height = 800;
    private int width = 800;
    private Draw draw;
    private Interpreter interpreter;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Point move;
    private boolean isTurn = false; // Indique si c'est le tour du joueur
    private String username;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Getter et setter pour `Interpreter` et autres attributs
    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    // Constructeur de la fenêtre `Win`
    public Win() {
        if (!showLoginForm()) {
            System.exit(0); // Quitter si l'utilisateur annule le formulaire
        }

        Properties config = new Properties();
        // Initialisation du réseau
        try {

            FileInputStream configfile = new FileInputStream("conf.config");
            config.load(configfile);
            configfile.close();

            String host = config.getProperty("server.host", "localhost");
            int port = Integer.parseInt(config.getProperty("server.port", "8080"));

            socket = new Socket(host, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // // Envoyer le nom d'utilisateur au serveur
            // out.writeObject(username);
            // out.flush();

            // // Recevoir message de bienvenue
            // String message = (String) in.readObject();
            // System.out.println(message);

            // Lancer le thread de réception
            new Thread(this::listen).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle(username + ": " + color);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.draw = new Draw(this); // Créer l'objet Draw pour afficher le plateau
        this.draw.addMouseListener(new Listener(this)); // Ajouter le Listener pour interagir avec le plateau
        interpreter = new Interpreter(this); // Créer l'interpréteur (peut-être pour gérer les coups)
        this.add(this.draw);
        setVisible(true);
    }

    private boolean showLoginForm() {
        String[] colors = new String[] { "white", "black" };
        JComboBox<String> select = new JComboBox<>(colors);
        JPanel panel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour plus de contrôle
        JTextField usernameField = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Ajouter de l'espace autour des composants

        // Ajouter le champ nom d'utilisateur
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom d'utilisateur :"), gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Ajouter le label "Choisissez votre couleur"
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Choisissez votre couleur :"), gbc);

        // Ajouter le JComboBox
        gbc.gridx = 1;
        panel.add(select, gbc);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            username = usernameField.getText().trim();
            color = (String) select.getSelectedItem();

            // Validation du nom d'utilisateur
            if (username.isEmpty()) {
                showError("Le nom d'utilisateur ne peut pas être vide.");
                return false;
            } else if (username.contains(" ")) {
                showError("Le nom d'utilisateur ne peut pas contenir d'espaces.");
                return false;
            } else {
                return true; // Connexion réussie
            }
        }
        return false; // L'utilisateur a annulé
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void listen() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof String) {
                    String message = (String) obj;
                    System.out.println(message);
                    if (message.equals("Votre tour")) {
                        isTurn = true;
                    }
                } else if (obj instanceof Point) {
                    Point opponentMove1 = (Point) obj;
                    Point opponentMove2 = (Point) obj;
                    interpreter.execute1(opponentMove1.x, opponentMove1.y);
                    interpreter.execute2(opponentMove2.x, opponentMove2.y);
                    System.out.println("Adversaire a joué : " + opponentMove1 + opponentMove2);
                    // Mettre à jour l'affichage (par exemple)
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setMove(Point move1, Point move2) {
        if (!isTurn) {
            System.out.println("Ce n'est pas votre tour !");
            return;
        }

        try {
            if (move2 != null) {
                out.writeObject(move1);
                out.writeObject(move2);
                out.flush();
                System.out.println("Mouvement envoyé : " + move1 + move2);
                isTurn = false; // Joueur ne peut plus jouer jusqu'à son prochain tour
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
