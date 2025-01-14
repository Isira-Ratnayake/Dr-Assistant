/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.prescription;

import controller.patient.PatientsController;
import controller.template.ViewTemplateController;
import getway.PatientGetway;
import getway.PrescriptionGetway;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import model.Patient;
import model.Prescription;
import model.PrescriptionDrug;
import uvimed.controller.EditPrescriptionController;
import view.html.PrescriptionMaker;

/**
 * FXML Controller class
 *
 * @author RIfat
 */
public class ViewPrescriptionController implements Initializable {

    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblDate;
    @FXML
    private WebView webView;

    ObservableList<PrescriptionDrug> drugs = FXCollections.observableArrayList();

    PrescriptionGetway prescriptionGetway = new PrescriptionGetway();
    PatientGetway patientGetway = new PatientGetway();

    Prescription prescription = new Prescription();
    Patient patient = new Patient();

    PrescriptionMaker maker = new PrescriptionMaker();

    WebEngine engine = new WebEngine();

    File file = null;
    PrintStream out = null;
    Window owner;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            System.out.println("working on it");
        });
    }

    @FXML
    private void handlePrintButton(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        job.showPrintDialog(owner);
        
        if (job != null) {
            engine.print(job);
            job.endJob();
        }
    }

    public void loadPrescription(int prescriptionId) {
        this.prescriptionId = prescriptionId;
        prescription = prescriptionGetway.getPrescription(prescriptionId);
        this.patientId = prescription.getPatientId();
        drugs = prescriptionGetway.getSelectedPrescriptionDrugs(prescriptionId);
        patient = patientGetway.selectedPatient(prescription.getPatientId());
        lblPatientName.setText("Patient Name : " + prescription.getPatientName());
        lblDate.setText("Prescription Date : " + prescription.getDate());
        try {
            file = new File("Prescription.html");
            file.createNewFile();

            out = new PrintStream(file);
            out.print(maker.makePrescription(prescription, drugs,patient));

            engine = webView.getEngine();
            engine.load(file.toURI().toString());

//            Desktop.getDesktop().browse(URI.create("Prescription.html"));
            System.out.println(file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(ViewTemplateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*************** UVIMED *****************/  
    
    private int prescriptionId;
    private int patientId;
    
    
    @FXML
    private void handleEditPrescription(ActionEvent event) {
        openEditPrescriptionStage(patientId, prescriptionId);
        //loadPrintablePrescription(1, false);
    }
    
    private void openEditPrescriptionStage(int patientId, int prescriptionId) {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/uvimed/view/EditPrescription.fxml"));
        try {
            Parent root = fXMLLoader.load();
            EditPrescriptionController controller = fXMLLoader.getController();
            controller.loadPatient(patientId);
            //load prescription
            controller.loadPrescriptionDetails(prescriptionId);
            controller.setCredentials(prescriptionId, patientId);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Edit Patient");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest((WindowEvent event1) -> {
                this.loadPrescription(prescriptionId);
            });
        } catch (IOException ex) {
            Logger.getLogger(PatientsController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
    
}
