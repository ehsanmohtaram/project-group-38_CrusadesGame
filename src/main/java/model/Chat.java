package model;

public class Chat {
    private final String userSender;
    private final String userReceiver;
    private final String messageText;
    private final ChatType chatType;
    private final Integer hours;
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
