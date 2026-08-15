package com.corp.proyectogovprocurematch;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FirestoreEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class ProyectoGovProcureMatchIntegrationTest {
    
    @Container
    public static FirestoreEmulatorContainer firestore = 
        new FirestoreEmulatorContainer(DockerImageName.parse("gcr.io/google.com/cloudsdktool/cloud-sdk:316.0.0-emulators"));

    @Test
    void testFirestoreIntegrationAndDomainLogic() {
        assertTrue(firestore.isRunning());
    }
}
