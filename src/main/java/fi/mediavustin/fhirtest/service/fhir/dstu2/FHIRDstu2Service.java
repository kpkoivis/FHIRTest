/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest.service.fhir.dstu2;

import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import java.util.List;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 *
 * @author kris
 */
public interface FHIRDstu2Service {
    public List<Patient> searchForPatient(IGenericClient client, String familyName, String givenName);
    public Conformance getConformanceStatement(IGenericClient client);
    public IGenericClient getClient(String baseUrl);
    public String resourceAsJSONString(IBaseResource resource);
}
