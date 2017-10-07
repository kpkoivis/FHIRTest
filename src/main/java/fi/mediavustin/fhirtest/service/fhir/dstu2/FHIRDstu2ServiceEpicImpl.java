/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

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

        IGenericClient client = ctx.newRestfulGenericClient(baseUrl);

        Bundle results = client
                .search()
                .forResource(Patient.class)
                .where(Patient.GIVEN.matches().value(givenName))
                .where(Patient.FAMILY.matches().value(familyName))
                .returnBundle(Bundle.class)
                .execute();

        List<Patient> patientList = new ArrayList();
        results.getEntry().forEach(entry -> {
            patientList.add((Patient) entry.getResource());
        });

        return patientList;

    }
}
