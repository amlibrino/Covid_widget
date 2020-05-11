package org.hmid.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hmid.datafetch.DataProviderService;
import org.hmid.datafetch.model.CovidDataModel;

public class Launch extends Application {
    //déclaration position de départ(les coordonnees)
    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // le stage fantome
        primaryStage.initStyle(StageStyle.UTILITY);//le rendreen fantome
        primaryStage.setOpacity(0);// le rendre transparent
        primaryStage.show();

        // le stage sans bordure contenu dans le premier
        Stage secondaryStage = new Stage();
        secondaryStage.initStyle(StageStyle.UNDECORATED);//pour retirer les bordure
        secondaryStage.initOwner(primaryStage);

        // On reprend le même code que précédemment en changeant juste le nom du stage
        Parent root = FXMLLoader.load(getClass().getResource("/org/hmid/gui/widget/widget.fxml"));
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        secondaryStage.show();

        //on aligne en haut à droite (definir l'ecran)
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        secondaryStage.setX(visualBounds.getMaxX() - 25 - scene.getWidth());
        secondaryStage.setY(visualBounds.getMinY() + 25);

        //ajout de la méthode de translation (au clic)actionneur
        scene.setOnMousePressed(event -> {
            xOffset = secondaryStage.getX() - event.getScreenX();
            yOffset = secondaryStage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            secondaryStage.setX(event.getScreenX() + xOffset);
            secondaryStage.setY(event.getScreenY() + yOffset);
        });

    }
    // lancherr l'app
    public static void main(String[] args) {
        launch(args);
    }
}
