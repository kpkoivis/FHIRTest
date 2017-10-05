/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author kris
 */
@Service
public class FHIRDstu2ServiceEpicImpl implements FHIRDstu2Service {

    private FhirContext ctx;
    private final String baseUrl = "https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/";

    @PostConstruct
    public void setup() {
        this.ctx = FhirContext.forDstu2();
    }

    @Override
    public List<Patient> searchForPatient(String familyName, String givenName) {

        String patientSearchString = "/Patient?given=" + givenName + "&family=" + familyName;

        URI patientUri;

        List<Patient> patientList = new ArrayList();
        
        try {
            patientUri = new URI(baseUrl + patientSearchString);
            RestTemplate restTemplate = new RestTemplate();

            String result = restTemplate.getForObject(patientUri, String.class);

            //System.out.println(result);

            IParser parser = this.ctx.newJsonParser();

            Bundle bundle = parser.parseResource(Bundle.class, result);
            
             

            bundle.getEntry().forEach(entry -> {
                patientList.add((Patient)entry.getResource());
            });

        } catch (URISyntaxException ex) {
            Logger.getLogger(FHIRDstu2ServiceEpicImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return patientList;

    }
}
