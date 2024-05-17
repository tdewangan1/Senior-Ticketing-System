package com.example.servingwebcontent.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

//
//// Class containing static methods to perform various database operations using Firestore.
//public class DatabaseOperations {
//
//    // Retrieves all documents from a specified Firestore collection and returns them as a list of maps.
//    public static List<Map<String, Object>> getAllDocumentsFromFirestore(Firestore db, String collection) {
//        List<Map<String, Object>> documentsData = new ArrayList<>();
//
//        try {
//            // Access the collection reference and asynchronously retrieve all documents.
//            CollectionReference colRef = db.collection(collection);
//            ApiFuture<QuerySnapshot> future = colRef.get();
//            QuerySnapshot querySnapshot = future.get();
//
//            if (!querySnapshot.isEmpty()) {
//                // Loop through each document snapshot to extract data.
//                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
//                    Map<String, Object> data = document.getData();
//                    documentsData.add(data);
//                }
//                System.out.println("Returned successful");
//            } else {
//                System.out.println("No documents found in the collection.");
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            // Handle the exception appropriately.
//            e.printStackTrace();
//            Thread.currentThread().interrupt(); // Restore interrupted state
//        }
//        return documentsData;
//    }
//
//    // Retrieves the names (IDs) of all documents in a specified Firestore collection.
//    public static List<String> getDocumentNamesFromFirestore(Firestore db, String collection) {
//        List<String> documentNames = new ArrayList<>();
//        try {
//            CollectionReference colRef = db.collection(collection);
//            ApiFuture<QuerySnapshot> future = colRef.get();
//            QuerySnapshot querySnapshot = future.get();
//
//            if (!querySnapshot.isEmpty()) {
//                // Extract document IDs and add them to the list.
//                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
//                    documentNames.add(document.getId());
//                }
//                System.out.println("Returned successful");
//            } else {
//                System.out.println("No documents found in the collection.");
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            // Handle the exception appropriately.
//            e.printStackTrace();
//            Thread.currentThread().interrupt(); // Restore interrupted state
//        }
//        return documentNames;
//    }
//
//    // Retrieves data from a specific Firestore document using its ID within a given collection.
//    public static Map<String, Object> getDocumentDataFromFirestore(Firestore db, String collection, String doc) throws ExecutionException, InterruptedException {
//        DocumentReference docRef = db.collection(collection).document(doc);
//        ApiFuture<DocumentSnapshot> future = docRef.get();
//        DocumentSnapshot document = future.get();
//        if (document.exists()) {
//            System.out.println("Returned successful");
//            return document.getData();
//        } else {
//            return Collections.emptyMap();
//        }
//    }
//
//    public static Map<String, Object> getDocumentByUsernameFromFirestore(Firestore db, String collection, String username) {
//        Map<String, Object> documentData = new HashMap<>();
//
//        try {
//            // Access the collection reference and create a query.
//            CollectionReference colRef = db.collection(collection);
//            Query query = colRef.whereEqualTo("username", username);
//
//            // Asynchronously retrieve the first document that matches the query.
//            ApiFuture<QuerySnapshot> future = query.get();
//            QuerySnapshot querySnapshot = future.get();
//
//            if (!querySnapshot.isEmpty()) {
//                // Extract data from the first document snapshot.
//                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
//                documentData = document.getData();
//                System.out.println("Returned successful");
//            } else {
//                System.out.println("No document found in the collection with username: " + username);
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            // Handle the exception appropriately.
//            e.printStackTrace();
//            Thread.currentThread().interrupt(); // Restore interrupted state
//        }
//
//        return documentData;
//    }
//
//    // Generic methods to write data to a Firestore document. Overloads allow different data types.
//    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, String value) {
//        Map<String, Object> data = new HashMap<>();
//        data.put(field, value);
//        writeDataToFirestore(db, collectionName, documentId, data);
//    }
//
//    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, int value) {
//        Map<String, Object> data = new HashMap<>();
//        data.put(field, value);
//        writeDataToFirestore(db, collectionName, documentId, data);
//    }
//
//    public static void writeDataToFirestoreByUsername(Firestore db, String collectionName, String username, String field, int value) {
//        Map<String, Object> documentData = getDocumentByUsernameFromFirestore(db, collectionName, username);
//        if (!documentData.isEmpty()) {
//            String documentId = (String) documentData.get("id");
//            writeDataToFirestore(db, collectionName, documentId, field, value);
//        } else {
//            System.out.println("No document found with the username: " + username);
//        }
//    }
//
//    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, String field, boolean value) {
//        Map<String, Object> data = new HashMap<>();
//        data.put(field, value);
//        writeDataToFirestore(db, collectionName, documentId, data);
//    }
//
//    // Overloaded method to write a map of data to a Firestore document.
//    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, Map<String, Object> data) {
//        try {
//            // Merging existing document data with new data to prevent overwrites.
//            DocumentReference docRef = db.collection(collectionName).document(documentId);
//            ApiFuture<DocumentSnapshot> future = docRef.get();
//            DocumentSnapshot documentSnapshot = future.get();
//            if (documentSnapshot.exists()) {
//                Map<String, Object> existingData = documentSnapshot.getData();
//                data.putAll(existingData);
//            }
//
//            ApiFuture<WriteResult> result = docRef.set(data);
//            System.out.println("Write successful with timestamp: " + result.get().getUpdateTime());
//        } catch (InterruptedException | ExecutionException e) {
//            System.err.println("Error writing document: " + e.getMessage());
//            Thread.currentThread().interrupt(); // Restore interrupted state
//        }
//    }
//}







