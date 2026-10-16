module org.example.pratica4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pratica4 to javafx.fxml;
    exports org.example.pratica4;
}