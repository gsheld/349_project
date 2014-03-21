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
    private HashMap<String, Double> synTotalTermFreq = new HashMap<>();
    private HashMap<String, Double> semanticMatch = new HashMap<>();
    private HashMap<String, Double> synSemanticMatch = new HashMap<>();
    private HashMap<String, Integer> numberOfColumns = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> wordMap = new HashMap<>();
    private String[] queryTerms;
    private String[] synTerms;

    /**
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void parseFiles(File f) throws FileNotFoundException, IOException {
        //File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        int flag;
        //for (File f : allfiles) {
            if (f.getName().endsWith(".csv") && f.length() < (52428800*6.5)) {
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
                //queryTerms = "state healthcare cost".split(" ");
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
        

        
        /*for (Entry entry : wordMap.entrySet()) {
            System.out.println(entry);
        }*/
    }
    
    public void dataComputation (String filePath, String labeledQueriesFile, String synonymFile) throws FileNotFoundException, IOException {
        File qfile = new File(labeledQueriesFile);
        FileReader qfileReader = new FileReader(qfile);
        BufferedReader qbufferedReader = new BufferedReader(qfileReader);
        
        File synfile = new File(synonymFile);
        FileReader fr = new FileReader(synfile);
        BufferedReader br = new BufferedReader(fr);
        
        File datafile = new File("rankerData.txt");
        FileWriter datafileWriter = new FileWriter(datafile);
        BufferedWriter dataBufferedWriter = new BufferedWriter(datafileWriter);
        
        File[] allfiles = new File(filePath).listFiles();
        
        String dataLine;
        int qIndex = 1;
        int rank = 0;
        String labeledQuery = null;
        String synQuery = null;
        String fileName = null;
        String trackedQuery = "als cases";
        String[] labeledParts = trackedQuery.split(",");
        File singleTable = allfiles[0];
        
        while ((labeledQuery = qbufferedReader.readLine()) != null) {
            synQuery = br.readLine();
            if (synQuery != null) {
                synTerms = synQuery.split(" ");
            }
            labeledParts = labeledQuery.split(", ");
            queryTerms = labeledParts[0].split(" ");
            fileName = labeledParts[1];
            rank = Integer.parseInt(labeledParts[2]);
            
            for (File f : allfiles) {
                if(f.getName().equalsIgnoreCase(labeledParts[1])) {
                    singleTable = f;
                    break;
                }
            }
            
            parseFiles(singleTable);
            
            semanticMatch.put(singleTable.getName(), semanticQueryMatch(singleTable.getName(), queryTerms)); //semantic tablename match
            if (synQuery != null) {
                synSemanticMatch.put(singleTable.getName(), semanticQueryMatch(singleTable.getName(), synTerms));
            }
            
                if (labeledParts[0].equalsIgnoreCase(trackedQuery)) {
                    dataLine = null;
                    if(wordMap.containsKey(singleTable.getName())) {
                        dataLine = tfIdfCalculator(rank, labeledParts[0], qIndex, singleTable.getName(), synQuery);
                        dataBufferedWriter.write(dataLine);
                    }
                } 
                else {
                    trackedQuery = labeledParts[0];
                    qIndex++;
                    dataLine = null;
                    if(wordMap.containsKey(singleTable.getName())) {
                        dataLine = tfIdfCalculator(rank, labeledParts[0], qIndex, singleTable.getName(), synQuery);
                        dataBufferedWriter.write(dataLine);
                    }
                }
        }
        qbufferedReader.close();
        dataBufferedWriter.close();
    }

    /**
     * Method to create termVector according to its tfidf score.
     */
    public String tfIdfCalculator(int rank, String query, int qId, String fileName, String synQuery) throws IOException {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency     
        double localTotalFreq = 0.0;
        double synTotalFreq = 0.0;
        double synTF;
        //for (String localKey : wordMap.keySet()) {
            double[] tfidfvectors = new double[queryTerms.length];
            if (synQuery != null) {
                for (String term : synTerms) {
                    synTF = new TfIdf().tfCalculator(wordMap.get(fileName), term);
                    synTotalFreq += synTF;
                }
                synTotalTermFreq.put(fileName, synTotalFreq);
            }
            int count = 0;
            for (String terms : queryTerms) {
                //System.out.println("ready to calc tfidf...");
                tf = new TfIdf().tfCalculator(wordMap.get(fileName), terms);
                idf = new TfIdf().idfCalculator(wordMap.get(fileName), terms);
                tfidf = tf * idf;
                localTotalFreq += tf;
                //System.out.println(terms+" tf = "+tf+" idf = "+idf);
                tfidfvectors[count] = tfidf;
                count++;
            }
            //System.out.println(Double.toString(localTotalFreq));
            //totalTermDocFreq.add(localTotalFreq);
            totalTermDocFreq.put(fileName, localTotalFreq);
            localTotalFreq = 0.0;
            //tfidfDocsMap.add(tfidfvectors);  //storing document vectors;            
            tfidfDocsMap.put(fileName, tfidfvectors);
        
        //File file = new File("state healthcare cost.txt");
        //FileWriter fileWriter = new FileWriter(file);
        //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        int localFeatureIndex;
        //System.out.println("here...\n");
        //for (String j: tfidfDocsMap.keySet()) {
                //System.out.println(fileName + "  =  " + Arrays.toString(tfidfDocsMap.get(fileName)) + 
                //        " " + Double.toString(totalTermDocFreq.get(fileName)) + " " + Double.toString(semanticMatch.get(fileName)) +
                //        " " + Integer.toString(numberOfColumns.get(fileName)));
                StringBuilder mySB = new StringBuilder();
                localFeatureIndex = 0;
                mySB.append(Integer.toString(rank));
                mySB.append(" ");
                mySB.append("qid:");
                mySB.append(Integer.toString(qId));
                mySB.append(" ");
                for (Double termTfIdf : tfidfDocsMap.get(fileName)) {
                    mySB.append(Integer.toString(localFeatureIndex));
                    localFeatureIndex++;
                    mySB.append(":");
                    mySB.append(Double.toString(termTfIdf));
                    mySB.append(" ");
                }
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Double.toString(totalTermDocFreq.get(fileName)));
                mySB.append(" ");
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Double.toString(semanticMatch.get(fileName)));
                mySB.append(" ");
                mySB.append(Integer.toString(localFeatureIndex));
                localFeatureIndex++;
                mySB.append(":");
                mySB.append(Integer.toString(numberOfColumns.get(fileName)));
                mySB.append(" ");
                if (synQuery != null) {
                    mySB.append(Integer.toString(localFeatureIndex));
                    localFeatureIndex++;
                    mySB.append(":");
                    mySB.append(Double.toString(synTotalTermFreq.get(fileName)));
                    mySB.append(" ");
                    mySB.append(Integer.toString(localFeatureIndex));
                    localFeatureIndex++;
                    mySB.append(":");
                    mySB.append(Double.toString(synSemanticMatch.get(fileName)));
                    mySB.append(" ");
                }
                mySB.append(" # ");
                mySB.append("'");
                mySB.append(query);
                mySB.append("'");
                mySB.append(" ");
                mySB.append(fileName);
                mySB.append("\n");
                System.out.println(mySB.toString());
                return mySB.toString();
                //bufferedWriter.write(mySB.toString());
            
        //bufferedWriter.close();
        //return "";
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
