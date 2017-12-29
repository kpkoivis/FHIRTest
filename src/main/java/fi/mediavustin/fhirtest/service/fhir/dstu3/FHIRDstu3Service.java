/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu3;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 *
 * @author kris
 */
public interface FHIRDstu3Service {
    public IIdType createPatient(IGenericClient client, Patient patient);
    public Patient getPatient(IGenericClient client, IIdType patientId);
    public CapabilityStatement getCapabilityStatement(IGenericClient client);
    public IGenericClient getClient(String baseUrl);
    public String resourceAsJSONString(IBaseResource resource);
}
