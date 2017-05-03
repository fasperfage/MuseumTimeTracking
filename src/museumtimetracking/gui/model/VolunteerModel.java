/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.Volunteer;
import museumtimetracking.bll.VolunteerManager;

/**
 *
 * @author Skovgaard
 */
public class VolunteerModel {

    private final VolunteerManager volunteerMgr;

    private static VolunteerModel instance;

    private final ObservableList<Volunteer> cachedVolunteers;
    private final ObservableList<Volunteer> cachedIdleVolunteers;

    private final List<Volunteer> volunteerFromDB;
    private final List<Volunteer> idleVolunteersFromDB;

    public static VolunteerModel getInstance() {
        if (instance == null) {
            instance = new VolunteerModel();
        }
        return instance;
    }

    public VolunteerModel() {
        // Instantiate volunteerMgr
        volunteerMgr = new VolunteerManager();
        volunteerFromDB = volunteerMgr.getAllVolunteersNotIdle();
        idleVolunteersFromDB = volunteerMgr.getAllIdleVolunteers();
        cachedVolunteers = FXCollections.observableArrayList(volunteerFromDB);
        cachedIdleVolunteers = FXCollections.observableArrayList(idleVolunteersFromDB);
    }

    public ObservableList<Volunteer> getCachedVolunteers() {
        return cachedVolunteers;
    }

    public void setVolunteerDescription(int id, String text) {
        volunteerMgr.setVolunteerDescription(id, text);
    }

    /**
     * Set the volunteer Image
     *
     * @param id
     * @param file
     */
    public void setVolunteerImage(int id, File file) {
        volunteerMgr.setVolunteerImage(id, file);
    }

    /**
     * Updates the volunteer in the DB.
     *
     * @param updatedVolunteer
     */
    public void updateVolunteer(Volunteer updatedVolunteer) {
        volunteerMgr.updateVolunteer(updatedVolunteer);

    }

    /**
     * Deletes the selected volunteer from DB.
     *
     * @param deleteVolunteer
     */
    public void deleteVolunteer(Volunteer deleteVolunteer) {
        volunteerMgr.deleteVolunteer(deleteVolunteer.getID());
        // Removes the volunteer from the listview.
        cachedVolunteers.remove(deleteVolunteer);
    }

    /**
     * Adds the volunteer to DB.
     *
     * @param volunteer
     */
    public void addVolunteer(Volunteer volunteer) {
        volunteerMgr.addVolunteer(volunteer);
        cachedVolunteers.add(volunteer);
    }

    public ObservableList<Volunteer> getCachedIdleVolunteers() {
        return cachedIdleVolunteers;
    }

    /**
     * Set the parsed volunteer as idle
     *
     * @param volunteer
     * @param value
     */
    public void updateIdleVolunteer(Volunteer volunteer, boolean value) {
        if (value) {
            cachedVolunteers.remove(volunteer);
            cachedIdleVolunteers.add(volunteer);
        } else {
            cachedVolunteers.add(volunteer);
            cachedIdleVolunteers.remove(volunteer);
        }
        volunteerMgr.updateVolunteerIdle(volunteer.getID(), value);
    }

}
