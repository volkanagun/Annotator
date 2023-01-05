module edu.btu.annotating {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires java.logging;
    requires simple.xml;
    requires zemberek.morphology;
    requires zemberek.tokenization;



    opens edu.btu.annotating to javafx.fxml;
    exports edu.btu.annotating;
}