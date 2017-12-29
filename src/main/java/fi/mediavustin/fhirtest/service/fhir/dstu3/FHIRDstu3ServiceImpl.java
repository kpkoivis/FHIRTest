/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import javax.annotation.PostConstruct;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

/**
 *
 * @author kris
 */
@Service
public class FHIRDstu3ServiceImpl implements FHIRDstu3Service {

    private FhirContext ctx;

    @PostConstruct
    public void setup() {
        this.ctx = FhirContext.forDstu3();
    }
    
    @Override
    public IGenericClient getClient(String baseUrl) {
        return ctx.newRestfulGenericClient(baseUrl);
    }
    


    @Override
    public IIdType createPatient(IGenericClient client, Patient patient) {

        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        return outcome.getCreated() ? outcome.getId() : null;
    }

    @Override
    public Patient getPatient(IGenericClient client, IIdType patientId) {
        return client
                .read()
                .resource(Patient.class)
                .withId(patientId.getIdPart())
                .execute();
    }

    @Override
    public CapabilityStatement getCapabilityStatement(IGenericClient client) {
        CapabilityStatement statement = client.capabilities().ofType(CapabilityStatement.class).execute();
        return statement;
    }

    @Override
    public String resourceAsJSONString(IBaseResource resource) {
        IParser jsonParser = ctx.newJsonParser();
        jsonParser.setPrettyPrint(true);
        String encoded = jsonParser.encodeResourceToString(resource);
        return encoded;
    }
}
