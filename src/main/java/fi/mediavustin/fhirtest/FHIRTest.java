/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest;

import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import fi.mediavustin.fhirtest.service.fhir.dstu2.FHIRDstu2Service;
import fi.mediavustin.fhirtest.service.fhir.dstu3.FHIRDstu3Service;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author kris
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("fi.mediavustin")
public class FHIRTest {

    // Epic DSTU2 sandboxes
    private final String epicDSTU2baseUrl = "https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/";
    //private final String epicDSTU2baseUrl = "https://open-ic.epic.com/argonaut/api/FHIR/Argonaut/";
    
    // Omakanta PHR DSTU3 sandboxes
    private final String omakantaDSTU3baseUrl = "http://fhirsandbox.kanta.fi/phr-resourceserver/baseStu3/";

    @Autowired
    private FHIRDstu2Service fhirDstu2Service;

    @Autowired
    private FHIRDstu3Service fhirDstu3Service;

    public static void main(String... args) {
        SpringApplication.run(FHIRTest.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

        return new CommandLineRunner() {
            @Override
            public void run(String[] args) throws Exception {

                // get a dstu2 client and print out the capability (conformance) statement of the dstu2 server the client has connected to
                IGenericClient epicClient = fhirDstu2Service.getClient(epicDSTU2baseUrl);
                Conformance confStatement = fhirDstu2Service.getConformanceStatement(epicClient);
                String confStatementJSON = fhirDstu2Service.resourceAsJSONString(confStatement);
                System.out.println(confStatementJSON);

                // get a dstu3 client and print out the capability (conformance) statement of the dstu3 server the client has connected to
                IGenericClient omakantaClient = fhirDstu3Service.getClient(omakantaDSTU3baseUrl);
                CapabilityStatement capsStatement = fhirDstu3Service.getCapabilityStatement(omakantaClient);
                String capsStatementJSON = fhirDstu3Service.resourceAsJSONString(capsStatement);
                System.out.println(capsStatementJSON);

                // a simple copy from one server to another
                testCopyPatient(epicClient, omakantaClient);

            }
                      
            

            private void testCopyPatient(IGenericClient epicClient, IGenericClient omakantaClient) {
                
                
                List<ca.uhn.fhir.model.dstu2.resource.Patient> epicPatients = fhirDstu2Service.searchForPatient(epicClient, "Argonaut", "Jason");
                
                System.out.println("Epic sandbox patient count: " + epicPatients.size());
                
                if (epicPatients.size() > 0) {
                    ca.uhn.fhir.model.dstu2.resource.Patient epicPatient = epicPatients.get(0);

                    Patient omakantaPatient = new Patient();

                    omakantaPatient.addIdentifier().setSystem("urn:system").setValue("12345");
                    HumanName name = new HumanName();
                    name.setFamily(epicPatient.getNameFirstRep().getFamilyFirstRep().toString());
                    name.addGiven(epicPatient.getNameFirstRep().getGivenFirstRep().toString());
                    List<HumanName> names = new ArrayList();
                    names.add(name);

                    omakantaPatient.setName(names);

                    /*
                    IIdType patientId = omakantaFHIRService.createPatient(omakantaPatient);
                    
                    if (patientId != null) {
                        System.out.println("Created a patient resource in Omakanta PHR with ID: " + patientId.getIdPart());
                    } else {
                        System.out.println("The patient resource in Omakanta PHR was not created. Aborting.");
                        return;
                    }
                    
                    Patient createdPatient = omakantaFHIRService.getPatient(patientId);     
                    
                    if (createdPatient != null) {
                        HumanName createdPatientName = createdPatient.getName().get(0);
                        System.out.println("The name of the patient created:");
                        System.out.println("Family name: " + createdPatientName.getFamily());
                        System.out.println("Given name: " + createdPatientName.getGiven().get(0));
                    } else {
                        System.out.println("No patient with id " + patientId.getIdPart() + " was found in Omakanta.");
                    }
*/
                }
            }
        };
    }
}
