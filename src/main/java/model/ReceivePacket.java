package model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ReceivePacket implements Serializable {
    @Expose
    private final String userSender;
    @Expose
    private final Object object;

    public ReceivePacket(String userSender, String object) {
        this.userSender = userSender;
        this.object = object;
    }

    public String getUserSender() {
        return userSender;
    }

    public Object getObject() {
        return object;
    }
}
