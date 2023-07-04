package model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;

public class SendPacket implements Serializable {
    @Expose
    private final String userSender;
    @Expose
    private final ArrayList<String> userReceiver;
    @Expose
    private final ObjectType objectType;
    @Expose
    private Chat chat;
    @Expose
    private String string;
    @Expose
    private Kingdom kingdom;
    @Expose
    private String map;
    private final Object object;

    public SendPacket(String userSender, ArrayList<String> userReceiver, ObjectType objectType, Object object) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.objectType = objectType;
        this.object = object;
        setObjectType();
    }

    private void setObjectType() {
        switch (objectType) {
            case Chat: chat = (Chat) object; string = null; kingdom = null; map = null; break;
            case String: string = (String) object; chat = null; kingdom = null; map = null; break;
            case Kingdom: kingdom = (Kingdom) object; chat = null; string = null; map = null; break;
            case Map: map = (String) object; chat = null; string = null; kingdom= null; break;
        }
    }

    public String getUserSender() {
        return userSender;
    }

    public ArrayList<String> getUserReceiver() {
        return userReceiver;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public Chat getChat() {
        return chat;
    }

    public String getString() {
        return string;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public String getMap() {
        return map;
    }
}
