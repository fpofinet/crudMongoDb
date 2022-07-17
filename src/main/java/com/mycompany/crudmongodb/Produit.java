/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crudmongodb;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCursor;

/**
 *
 * @author pc
 */
public class Produit {
    private MongoDatabase database;
    private MongoClient mongoClient;
    
   public void run(){
       connexion();
       //insert();
       //update();
       //delete();
       //showAll();
       //getAll();
       
       mongoClient.close();
   }
    public void connexion(){
        try {
            // 
            System.out.println("Hello World!");
            // Connect to MongoDB Server on localhost, port 27017 (default)
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
                // Connect to Database "cartoon"
             database = mongoClient.getDatabase("magasin");
                System.out.println("Successful database connection established. \n");

        } catch (Exception exception) {
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
    public void showAll(){
        MongoCollection collection = database.getCollection("produit");
        System.out.println("Print the documents.");

            MongoCursor cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    Document i = (Document)cursor.next();
                    System.out.println(i.get("ref"));
                }

            } finally {
                cursor.close();
            }
    }
    public MongoCursor getAll(){
        connexion();
        MongoCollection collection = database.getCollection("produit");
      
        MongoCursor cursor = collection.find().iterator();
        //cursor.close();
        
        return cursor;
    }
    public void insert() {
        //Insert a document into the "characters" collection.
        MongoCollection collection = database.getCollection("produit");

        Document produit3 = new Document();
        Document produit4 = new Document();
        produit3.append("ref", "prod3").append("libelle", "chicha").append("quantite", 105);
        produit4.append("ref", "prod4").append("libelle", "malboro").append("quantite", 105);
        try {
            collection.insertOne(produit3);
            collection.insertOne(produit4);
            System.out.println("Successfully inserted documents. \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Document with that id already exists");
            }
        }
    }
    
    public void update(){
        MongoCollection collection = database.getCollection("produit");
        //up = new Document();
         Document up = (Document)collection.find(Filters.eq("libelle", "poulet")).first();
            System.out.println(up.toJson());

            collection.updateOne(new Document("libelle","poulet"),new Document("$set", new Document("quantite", 110) ) );

            System.out.println("\nUpdated third document:");
            Document dilbert = (Document)collection.find(Filters.eq("libelle", "poulet")).first();
            System.out.println(dilbert.toJson());
    }
    
    public void delete(){
        //Delete data
            System.out.println("\nDelete documents with an id greater than or equal to 4.");
            MongoCollection collection = database.getCollection("produit");
            collection.deleteMany(Filters.gte("libelle","poulet"));
    }
}
