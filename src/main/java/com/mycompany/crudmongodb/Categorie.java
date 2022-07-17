/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crudmongodb;

/**
 *
 * @author pc
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.*;
import com.mongodb.client.MongoCursor;


public class Categorie {
    private MongoDatabase database;
    private MongoClient mongoClient;
    
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
    public void run(){
        connexion();
        insert();
        //update();
        //delete();
    }
    
    public void insert() {
        MongoCollection collection = database.getCollection("categorie");

        List prods1 = new ArrayList();
        List prods2 = new ArrayList();

        Document produit1 = new Document();
        Document produit2 = new Document();
        Document produit3 = new Document();
        Document produit4 = new Document();

        produit1.append("ref", "prod1").append("libelle", "poulet").append("quantite", 25);
        produit2.append("ref", "prod2").append("libelle", "parfun").append("quantite", 11);
        produit3.append("ref", "prod3").append("libelle", "chicha").append("quantite", 105);
        produit4.append("ref", "prod4").append("libelle", "malboro").append("quantite", 105);

        Document categ1 = new Document();
        Document categ2 = new Document();

        prods1.add(produit1);
        prods1.add(produit2);
        prods2.add(produit3);
        prods2.add(produit4);

        categ1.append("code", "categ1").append("libelle", "categorie1").append("produits", prods1);
        categ2.append("code", "categ2").append("libelle", "categorie2").append("produits", prods2);

        try {
            collection.insertOne(categ1);
            collection.insertOne(categ2);
            System.out.println("Successfully inserted documents. \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Document with that id already exists");
            }
        }
    }
    
    public void update(){
        MongoCollection collection = database.getCollection("categorie");
        //up = new Document();
        Document up = (Document)collection.find(Filters.eq("code", "categ1")).first();
        System.out.println(up.toJson());
        collection.updateOne(new Document("code","categ1"),new Document("$set", new Document("libelle","produit public") ) );
        System.out.println("\nUpdated categ:");
        Document dilbert = (Document)collection.find(Filters.eq("code", "categ1")).first();
        System.out.println(dilbert.toJson());
    }
    
    public void delete(){
        //Delete data
            System.out.println("\nDelete documents with an id greater than or equal to 4.");
            MongoCollection collection = database.getCollection("categorie");
            collection.deleteOne(Filters.gte("code","categ1"));
            System.out.println("del done");
    }
}
