/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu2;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import java.util.List;

/**
 *
 * @author kris
 */
public interface FHIRDstu2Service {
    public List<Patient> searchForPatient(String familyName, String givenName);
    
}
