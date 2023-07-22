package uvimed.typecast;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.PrescriptionDrug;
import model.TemplateDrug;

public class UMTypeCast {
    
    public static TemplateDrug CastPrescriptionDrugToTemplateDrug(PrescriptionDrug pd) {
        return new TemplateDrug(pd.getId(), pd.getDrugId(), pd.getDrugName(), pd.getDrugType(), pd.getDrugStrength(), pd.getDrugDose(), pd.getDrugDuration(), pd.getDrugAdvice());    
    }
    
    public static ObservableList<TemplateDrug> CastPDListotTDList(ObservableList<PrescriptionDrug> pdl) {
        ObservableList<TemplateDrug> tdl = FXCollections.observableArrayList();
        for(PrescriptionDrug pd:pdl) {
            tdl.add(CastPrescriptionDrugToTemplateDrug(pd));
        }
        return tdl;
    }
    
}
