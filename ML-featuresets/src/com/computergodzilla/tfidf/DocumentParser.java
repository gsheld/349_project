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
import java.util.List;

/**
 * Class to read documents
 *
 * @author Mubin Shrestha
 */
public class DocumentParser {

    //This variable will hold all terms of each document in an array.
    private List<String[]> termsDocsArray = new ArrayList<String[]>();
    private List<String> allTerms = new ArrayList<String>(); //to hold all terms
    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
    private List<double[]> totalTermDocFreq = new ArrayList<double[]>();

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
            if (f.getName().endsWith(".csv")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", " ").split("\\W+");   //to get individual terms
                String[] queryTerms = "cancer cases america".split(" ");
                for (String term : queryTerms) {
                    if (!allTerms.contains(term)) {  //avoid duplicate entry
                        allTerms.add(term);
                    }
                }
                termsDocsArray.add(tokenizedTerms);
            }
        }

    }

    /**
     * Method to create termVector according to its tfidf score.
     */
    public void tfIdfCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency     
        double localTotalFreq = 0.0;
        double[] localTotalTermDocFreq = new double[1];
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = new TfIdf().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                localTotalFreq += tf;
                //System.out.println(terms+" tf = "+tf+" idf = "+idf);
                tfidfvectors[count] = tfidf;
                count++;
            }
            localTotalTermDocFreq[0] = localTotalFreq;
            System.out.println(Arrays.toString(localTotalTermDocFreq));
            totalTermDocFreq.add(localTotalTermDocFreq);
            localTotalFreq = 0.0;
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;            
        }
        //System.out.println("here...\n");
        for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("Document " + j + "  =  " + Arrays.toString(tfidfDocsVector.get(j)) + 
                        " " + Arrays.toString(totalTermDocFreq.get(j)));
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
