package com.example.servingwebcontent.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.concurrent.ExecutionException;


public class DatabaseOperations {
    //    public static void getDataFromFirestore(Firestore db, String collection, String doc) throws ExecutionException, InterruptedException {
//        DocumentReference docRef = db.collection(collection).document(doc);
//        ApiFuture<DocumentSnapshot> future = docRef.get();
//        DocumentSnapshot document = future.get();
//        if (document.exists()) {
//            System.out.println("Document data: ");
//            Map<String, Object> data = document.getData();
//            if (data != null) {
//                for (Map.Entry<String, Object> entry : data.entrySet()) {
//                    System.out.println(entry.getKey() + ": " + entry.getValue());
//                }
//            } else {
//                System.out.println("Document is empty");
//            }
//        } else {
//            System.out.println("No such document!");
//        }
//    }

    public static List<Map<String, Object>> getAllDocumentsFromFirestore(Firestore db, String collection) {
        List<Map<String, Object>> documentsData = new ArrayList<>();

        try{
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    documentsData.add(data);
                }
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }
            return documentsData;
        } catch (InterruptedException | ExecutionException e){
            // Handle the exception, maybe log it, or rethrow as a runtime exception
            e.printStackTrace();
            // Optionally, you could wrap and rethrow as a runtime exception:
            // throw new RuntimeException("Failed to get document names from Firestore", e);
        }
        return documentsData;

    }

    public static List<String> getDocumentNamesFromFirestore(Firestore db, String collection) {
        List<String> documentNames = new ArrayList<>();
        try {
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                querySnapshot.getDocuments().forEach(document -> documentNames.add(document.getId()));
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception, maybe log it, or rethrow as a runtime exception
            e.printStackTrace();
            // Optionally, you could wrap and rethrow as a runtime exception:
            // throw new RuntimeException("Failed to get document names from Firestore", e);
        }
        return documentNames;
    }

    // This method retrieves data from a Firestore document
    public static Map<String, Object> getDocumentDataFromFirestore(Firestore db, String collection, String doc) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(doc);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Map<String, Object> data = document.getData();
            System.out.println("Returned successful");
            return data;
        } else {
            Map<String, Object> data = Collections.emptyMap();
            return data;
        }
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, String value){
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, int value){
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, boolean value){
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        writeDataToFirestore(db, collectionName, documentId, data);
    }

    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, Map<String, Object> data){
        try {
            Map<String, Object> dataCopy = new HashMap<>(data);

            dataCopy.putAll(getDocumentDataFromFirestore(db, collectionName, documentId));
            System.out.println(dataCopy);

            // Create a document reference
            DocumentReference docRef = db.collection(collectionName).document(documentId);
            // Set the document with the provided data. This will overwrite an existing document or create a new one if it does not exist.
            ApiFuture<WriteResult> result = docRef.set(dataCopy);
            // Print out the update time of the write result
            System.out.println("Write successful with timestamp: " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing document: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
