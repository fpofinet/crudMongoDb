/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.crudmongodb;

import java.util.*;
import org.bson.Document;


/**
 *
 * @author pc
 */
public class CrudMongoDb {
    
    public static void main(String[] args) {
       Produit p = new Produit();
       Document produit1 = new Document();
       Document produit2 = new Document();
       Document produit3 = new Document();
       produit2.append("ref", "prod11").append("libelle", "sandale").append("quantite", 11);
       produit1.append("ref", "prod12").append("libelle", "chaussure").append("quantite", 20);
       produit3.append("ref", "prod3").append("libelle", "chicha").append("quantite", 105);
      
       p.insert(produit1);
       //p.run();
       
       Categorie c = new Categorie();
       Document categ1 = new Document();
      
      List prods = new ArrayList();
       prods.add(produit1);
       prods.add(produit2);
       prods.add(produit3);
       categ1.append("code", "categ5").append("libelle", "categorie5").append("produits",prods);
       c.insert(categ1);
    }
}