// Class containing static methods to perform various database operations using Firestore.
public class DatabaseOperations {


    // GETTING DATA FROM THE DATABASE


    // Retrieves all documents from a specified Firestore collection and returns them as a list of maps.
    // Contributors: Tanmay Dewangan
    public static List<Map<String, Object>> getAllDocumentsFromFirestore(Firestore db, String collection) {
        List<Map<String, Object>> documentsData = new ArrayList<>();

        try {
            // Access the collection reference and asynchronously retrieve all documents.
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            // If the collection is not empty, loop through each document snapshot to extract data.
            if (!querySnapshot.isEmpty()) {
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    documentsData.add(data);
                }
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }

            // Return the list of document data.
            return documentsData;
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
        }
        // Return an empty list if an exception occurs.
        return documentsData;
    }

    // Retrieves the names (IDs) of all documents in a specified Firestore collection.
    // Contributors: Shubham Kale
    public static List<String> getDocumentNamesFromFirestore(Firestore db, String collection) {
        // Create a list to store document names.
        List<String> documentNames = new ArrayList<>();

        // Try to retrieve the document names from the Firestore collection.
        try {
            CollectionReference colRef = db.collection(collection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            QuerySnapshot querySnapshot = future.get();

            // If the collection is not empty, extract document IDs and add them to the list.
            if (!querySnapshot.isEmpty()) {
                querySnapshot.getDocuments().forEach(document -> documentNames.add(document.getId()));
                System.out.println("Returned successful");
            } else {
                System.out.println("No documents found in the collection.");
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
        }

        // Return the list of document names.
        return documentNames;
    }

    // Retrieves data from a specific Firestore document using its ID within a given collection.
    // Contributors: Shubham Kale
    public static Map<String, Object> getDocumentDataFromFirestore(Firestore db, String collection, String id) throws ExecutionException, InterruptedException {
        // Access the Firestore document reference and retrieve the document snapshot.
        DocumentReference docRef = db.collection(collection).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        // If the document exists, return the document data.
        if (document.exists()) {
            Map<String, Object> data = document.getData();
            System.out.println("Returned successful");
            return data;

        // If the document does not exist, return an empty map.
        } else {
            return Collections.emptyMap();
        }
    }

    // Retrieves data from a Firestore document using the username within a given collection.
    // Contributors: Tanmay Dewangan and Shubham Kale
    public static Map<String, Object> getDocumentByUsernameFromFirestore(Firestore db, String collection, String username) {
        // Create an empty map to store the document data.
        Map<String, Object> documentData = new HashMap<>();

        try {
            // Access the collection reference and create a query.
            CollectionReference colRef = db.collection(collection);
            Query query = colRef.whereEqualTo("username", username);

            // Asynchronously retrieve the first document that matches the query.
            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                // Extract data from the first document snapshot.
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                documentData = document.getData();
                System.out.println("Returned successful");
            } else {
                System.out.println("No document found in the collection with userType: " + username);
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
        }

        // Return the document data.
        return documentData;
    }



    // WRITING DATA TO THE DATABASE


    // Generic methods to write data to a Firestore document. Overloads allow different data types.
    // Contributors: Tanmay Dewangan
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

    /*
     * Write data to firebase document using the username instead of the document data.
     * This was used to update the volunteer hours of a user.
     * Contributors: Tanmay Dewangan and Shubham Kale
     */
    public static void writeDataToFirestoreByUsername(Firestore db, String collectionName, String username, String field, int value) {
        try {
            // Access the collection reference and create a query.
            CollectionReference colRef = db.collection(collectionName);
            Query query = colRef.whereEqualTo("username", username);

            // Asynchronously retrieve the first document that matches the query.
            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                // Extract data from the first document snapshot.
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                String documentId = document.getId();

                // Create a new map with the field and value to be updated.
                Map<String, Object> data = new HashMap<>();
                data.put(field, value);

                // Write the data to the Firestore document.
                DocumentReference docRef = db.collection(collectionName).document(documentId);
                ApiFuture<WriteResult> result = docRef.set(data, SetOptions.merge());
                System.out.println("Write successful with timestamp: " + result.get().getUpdateTime());
            } else {
                System.out.println("No document found in the collection with username: " + username);
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Restore interrupted state
        }
    }


    // Overloaded method to write a map of data to a Firestore document.
    // Contributors: Tanmay Dewangan
    public static void writeDataToFirestore(Firestore db, String collectionName, String documentId, Map<String, Object> data) {
        try {
            // Merging existing document data with new data to prevent overwrites.
            Map<String, Object> dataCopy = new HashMap<>(data);
            dataCopy.putAll(getDocumentDataFromFirestore(db, collectionName, documentId));

            // Write the data to the Firestore document.
            DocumentReference docRef = db.collection(collectionName).document(documentId);
            ApiFuture<WriteResult> result = docRef.set(dataCopy);
            System.out.println("Write successful with timestamp: " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately.
            System.err.println("Error writing document: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
