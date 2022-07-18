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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pc
 */
public class Produit {
    private MongoDatabase database;
    private MongoClient mongoClient;

    public Produit() {
        connexion();
    }
    
 
    private void connexion(){
        try {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
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
                    System.out.println(i.toJson());
                }

            } finally {
                cursor.close();
            }
    }
    public MongoCursor getAll(){
        //connexion();
        MongoCollection collection = database.getCollection("produit");
        MongoCursor cursor = collection.find().iterator();
        return cursor;
    }
    public void insert(Document produit3) {
        //Insert a document into the "product" collection.
        MongoCollection collection = database.getCollection("produit");   
        produit3.append("ref", "prod33").append("libelle", "produit33").append("quantite", 105).append("categorie",new Document().append("code","categ66").append("libelle","categorie66"));
        Categorie c = new Categorie();
        if(produit3.containsKey("categorie")){
            Document cat = (Document) produit3.get("categorie");
            //System.out.println(cat.toJson());
            MongoCursor mc = c.getAll();
            if(!mc.hasNext()){
                System.out.println("ici");
                cat.append("produits",new ArrayList().add(produit3));
                c.insertSolo(cat);
               // System.out.println(cat);
            } else{
                while (mc.hasNext()) {
                    Document k = (Document) mc.next();
                    if (cat.get("code").equals(k.get("code"))) {
                        System.out.println("action a realiser");
                    } else {
                        cat.append("produits",new ArrayList());
                        c.insertSolo(cat);
                        System.out.println("bibi");
                    }
                }
            }
        }
        try {
            collection.insertOne(produit3);
            System.out.println("Documents inserer. \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Document acev ce id existe deja");
            }
        }
    }
    //cette methode permet d'inserer un document dans la base de donne
    public void add(Document p) {
        MongoCollection collection = database.getCollection("produit");
        try {
            collection.insertOne(p);
            System.out.println("ajout Bingo");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Document with that id already exists");
            }
        }
    }
    
    public void update(){
        MongoCollection collection = database.getCollection("produit");
        //up = new Document();
        Document up = (Document) collection.find(Filters.eq("libelle", "poulet")).first();
        System.out.println(up.toJson());

        collection.updateOne(new Document("libelle", "poulet"), new Document("$set", new Document("quantite", 110)));

        System.out.println("\nUpdated ");
        Document dilbert = (Document) collection.find(Filters.eq("libelle", "poulet")).first();
        System.out.println(dilbert.toJson());
    }
    
    public void updateCat(Document pro){
        MongoCollection collection = database.getCollection("produit");
        System.out.println(pro.toJson());
        collection.updateOne(new Document("ref",pro.get("ref")),new Document("$set", new Document("categorie",pro.get("categorie")) ) );
    }
    
    public void delete(){
        //Delete data
        System.out.println("\nDelete documents");
        MongoCollection collection = database.getCollection("produit");
        collection.deleteMany(Filters.gte("libelle", "poulet"));
    }
}
