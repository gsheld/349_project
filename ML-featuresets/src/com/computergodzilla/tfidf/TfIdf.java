/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computergodzilla.tfidf;

import java.util.HashMap;
import java.util.List;

//<editor-fold defaultstate="collapsed" desc="TFIDF calculator">
/**
 * Class to calculate TfIdf of term.
 * @author Mubin Shrestha
 */
public class TfIdf {
    
    //<editor-fold defaultstate="collapsed" desc="TF Calculator">
    /**
     * Calculated the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(HashMap<String, Integer> totalTerms, String termToCheck) {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalTerms.keySet()) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        double totalLength = 0.0;
        for (String s : totalTerms.keySet()) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
            totalLength += totalTerms.get(s);
        }
        //System.out.println("count = "+count+"total terms = "+totalterms.length);
        return count / totalLength;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Idf Calculator">
    /**
     * Calculated idf of term termToCheck
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(HashMap<String, Integer> allTerms, String termToCheck) {
        double count = 0;
        int flag = 1;
        for (String ss : allTerms.keySet()) {
            //for (String s : ss) {
                if (ss.toLowerCase().contains(termToCheck.toLowerCase())) {
                    count++;
                    /*if (flag == 1) {
                        //System.out.println(s);
                        flag = 0;
                    }*/
                    break;
                }
            //}
        }
        //System.out.println("no of docs = "+allTerms.size());
        return Math.log(allTerms.size() / count);
    }
//</editor-fold>
}
//</editor-fold>
