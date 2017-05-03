/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.ELanguage;

/**
 *
 * @author Skovgaard
 */
public class VolunteerDAO extends APersonDAO {

    private DBConnectionManager cm;

    private static VolunteerDAO instance;

    public static VolunteerDAO getInstance() {
        if (instance == null) {
            instance = new VolunteerDAO();
        }
        return instance;
    }

    private VolunteerDAO() {
        cm = null;
        try {
            cm = DBConnectionManager.getInstance();
        } catch (IOException ex) {
            System.out.println("Couldn't connect to the database.");
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get all idle volunteers
     *
     * @return
     */
    public List<Volunteer> getAllIdleVolunteers() {

        List<Volunteer> volunteers = new ArrayList<>();

        String sql = "SELECT * FROM Volunteer v "
                + "JOIN Person p ON p.ID = v.PersonID "
                + "WHERE v.IsIdle = 1";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                volunteers.add(getOneVolunteer(rs));
            }
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return volunteers;
    }

    /**
     * All the volunteers from DB.
     *
     * @return
     */
    public List<Volunteer> getAllVolunteersNotIdle() {
        List<Volunteer> volunteers = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer v "
                + "JOIN Person p ON p.ID = v.PersonID "
                + "WHERE v.IsIdle = 0 ";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                volunteers.add(getOneVolunteer(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuildDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return volunteers;
    }

    // Get one volunteer from DB.
    private Volunteer getOneVolunteer(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String eMail = rs.getString("Email");
        int phoneNumber = rs.getInt("Phone");
        boolean isIdle = rs.getBoolean("IsIdle");
        String language = rs.getString("Language");
        String description = rs.getString("Description");
        InputStream in = rs.getBinaryStream("Picture");
        Image img = null;
        if (in != null) {
            img = new Image(in);
        }

        ELanguage Elang = ELanguage.getLanguageByString(language);

        Volunteer volunteer = new Volunteer(id, firstName, lastName, eMail, phoneNumber, isIdle, Elang);
        volunteer.setDescription(description);
        volunteer.setImage(img);

        return volunteer;
    }

    /**
     * Create a Volunteer in the Person table.
     *
     * @param newVolunteer
     */
    public void createVolunteer(Volunteer newVolunteer) {
        try (Connection con = cm.getConnection()) {
            int id = createNewPersonInDatabase(con, newVolunteer);
            addVolunteer(id);
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add the Volunteer to the Volunteer table
     *
     * @param personID
     */
    private void addVolunteer(int personID) {
        String sql = "INSERT INTO Volunteer "
                + "(PersonID, IsIdle) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, personID);
            ps.setInt(2, 0);

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println("Couldn't add newVolunteer to DB");
            System.out.println(sqlException);
            Logger
                    .getLogger(VolunteerDAO.class
                            .getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    /**
     * Set the description of the volunteer
     *
     * @param id
     * @param text
     */
    public void setVolunteerDescription(int id, String text) {
        try (Connection con = cm.getConnection()) {
            String sql = "UPDATE Volunteer "
                    + "SET Description = ? "
                    + "WHERE PersonID = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, text);
            ps.setInt(2, id);

            ps.executeUpdate();
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates the new edits for the volunteer and saves it in the DB.
     *
     * @param volunteer
     */
    public void updateVolunteerPersonInfo(Volunteer volunteer) {
        try (Connection con = cm.getConnection()) {
            // updatePersonInformation is from a abstract class "APersonDAO".
            updatePersonInformation(con, volunteer);
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update the idle status of the volunteer
     *
     * @param id
     * @param value
     */
    public void updateVolunteerIdleStatus(int id, boolean value) {
        String sql;
        if (value) {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 1 "
                    + "WHERE PersonID = ?";
        } else {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 0 "
                    + "WHERE PersonID = ?";
        }

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes the selected volunteer from the DB.
     *
     * @param id
     */
    public void deleteVolunteer(int id) {
        try (Connection con = cm.getConnection()) {
            deletePersonFromDatabaseByID(con, id);
        } catch (SQLException ex) {
            System.out.println("Couldn't remove volunteer from the DB");
            System.out.println(ex);
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Set the volunteer image
     *
     * @param id
     * @param file
     */
    public void setVolunteerImage(int id, File file) {
        try (Connection con = cm.getConnection()) {
            setPersonImage(con, id, file);
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
