module org.example.assigment {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.assigment to javafx.fxml;
    exports org.example.assigment;
}