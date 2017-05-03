/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.volunteerInfo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import museumtimetracking.be.Volunteer;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class VolunteerInfoViewController implements Initializable {

    @FXML
    private Button btnEdit;
    @FXML
    private TextArea txtVolunteerInfo;

    private Volunteer currentVolunteer;

    private Stage primStage;

    private final VolunteerModel volunteerModel;

    public VolunteerInfoViewController() {
        volunteerModel = VolunteerModel.getInstance();
    }

    @FXML
    private void handleBack() {
        primStage = (Stage) btnEdit.getScene().getWindow();
        primStage.close();
    }

    @FXML
    private void handleEditVolunteerInfo() {
        primStage = (Stage) btnEdit.getScene().getWindow();
        if (btnEdit.getText().equalsIgnoreCase("rediger")) {
            btnEdit.setText("Gem");
            txtVolunteerInfo.setDisable(false);
        } else {
            btnEdit.setText("Rediger");
            txtVolunteerInfo.setDisable(true);
            currentVolunteer.setDescription(txtVolunteerInfo.getText());
            volunteerModel.setVolunteerDescription(currentVolunteer.getID(), txtVolunteerInfo.getText());
            volunteerModel.updateIdleVolunteer(currentVolunteer, true);
            primStage.close();
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtVolunteerInfo.setDisable(true);
    }

    /**
     * Set the current volunteer
     *
     * @param volunteer
     */
    public void setCurrentVolunteer(Volunteer volunteer) {
        currentVolunteer = volunteer;
        txtVolunteerInfo.setText(currentVolunteer.getDescription());
    }
}