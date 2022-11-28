package fr.ensibs.mvc;

import fr.ensibs.graphic.JavaFXImage;
import fr.ensibs.util.graphic.Snapshot;
import fr.ensibs.util.graphic.SnapshotLayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Definitions of methods executed in reaction to user actions
 *
 * @author Nassim, Grégoire, Andréa
 */
public class ActionsHandler {
    /**
     * component that displays the list
     */
    @FXML
    private ListView<Object> listView;

    /**
     * component that displays a message
     */
    @FXML
    private Label messageLabel;

    /**
     * component that displays a text
     */
    @FXML
    private TextArea textArea;

    /**
     * component that displays images
     */
    @FXML
    private Canvas imageCanvas;

    /**
     * Group composed of a textArea
     */
    @FXML
    private Group textGroup;

    /**
     * Group composed of a Canvas
     */
    @FXML
    private Group imageGroup;

    /**
     * The directory that contains the names to be displayed in the list
     */
    private Directory directory;

    /**
     * Method called after the application has been displayed and the components have
     * been initialized
     */
    public void initialize() {
        directory = new Directory();
        listView.getItems().addAll(directory.getNames());
    }

    /**
     * Reset the list of names to its initial values
     *
     * @param actionEvent the event that triggered this action
     * @post the list contains the directory initial default elements
     */
    @FXML
    public void handleResetAction(ActionEvent actionEvent) {
        directory.reset();
        listView.getItems().clear();
        listView.getItems().addAll(directory.getNames());
    }

    /**
     * Clear the list of names to its initial values
     *
     * @param actionEvent the event that triggered this action
     * @post the list is empty
     */
    @FXML
    public void handleClearAction(ActionEvent actionEvent) {
        directory.clear();
        listView.getItems().clear();
    }

    /**
     * Ajoute un fichier zip à la liste
     *
     * @param actionEvent the event that triggered this action
     * @throws IOException    source introuvable ou invalide
     * @throws ParseException table JSON non-parsable
     * @author Nassim
     */
    @FXML
    public void handleAddFileAction(ActionEvent actionEvent) throws IOException, ParseException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ajoutez une archive");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archive ZIP", "*.zip"));

        File file = fileChooser.showOpenDialog(messageLabel.getScene().getWindow());

        if (file != null) {
            listView.getItems().clear();
            directory.clear();
            directory.addFile(file);

            for (String name : directory.getNames()) {
                listView.getItems().add(name);
            }
        }
    }

    /**
     * Displays the name selected in the list
     *
     * @param mouseEvent the event that triggered this action
     * @post the textArea content matches the selected item in the list
     */
    @FXML
    public void handleListClicked(MouseEvent mouseEvent) throws ParseException, FileNotFoundException {
        Object item = listView.getSelectionModel().getSelectedItem();

        if (item != null) {
            String extension = getExtension(item.toString()); // extension of the file

            if (extension.equals("png") || extension.equals("jpg")) {
                textGroup.setVisible(false);
                imageGroup.setVisible(true);
                imageCanvas.getGraphicsContext2D().clearRect(0, 0, imageCanvas.getWidth(), imageCanvas.getHeight()); // efface la zone d'image avant de la re-remplir

                Image img = ((JavaFXImage) directory.displayContent(item.toString())).getImage();

                List<Double> coord = getCoordinates(img);
                double imgWidth = coord.get(0);
                double imgHeight = coord.get(1);

                imageCanvas.getGraphicsContext2D().drawImage(img, 0, 0, imgWidth, imgHeight);
            } else if (item.toString().startsWith("snapshot")) {
                textGroup.setVisible(false);
                imageGroup.setVisible(true);
                imageCanvas.getGraphicsContext2D().clearRect(0, 0, imageCanvas.getWidth(), imageCanvas.getHeight()); // efface la zone d'image avant de la re-remplir

                Snapshot<JavaFXImage> snapshot = directory.getSnapshot(item.toString());

                for (SnapshotLayer<JavaFXImage> snapshotLayer : snapshot) {
                    Image img = (snapshotLayer.getImage()).getImage();
                    List<Double> coord = getCoordinates(img);
                    double imgWidth = coord.get(0);
                    double imgHeight = coord.get(1);

                    imageCanvas.getGraphicsContext2D().drawImage(img, snapshotLayer.getX(), snapshotLayer.getY(), imgWidth, imgHeight);
                }
            } else {
                textGroup.setVisible(true);
                imageGroup.setVisible(false);
                textArea.setText((String) directory.displayContent(item.toString()));
            }
        } else {
            textArea.setText("Select a non-empty file");
        }
    }

    /**
     * Ask the user for a new name to be added to the list
     *
     * @param actionEvent the event that triggered this action
     * @post the name entered by the user, if any, is displayed in the list
     */
    @FXML
    public void handleAddItem(ActionEvent actionEvent) throws IOException, ParseException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ajouter un fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Texte", "*.txt"),
                new FileChooser.ExtensionFilter("Table JSON", "*.json"),
                new FileChooser.ExtensionFilter("Image JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("Image PNG", "*.png"));
        File file = fileChooser.showOpenDialog(messageLabel.getScene().getWindow());

        if (file != null) {
            listView.getItems().add(file.getName());
            directory.addName(file.getName());
            directory.addFileTo(file);
        }
    }

    /**
     * Sauvegarde de l'archive zip dans le répertoire choisi par l'utilisateur
     *
     * @param actionEvent the event that triggered this action
     * @throws IOException erreurs d'enregistrement du fichier
     */
    public void handleSaveFileAction(ActionEvent actionEvent) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrez une archive");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archive ZIP", "*.zip"));
        File file = fileChooser.showSaveDialog(messageLabel.getScene().getWindow());

        if (file != null) {
            directory.saveArchive(file.getAbsolutePath());
            alertSuccess("Archive saved", "Files were successfully compressed");
        }else{
            alertError("Archive unsaved", "Please select where to save your archive");
        }
    }

    /**
     * Affiche une fenêtre d'alerte informant l'utilisateur sur une action réussie
     *
     * @param title titre de la fenêtre
     * @param desc description de l'alerte
     */
    public void alertSuccess(String title, String desc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(desc);

        alert.showAndWait();
    }

    /**
     * Affiche une fenêtre d'alerte informant l'utilisateur sur une action erronée
     *
     * @param title titre de la fenêtre
     * @param desc description de l'alerte
     */
    public void alertError(String title, String desc) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(desc);

        alert.showAndWait();
    }

    /**
     * Renvoie l'extension d'un fichier
     *
     * @param filename nom du fichier
     * @return (String) extension d'un fichier
     */
    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    /**
     * Permet un affichage des images adapté à la taille de la fenêtre.
     *
     * @param img l'image à redimensionner
     * @return les coordonnées de l'image redimensionnées proportionnellement
     */
    public List<Double> getCoordinates(Image img){
        List<Double> listCoord = new ArrayList<>();

        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();
        double rapport = imgWidth / imgHeight;

        if (imgWidth > imageCanvas.getWidth()) {
            imgWidth = imageCanvas.getWidth();
            imgHeight = rapport * imgWidth;
        }

        if (imgHeight > imageCanvas.getHeight()) {
            imgHeight = imageCanvas.getHeight();
            imgWidth = rapport * imgHeight;
        }

        listCoord.add(imgWidth);
        listCoord.add(imgHeight);
        return listCoord;
    }
}

