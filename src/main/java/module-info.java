module com.microproject.microproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.microproject.microproject to javafx.fxml;
    exports com.microproject.microproject;
    exports com.microproject.microproject.model;
    opens com.microproject.microproject.model to javafx.fxml;
    exports com.microproject.microproject.simulator;
    opens com.microproject.microproject.simulator to javafx.fxml;
    exports com.microproject.microproject.util;
    opens com.microproject.microproject.util to javafx.fxml;
}