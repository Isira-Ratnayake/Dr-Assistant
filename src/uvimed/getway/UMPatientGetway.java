package uvimed.getway;

import database.DBConnection;
import getway.PatientGetway;
import getway.PrescriptionGetway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Patient;

/**
 *
 * @author IU Ratnayake
 */
public class UMPatientGetway extends PatientGetway {
    
        public Patient selectedPatient(String name, LocalDate dob) {
            DBConnection connection = new DBConnection();
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            con = connection.geConnection();
            Patient patient = new Patient();
            try {
                pst = con.prepareStatement("select * from patient where name = '" + name +"' and date_of_birth ='" + String.valueOf(dob) + "';");
                rs = pst.executeQuery();
                if (rs.next()) {
                    patient = new Patient(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            LocalDate.parse(rs.getString(4)),
                            rs.getInt(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9)
                    );
                }
                rs.close();
                pst.close();
                con.close();
                connection.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PatientGetway.class.getName()).log(Level.SEVERE, null, ex);
            }
            return patient;
        }
    
}
