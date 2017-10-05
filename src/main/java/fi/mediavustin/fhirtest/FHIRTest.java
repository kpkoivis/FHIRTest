/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mediavustin.fhirtest;

import fi.mediavustin.fhirtest.service.fhir.dstu2.FHIRDstu2Service;
import fi.mediavustin.fhirtest.service.fhir.dstu3.FHIRDstu3Service;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
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

    @Autowired
    private FHIRDstu2Service epicFHIRService;

    @Autowired
    private FHIRDstu3Service omakantaFHIRService;

    public static void main(String... args) {
        SpringApplication.run(FHIRTest.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

        return new CommandLineRunner() {
            @Override
            public void run(String[] args) throws Exception {

                List<ca.uhn.fhir.model.dstu2.resource.Patient> epicPatients = epicFHIRService.searchForPatient("Argonaut", "Jason");
                
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

                }
                      
            }

        };
    }
}
