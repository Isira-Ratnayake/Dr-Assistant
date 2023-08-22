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
package uvimed.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Template;
import model.TemplateDrug;
import getway.TemplateGetway;

/**
 * FXML Controller class
 *
 * @author Work
 */
public class NewTemplateNamer implements Initializable {
    
    
    @FXML
    private TextField tfTemplateName;
    @FXML
    private TextField tfTemplateNote;
    
    private Template template = new Template();
    private ObservableList<TemplateDrug> templateDrugList = FXCollections.observableArrayList();
    private TemplateGetway templateGetway = new TemplateGetway();
    
    @FXML
    private void handleSaveTemplate(ActionEvent event) {
        if (templateDrugList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add drug first");
            alert.setHeaderText("You didn't add any drug");
            alert.show();
        } else if (tfTemplateName.getText() == null || tfTemplateName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Template name empty");
            alert.setHeaderText("You didn't give your template a name");
            alert.show();
        } else {
            template.setTemplateName(tfTemplateName.getText());
            template.setNote(tfTemplateNote.getText());
            if (templateGetway.save(template, templateDrugList)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Template added");
                alert.setHeaderText("Template saved");
                alert.setContentText("Template saved");
                alert.showAndWait();
                resetForm();
            }
        }
    }
    
    private void resetForm() {
        tfTemplateName.setText(null);
        tfTemplateNote.setText(null);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setTemplateStruct(Template template, ObservableList<TemplateDrug> templateDrugList) {
        this.template = template;
        this.templateDrugList = templateDrugList;
    }
    
}
