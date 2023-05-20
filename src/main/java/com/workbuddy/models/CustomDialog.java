package com.workbuddy.models;

import com.intellij.openapi.ui.DialogWrapper;
import com.workbuddy.listeners.ChatGPTListener;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends DialogWrapper {
    private List<JTextArea> textFields;

    private JPanel mainPanel;

    private String selectedCode;

    public CustomDialog(String selectedCode) {
        super(true); // Pass 'true' to indicate that the dialog is modal
        setTitle("Work Buddy"); // Set the title of the dialog
        this.textFields = new ArrayList<>();
        init(); // Initialize the dialog
        this.selectedCode = selectedCode;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JButton ask = new JButton("Chat");
        ask.addActionListener(new ChatGPTListener(this));
        mainPanel.add(ask);

        addTextField("");
        mainPanel.setPreferredSize(new Dimension(800, 400)); // Set the desired width and height

        return mainPanel;
    }

    public void addTextField(String text) {
        if(!textFields.isEmpty()){
            textFields.get(textFields.size()-1).setEditable(false);
        }
        JTextArea textField = new JTextArea();
        textField.setText(text);
        textField.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textField);
        mainPanel.add(scrollPane);
        textFields.add(textField);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public String getSelectedCode() {
        return selectedCode;
    }

    public List<JTextArea> getTextFields() {
        return textFields;
    }
}