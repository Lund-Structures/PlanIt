module com.student.appointment_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;

    exports com.student.appointment_scheduler;
    exports controller;
    exports model;

    opens com.student.appointment_scheduler to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;

}