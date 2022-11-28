package fr.ensibs.mvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * A JavaFX application that displays a directory and is intended to illustrate the MVC architecture
 *
 * @author Pascale Launay
 */
public class Main extends Application
{
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     * @throws Exception if something goes wrong
     */

    //public static void main(String[] args){launch(args);}
    @Override
    public void start(Stage stage) throws Exception
    {
        // init the scene from the FXML description
        URL resource = Main.class.getResource("main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Scene scene = new Scene(fxmlLoader.load());

        // init the stage
        stage.setScene(scene);
        stage.setTitle("Sprites");

        // display the frame
        stage.show();
    }
}
