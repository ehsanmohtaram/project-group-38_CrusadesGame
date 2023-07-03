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
    private final Object object;

    public SendPacket(String userSender, ArrayList<String> userReceiver, String object) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.object = object;
    }

    public String getUserSender() {
        return userSender;
    }

    public ArrayList<String> getUserReceiver() {
        return userReceiver;
    }

    public Object getObject() {
        return object;
    }
}
