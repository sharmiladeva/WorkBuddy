package com.workbuddy.listeners;

import com.intellij.openapi.ui.DialogWrapper;
import com.workbuddy.clients.HttpClient;
import com.workbuddy.models.ChatGPTRequestBody;
import com.workbuddy.models.CustomDialog;
import com.workbuddy.models.Message;
import com.workbuddy.util.Constants;
import io.netty.handler.codec.http.HttpMethod;
import net.minidev.json.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTListener implements ActionListener {
    public final CustomDialog dialog;

    public final HttpClient httpClient;
    public ChatGPTListener(CustomDialog dialog) {
        this.dialog=dialog;
        this.httpClient = new HttpClient();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ChatGPTRequestBody request = getRequestBody(dialog.getSelectedCode());
            String response = httpClient.makeRequestToChatGPT(Constants.CHATGPT_API, HttpMethod.POST.toString(), request);
            dialog.addTextField(response);
            dialog.addTextField("");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private ChatGPTRequestBody getRequestBody(String selectedCode) {
        // Prepare the request body
        String rolePlay = "I want you to act as a highly intelligent AI chatbot that has deep understanding of any coding " +
                "language and its API documentations. I will provide you with a code block and your role is " +
                " provide a comprehensive answer to any questions or requests that I will ask about the code" +
                " block. Please answer in a crisp and short way." +
                "It is very important that you provide concise answers and answer in markdown format.";

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", rolePlay + "\n Code : \n"+ selectedCode));
        messages.add(new Message("user", "Who are you?"));
        messages.add(new Message("assistant", "I am an intelligent and helpful AI chatbot."));
        for(int i=Math.max(0,dialog.getTextFields().size()-9);i<dialog.getTextFields().size(); i++) {
            if(i%2==0){
                messages.add(new Message("user", dialog.getTextFields().get(i).getText()));
            }
            else {
                messages.add(new Message("assistant", dialog.getTextFields().get(i).getText()));
            }
        }

        ChatGPTRequestBody requestBody = new ChatGPTRequestBody();
        requestBody.setModel("gpt-3.5-turbo");
        requestBody.setMessages(messages);
        requestBody.setTemperature(0);
        return requestBody;
    }
}
