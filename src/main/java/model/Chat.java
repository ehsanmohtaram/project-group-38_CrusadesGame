package model;

import com.google.gson.annotations.Expose;
import controller.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

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
    private final Integer groupId;
    @Expose
    private final String groupName;
    @Expose
    private final ArrayList<String> groupMember;
    @Expose
    private final Integer hours;
    @Expose
    private final Integer seconds;

    public Chat(String userSender, String userReceiver, String messageText, ChatType chatType, Integer groupId, String groupName, ArrayList<String> groupMember,Integer hours, Integer seconds) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.messageText = messageText;
        this.chatType = chatType;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupMember = groupMember;
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

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<String> getGroupMember() {
        return groupMember;
    }

    public static int getIdForGroup() {
        ArrayList<Integer> id = new ArrayList<>();
        for (Chat chat : Controller.currentUser.getMyChats())
            if (chat.getChatType().equals(ChatType.GROUP)) id.add(chat.groupId);
        if (id.size() == 0) return 0;
        else return Collections.max(id) + 1;
    }
}
