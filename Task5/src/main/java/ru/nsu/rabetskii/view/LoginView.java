package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.model.ChatModel;
import ru.nsu.rabetskii.model.client.ClientHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView {
    private JFrame frame;
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private ChatModel chatModel;

    public LoginView(ChatModel chatModel) {
        this.chatModel = chatModel;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        nicknameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nickname:"));
        panel.add(nicknameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nickname = nicknameField.getText();
                    String password = new String(passwordField.getPassword());
                    ClientHandler clientHandler = new ClientHandler("localhost", 8080, chatModel, nickname, password);
                    frame.dispose();
                    new ChatView(chatModel, nickname, clientHandler);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
                }
            }
        });
    }
}
