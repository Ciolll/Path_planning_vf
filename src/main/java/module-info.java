module com.feron{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;
    requires javafx.swing;

    opens com.feron to javafx.fxml;
    
    exports com.feron;
   
    exports com.feron.Visualisation;
    opens com.feron.Visualisation to javafx.fxml;
}