package com.example.servingwebcontent;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Configuration class to set up Firestore database connectivity.
 * This class configures and initializes Firebase Firestore using credentials provided in a configuration file.
 *
 * The @Configuration annotation indicates that the class has @Bean definition methods,
 * allowing Spring IoC container to process and generate beans to be used in the application.
 */
@Configuration
public class FirebaseConfig {

    // Path to the Firebase service account file, injected from application properties.
    @Value("${firebase.config.path}")
    private Resource serviceAccount;

    /**
     * Creates and configures a Firestore instance for use throughout the application.
     *
     * This bean will provide a Firestore instance configured with credentials specified by the serviceAccount resource.
     * It checks if a Firebase app has already been initialized to prevent re-initialization errors.
     *
     * @return Firestore instance to interact with Firebase Firestore database.
     * @throws IOException if there are issues reading the service account file.
     */
    @Bean
    public Firestore firestore() throws IOException {
        // Load the Google credentials from the service account file
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount.getInputStream());

        // Set up Firebase options with the credentials
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Initialize the Firebase app with options if it hasn't been initialized already
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        // Return the Firestore instance from the initialized Firebase app
        return FirestoreClient.getFirestore();
    }
}
