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
import java.io.FileWriter;
import java.io.BufferedWriter;

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
    private HashMap<String, Double> semanticMatch = new HashMap<>();
    private HashMap<String, Integer> numberOfColumns = new HashMap<>();
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
        int flag;
        for (File f : allfiles) {
            if (f.getName().endsWith(".csv") && f.length() < (52428800*6.5) ) {
                //System.out.println(f.getName());
                in = new BufferedReader(new FileReader(f));
                flag = 1;
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    if(flag == 1) {
                        //System.out.println("file name = " + f.getName() + "row = " + s + "Row length = " + s.split(",").length);
                        numberOfColumns.put(f.getName(), s.split(",").length);
                        flag = 0;
                    }
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[^a-zA-Z\\\\d]+", " ").split("\\W+");   //to get individual terms
                queryTerms = "state healthcare cost".split(" ");
                /*for (String queryWord : queryTerms) {
                        if (!allTerms.contains(term)) {  //avoid duplicate entry
                            allTerms.add(term);
                        }
                    }*/
                
                semanticMatch.put(f.getName(), semanticQueryMatch(f.getName(), queryTerms));        //semantic tablename match
                
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
    public void tfIdfCalculator() throws IOException {
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
        File file = new File("state healthcare cost.txt");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        int localFeatureIndex;
        //System.out.println("here...\n");
        for (String j: tfidfDocsMap.keySet()) {
                System.out.println(j + "  =  " + Arrays.toString(tfidfDocsMap.get(j)) + 
                        " " + Double.toString(totalTermDocFreq.get(j)) + " " + Double.toString(semanticMatch.get(j)) +
                        " " + Integer.toString(numberOfColumns.get(j)));
                StringBuilder mySB = new StringBuilder();
                localFeatureIndex = 0;
                mySB.append("0 ");
                for (Double termTfIdf : tfidfDocsMap.get(j)) {
                    mySB.append(Integer.toString(localFeatureIndex));
                    localFeatureIndex++;
                    mySB.append(":");
                    mySB.append(Double.toString(termTfIdf));
                    mySB.append(" ");
                }
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Double.toString(totalTermDocFreq.get(j)));
                mySB.append(" ");
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Double.toString(semanticMatch.get(j)));
                mySB.append(" ");
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Integer.toString(numberOfColumns.get(j)));
                mySB.append(" # ");
                mySB.append(j);
                mySB.append("\n");
                bufferedWriter.write(mySB.toString());
            }
        bufferedWriter.close();
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
    
    public double semanticQueryMatch(String tableName, String[] query) {
        String[] tableWords = tableName.split("-|\\.");
        double count = 0.0;
        //System.out.println(tableWords);
        for(String word : tableWords) {
            for(String queryWord : query) {
                if(word.equalsIgnoreCase(queryWord)) {
                    count++;
                    //System.out.println(word);
                }
            }
        }
        //System.out.println(count);
        return count/query.length;
    }
}
