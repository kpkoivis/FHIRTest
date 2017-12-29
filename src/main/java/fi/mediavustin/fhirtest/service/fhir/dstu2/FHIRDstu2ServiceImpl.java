/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author kris
 */
@Service
public class FHIRDstu2ServiceImpl implements FHIRDstu2Service {

    private FhirContext ctx;

    @PostConstruct
    public void setup() {
        this.ctx = FhirContext.forDstu2();
    }

    
    @Override
    public IGenericClient getClient(String baseUrl) {
        return ctx.newRestfulGenericClient(baseUrl);
    }
    
    @Override
    public List<Patient> searchForPatient(IGenericClient client, String familyName, String givenName) {

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

    @Override
    public Conformance getConformanceStatement(IGenericClient client) {
        // Retrieve the server's conformance statement
        Conformance conf = client.capabilities().ofType(Conformance.class).execute();
        //System.out.println(conf.getDescriptionElement().getValue());
        return conf;
    }

    @Override
    public String resourceAsJSONString(IBaseResource resource) {
        IParser jsonParser = ctx.newJsonParser();
        jsonParser.setPrettyPrint(true);
        String encoded = jsonParser.encodeResourceToString(resource);
        return encoded;
    }
    
}
