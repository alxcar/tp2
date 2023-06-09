package client;

import client.models.Client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe View, affiche une application d'inscription de cours avec laquelle le user interagie.
 */
public class View extends Application {
    private int width = 750;
    private int height = 500;
    private int padding = 20;

    private Stage stage;
    private HBox mainPlane;

    protected TableView<Course> table;
    protected ChoiceBox sessionBox;
    private Controller controller;

    private VBox leftPlane;

    private VBox rightPlane;
    private TextField matricule;
    private TextField email;
    private TextField prenom;
    private TextField nom;

    /**
     * Constructeur de la classe View.
     */
    public View() {
        controller = new Controller(this);
    }

    /**
     * Getter pour le TextField matricule.
     * @return Le TextField matricule.
     */
    public TextField getMatricule() {
        return matricule;
    }

    /**
     * Getter pour le TextField email.
     * @return Le TextField email.
     */
    public TextField getEmail() {
        return email;
    }

    /**
     * Creer et définie l'interface graphique
     * @param stage Fenêtre de l'application
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Inscription UdeM");
        initLeftPane();
        initRightPane();
        mainPlane = new HBox();


        mainPlane.getChildren().addAll(leftPlane, rightPlane);
        stage.setScene(new Scene(mainPlane, width, height));
        stage.setResizable(false);
        stage.show();
    }

    private void initLeftPane() {
        leftPlane = new VBox();

        leftPlane.setPadding(new Insets(padding, padding, padding, padding));
        leftPlane.setPrefSize(width/2, height);
        leftPlane.setAlignment(Pos.CENTER);
        Label listeLabel = new Label("Liste des cours");
        listeLabel.setFont(new Font(25));

        table = new TableView();
        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> coursCol = new TableColumn("Cours");
        coursCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(codeCol, coursCol);
        // Faire fit les columns sur la longueur du tab
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox chargerPlane = new HBox();
        chargerPlane.setSpacing(padding*3);
        chargerPlane.setAlignment(Pos.CENTER);

        sessionBox = new ChoiceBox();
        sessionBox.getItems().add("Automne");
        sessionBox.getItems().add("Hiver");
        sessionBox.getItems().add("Ete");

        Button charger = new Button("charger");

        chargerPlane.getChildren().addAll(sessionBox, charger);

        leftPlane.getChildren().addAll(listeLabel, table, chargerPlane);
        charger.setOnAction( e -> {
            controller.chargerCours();
        });
    }

    private void initRightPane() {
        rightPlane = new VBox();
        rightPlane.setPadding(new Insets(padding, padding, padding, padding));
        rightPlane.setPrefSize(width/2, height);
        rightPlane.setAlignment(Pos.CENTER);

        Label labelIns = new Label("Formulaire d'inscription");
        labelIns.setFont(new Font(25));
        labelIns.setPadding(new Insets(0, 0, padding, 0));

        GridPane formulaire = new GridPane();

        formulaire.add(new Label("Prénom"), 0, 0);
        prenom = new TextField();
        formulaire.add(prenom, 1, 0);
        formulaire.add(new Label("Nom"), 0, 1);
        nom = new TextField();
        formulaire.add(nom, 1, 1);
        formulaire.add(new Label("Email"), 0, 2);
        email = new TextField();
        formulaire.add(email, 1, 2);
        formulaire.add(new Label("Matricule"), 0, 3);
        matricule = new TextField();
        formulaire.add(matricule, 1, 3);
        Button sendForm = new Button("envoyer");
        sendForm.setAlignment(Pos.BOTTOM_RIGHT);

        formulaire.add(sendForm, 1, 4);
        formulaire.setHgap(width/30);
        formulaire.setVgap(height/35);
        Pane region = new Pane();
        region.setMinHeight(height/2);
        rightPlane.getChildren().addAll(labelIns, formulaire, region);

        sendForm.setOnAction(e -> {
            controller.sendForm(prenom.getText(), nom.getText(), email.getText(), matricule.getText());
        });
    }

    /**
     * Change la couleur de la bordure d'une boite de texte en rouge.
     * @param node Boite de texte à modifier.
     */
    public void setRedBorder(Control node) {
        node.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    /**
     * Supprime toutes modifications faites à la bordure d'une boite de texte.
     * @param node Boite de texte à restorer.
     */
    public void removeBorder(Control node) {
        node.setBorder(new Border(new BorderStroke(new Color(0,0,0,0), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }

    /**
     * Efface le texte écrit dans les boites de textes prenom, nom, email, matricule.
     */
    public void clearForm() {
        prenom.clear();
        nom.clear();
        email.clear();
        matricule.clear();
    }

    /**
     * Ouvre une fenêtre d'erreur qui indique les champs invalides du formulaire d'inscription.
     * @param errorList List de messages d'erreurs des champs invalides.
     */
    public void throwErrorAlert(ArrayList<String> errorList) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Le formulaire est invalide\n" + String.join("\n", errorList));
        alert.showAndWait();
    }

    /**
     * Ouvre une fenêtre de confirmation d'inscription.
     * @param prenom Prenom entré par le user.
     * @param nom Nom entré par le user.
     * @param code Code du cours auquel le user s'est inscrit.
     */
    public void successAlert(String prenom, String nom, String code) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Message");
        alert.setContentText("Félicitations! " + nom + " " + prenom + " est inscrit(e) avec succès au cours " + code);
        alert.showAndWait();
    }
}
