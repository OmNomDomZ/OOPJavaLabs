package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.model.ChatModel;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.model.client.ClientHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatView implements Observer {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private ChatModel chatModel;
    private String nickname;
    private ClientHandler clientHandler;

    public ChatView(ChatModel chatModel, String nickname, ClientHandler clientHandler) {
        this.chatModel = chatModel;
        this.nickname = nickname;
        this.clientHandler = clientHandler;
        chatModel.addObserver(this);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        textField = new JTextField(25);
        sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.add(textField);
        panel.add(sendButton);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.trim().isEmpty()) {
            clientHandler.sendMessage(message);
            textField.setText("");
        }
    }

    @Override
    public void update(String message) {
        textArea.append(message + "\n");
    }
}
