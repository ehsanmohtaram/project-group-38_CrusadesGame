package model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Chat implements Serializable {
    @Expose
    private final String userSender;
    @Expose
    private final String userReceiver;
    @Expose
    private final String messageText;
    @Expose
    private final ChatType chatType;
    @Expose
    private final Integer hours;
    @Expose
    private final Integer seconds;

    public Chat(String userSender, String userReceiver, String messageText, ChatType chatType, Integer hours, Integer seconds) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.messageText = messageText;
        this.chatType = chatType;
        this.hours = hours;
        this.seconds = seconds;
    }

    public String getUserSender() {
        return userSender;
    }

    public String getUserReceiver() {
        return userReceiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public Integer getHours() {
        return hours;
    }

    public Integer getSeconds() {
        return seconds;
    }
}
