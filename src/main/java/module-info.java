module project.group {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires json.simple;
    requires org.apache.commons.codec;
    requires jargs;
    requires passay;
    requires jjwt.api;

    exports view;
    exports controller;
    exports model;

    opens model to com.google.gson, java.json;
    opens view to javafx.fxml;
    opens controller to com.google.gson, java.json;
    opens model.building to com.google.gson;
    opens model.unit to com.google.gson;

}
