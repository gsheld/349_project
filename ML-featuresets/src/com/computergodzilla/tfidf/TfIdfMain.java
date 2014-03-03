/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computergodzilla.tfidf;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mubin Shrestha
 */
public class TfIdfMain {
    
    /**
     * Main method
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        DocumentParser dp = new DocumentParser();
        dp.parseFiles("/Users/arundhatijaswal/Documents/Q2/ML/Group Project/code/349_project/chsi_dataset/data");
        dp.tfIdfCalculator(); //calculates tfidf
        //dp.getCosineSimilarity(); //calculated cosine similarity   
    }
}
