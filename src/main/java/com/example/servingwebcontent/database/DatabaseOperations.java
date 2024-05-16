package com.example.servingwebcontent.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

// Class containing static methods to perform various database operations using Firestore.
public class DatabaseOperations {

    // Retrieves all documents from a specified Firestore collection and returns them as a list of maps.
    public static List<Map<String, Object>> getAllDocumentsFromFirestore(Firestore db, String collection) {
        List<Map<String, Object>> documentsData = new ArrayList<>();

        try {
            // Access the collection reference and asynchronously retrieve all documents.
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                // Loop through each document snapshot to extract data.
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    documentsData.add(data);
                }
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }
            return documentsData;
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
        }
        return documentsData;
    }

    // Retrieves the names (IDs) of all documents in a specified Firestore collection.
    public static List<String> getDocumentNamesFromFirestore(Firestore db, String collection) {
        List<String> documentNames = new ArrayList<>();
        try {
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                // Extract document IDs and add them to the list.
                querySnapshot.getDocuments().forEach(document -> documentNames.add(document.getId()));
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
        }
        return documentNames;
    }

    // Retrieves data from a specific Firestore document using its ID within a given collection.
    public static Map<String, Object> getDocumentDataFromFirestore(Firestore db, String collection, String doc) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(doc);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Map<String, Object> data = document.getData();
            System.out.println("Returned successful");
            return data;
        } else {
            return Collections.emptyMap();
        }
    }

    // Generic methods to write data to a Firestore document. Overloads allow different data types.
    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, String value) {
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, int value) {
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, boolean value) {
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    // Overloaded method to write a map of data to a Firestore document.
    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, Map<String, Object> data) {
        try {
            // Merging existing document data with new data to prevent overwrites.
            Map<String, Object> dataCopy = new HashMap<>(data);
            dataCopy.putAll(getDocumentDataFromFirestore(db, collectionName, documentId));

            DocumentReference docRef = db.collection(collectionName).document(documentId);
            ApiFuture<WriteResult> result = docRef.set(dataCopy);
            System.out.println("Write successful with timestamp: " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing document: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
