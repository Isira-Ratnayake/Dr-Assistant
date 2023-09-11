/*
 * Copyright 2023 Work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uvimed.getway;

import database.DBConnection;
import getway.PrescriptionGetway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import model.Prescription;
import model.TemplateDrug;

/**
 *
 * @author Work
 */
public class UMPrescriptionGetway extends PrescriptionGetway {
    
    
    public void update(Prescription prescription, ObservableList<TemplateDrug> templateDrugList) {
        boolean saved = false;
        DBConnection connection = new DBConnection();
        Connection con = null;
        PreparedStatement pst = null;
        con = connection.geConnection();
        try {
            pst = con.prepareStatement("update prescription set cc = ?, oe = ?, pd = ?, dd = ?, lab_workup = ?, advice = ?, next_visit = ? where id = ?;");
            pst.setString(1, prescription.getCc());
            pst.setString(2, prescription.getOe());
            pst.setString(3, prescription.getPd());
            pst.setString(4, prescription.getDd());
            pst.setString(5, prescription.getLabWorkUp());
            pst.setString(6, prescription.getAdvice());
            pst.setString(7, prescription.getNextVisit());
            pst.setInt(8, prescription.getId());
            pst.executeUpdate();
            pst.close();
            con.close();
            connection.con.close();
            deletePrescriptionDrugs(prescription.getId());
            for (int i = 0; i < templateDrugList.size(); i++) {
                updatePrescriptionDrug(templateDrugList.get(i), prescription.getId());
            }

        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionGetway.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    private void deletePrescriptionDrugs(int prescriptionId) {
        DBConnection connection = new DBConnection();
        Connection con = null;
        PreparedStatement pst = null;
        con = connection.geConnection();
        try {
            pst = con.prepareStatement("delete from prescription_drug where prescription_id = ?");
            pst.setInt(1, prescriptionId);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionGetway.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    private void updatePrescriptionDrug(TemplateDrug templateDrug, int prescriptionId) {
        DBConnection connection = new DBConnection();
        Connection con = null;
        PreparedStatement pst = null;
        con = connection.geConnection();
        try {
            pst = con.prepareStatement("insert into prescription_drug values(?,?,?,?,?,?,?,?)");
            pst.setString(1, null);
            pst.setInt(2, prescriptionId);
            pst.setInt(3, templateDrug.getDrug_id());
            pst.setString(4, templateDrug.getType());
            pst.setString(5, templateDrug.getDose());
            pst.setString(6, templateDrug.getDuration());
            pst.setString(7, templateDrug.getStrength());
            pst.setString(8, templateDrug.getAdvice());
            pst.executeUpdate();
            pst.close();
            con.close();
            connection.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionGetway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
