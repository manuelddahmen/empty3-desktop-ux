package one.empty3.apps.opad.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import java.io.InputStream;
import java.io.IOException;

public class FirestoreService {
    private static Firestore db;

    public static void init() throws IOException {
        // Load the key file from the classpath (src folder)
        InputStream serviceAccount = FirestoreService.class.getClassLoader()
                .getResourceAsStream("serviceAccountKey.json");

        if (serviceAccount == null) {
            throw new IOException("serviceAccountKey.json not found in src/ folder");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    public static Firestore getDb() {
        return db;
    }
}