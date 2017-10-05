/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import javax.annotation.PostConstruct;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

/**
 *
 * @author kris
 */
@Service
public class FHIRDstu3ServiceOmakantaImpl implements FHIRDstu3Service {

    private FhirContext ctx;
    private final String baseUrl = "http://fhirsandbox.kanta.fi/phr-resourceserver/baseStu3/";

    @PostConstruct
    public void setup() {
        this.ctx = FhirContext.forDstu3();
    }

    @Override
    public IIdType createPatient(Patient patient) {

        IGenericClient client = ctx.newRestfulGenericClient(baseUrl);

        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();
        
        return outcome.getCreated() ? outcome.getId(): null;
    }
    
    @Override
    public Patient getPatient(IIdType patientId) {

        IGenericClient client = ctx.newRestfulGenericClient(baseUrl);

        return client.read().resource(Patient.class).withId(patientId.getIdPart()).execute();
    }
    
}
