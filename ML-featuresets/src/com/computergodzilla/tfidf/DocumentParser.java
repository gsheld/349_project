/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computergodzilla.tfidf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Class to read documents
 *
 * @author Mubin Shrestha
 */
public class DocumentParser {

    //This variable will hold all terms of each document in an array.
    //private List<String[]> termsDocsArray = new ArrayList<String[]>();
    //private List<String> allTerms = new ArrayList<String>(); //to hold all terms
    private HashMap<String, double[]> tfidfDocsMap = new HashMap<>();
    private HashMap<String, Double> totalTermDocFreq = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> wordMap = new HashMap<>();
    private String[] queryTerms;

    /**
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        for (File f : allfiles) {
            if (f.getName().endsWith(".csv") && f.length() < (52428800*6.5) ) {
                //System.out.println(f.getName());
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[^a-zA-Z\\\\d]+", " ").split("\\W+");   //to get individual terms
                queryTerms = "cancer cases america".split(" ");
                /*for (String queryWord : queryTerms) {
                        if (!allTerms.contains(term)) {  //avoid duplicate entry
                            allTerms.add(term);
                        }
                    }*/
                HashMap<String, Integer> localMap = new HashMap<>();
                for (String term : tokenizedTerms) {
                    
                        //if (queryWord.equals(term.toLowerCase())) {
                            if (!localMap.containsKey(term.toLowerCase())) {
                                localMap.put(term.toLowerCase(), 1);
                            }
                            else {
                                Integer currentCount = localMap.get(term.toLowerCase());
                                localMap.put(term.toLowerCase(), ++currentCount);
                            }
                        
                    
                }
                wordMap.put(f.getName(), localMap);
//                termsDocsArray.add(tokenizedTerms);                
            }
        }

        /*for (Entry entry : wordMap.entrySet()) {
            System.out.println(entry);
        }*/
    }

    /**
     * Method to create termVector according to its tfidf score.
     */
    public void tfIdfCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency     
        double localTotalFreq = 0.0;
        for (String localKey : wordMap.keySet()) {
            double[] tfidfvectors = new double[queryTerms.length];
            int count = 0;
            for (String terms : queryTerms) {
                //System.out.println("ready to calc tfidf...");
                tf = new TfIdf().tfCalculator(wordMap.get(localKey), terms);
                idf = new TfIdf().idfCalculator(wordMap.get(localKey), terms);
                tfidf = tf * idf;
                localTotalFreq += tf;
                //System.out.println(terms+" tf = "+tf+" idf = "+idf);
                tfidfvectors[count] = tfidf;
                count++;
            }
            //System.out.println(Double.toString(localTotalFreq));
            //totalTermDocFreq.add(localTotalFreq);
            totalTermDocFreq.put(localKey, localTotalFreq);
            localTotalFreq = 0.0;
            //tfidfDocsMap.add(tfidfvectors);  //storing document vectors;            
            tfidfDocsMap.put(localKey, tfidfvectors);
        }
        System.out.println("here...\n");
        for (String j: tfidfDocsMap.keySet()) {
                System.out.println("Document " + j + "  =  " + tfidfDocsMap.get(j) + 
                        " " + totalTermDocFreq.get(j));
            }
    }

    /**
     * Method to calculate cosine similarity between all the documents.
     */
    /*public void getCosineSimilarity() {
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                                   + new CosineSimilarity().cosineSimilarity
                                       (
                                         tfidfDocsVector.get(i), 
                                         tfidfDocsVector.get(j)
                                       )
                                  );
            }
        }
    }*/
}
