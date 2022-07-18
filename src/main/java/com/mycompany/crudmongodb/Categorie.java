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

    public Categorie() {
        connexion();
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
    /*public void run(){
       // connexion();
        //insert();
        //update();
        //delete();
    }*/
    
    public MongoCursor getAll(){
        MongoCollection collection = database.getCollection("categorie");
        MongoCursor cursor = collection.find().iterator();
        return cursor;
    }
    
    public void insertSolo(Document categ){
        MongoCollection collection = database.getCollection("categorie"); 
        try {
            collection.insertOne(categ);
            System.out.println("cat ajouter \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("le document avec ce existe deja");
            }
        }
    }
    public void insert(Document categ) {
        MongoCollection collection = database.getCollection("categorie");

       /* List prods1 = new ArrayList();
        List prods2 = new ArrayList();

        Document produit1 = new Document();
        Document produit2 = new Document();
        Document produit3 = new Document();
        Document produit4 = new Document();

        //produit1.append("ref", "prod5").append("libelle", "poisson").append("quantite", 110);
        produit2.append("ref", "prod2").append("libelle", "parfun").append("quantite", 11);
        produit3.append("ref", "prod3").append("libelle", "chicha").append("quantite", 105);
        produit4.append("ref", "prod4").append("libelle", "malboro").append("quantite", 105);

        Document categ1 = new Document();
        Document categ2 = new Document();

        //prods1.add(produit1);
        prods1.add(produit2);
        prods2.add(produit3);
        prods2.add(produit4);
   
        categ1.append("code", "categ3").append("libelle", "categorie3").append("produits", prods1);
        categ2.append("code", "categ2").append("libelle", "categorie2").append("produits", prods2);*/
        
        Produit pro = new Produit();
        //System.out.println("ici");
        
        try {
            //on verifier si la nouvelle categorie avec des produits
            if(categ.containsKey("produits")){
                //on recupere la liste de tout les produits de la collection produit
                MongoCursor mc = pro.getAll();
                //on recupere tout les produit du champ produit de la nouvelle categorie qu'on veut inserer
                ArrayList p=(ArrayList)categ.get("produits");
                
                for (int i = 0; i < p.size(); i++) {
                    Document j = (Document) p.get(i);
                    while (mc.hasNext()) {
                        Document k = (Document) mc.next();
                        //on verifie si les produit existe deja dans la base de donnees. si oui on ajoute un champs categorie au produit exitant
                        // si non on insert de nouveau produit dans la base de donnes avec la categorie correspondante
                        if (j.get("ref").equals(k.get("ref"))) {
                            Document upd=k;
                            upd.append("categorie", new Document().append("code", categ.get("code")).append("libelle", categ.get("code")));
                            pro.updateCat(k);
                        } else{
                            Document newProd =j;
                            newProd.append("categorie", new Document().append("code", categ.get("code")).append("libelle", categ.get("code")));
                            pro.add(newProd);
                        }
                        //System.out.println(k.get("ref"));
                    }
                    //System.out.println(j.toJson());
                }
            }
           
            collection.insertOne(categ);
            //collection.insertOne(categ2);
            System.out.println("cat ajouter \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("le document existe deja");
            }
        }
    }
    
    public void update(){
        MongoCollection collection = database.getCollection("categorie");
        
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
