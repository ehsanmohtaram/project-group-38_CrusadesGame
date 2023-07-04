package model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class ReceivePacket implements Serializable {
    @Expose
    private final String userSender;
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

    public ReceivePacket(String userSender, ObjectType objectType, Object object) {
        this.userSender = userSender;
        this.object = object;
        this.objectType = objectType;
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
