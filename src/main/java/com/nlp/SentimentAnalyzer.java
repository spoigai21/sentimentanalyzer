package com.nlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.Scanner;

import java.util.Properties;


public class SentimentAnalyzer {
    StanfordCoreNLP pipeline;
    private static final String ANNOTATORS = "tokenize, ssplit, pos, parse, sentiment";

    SentimentAnalyzer() {
        Properties properties = new Properties();
        properties.setProperty("annotators", ANNOTATORS);
        pipeline = new StanfordCoreNLP(properties);
    }

    public static void main(String[] args){
        SentimentAnalyzer analyzer = new SentimentAnalyzer();
        Scanner input = new Scanner(System.in);
        String text = input.nextLine();
        analyzer.estimatingSentiment(text);
    }

    public void estimatingSentiment(String text)
    {
        int sentimentInt;
        String sentimentName;
        Annotation annotation = pipeline.process(cleanText(text));
        for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
        {
            Tree tree = sentence.get(SentimentAnnotatedTree.class);
            sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
            sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentimentName + "\t" + sentimentInt + "\t" + sentence);
        }
    }

    private String cleanText(String text) {
       return  text.trim()
                // remove links
                .replaceAll("http.*?[\\S]+", "")
                // remove usernames
                .replaceAll("@[\\S]+", "")
                // replace hashtags by just words
                .replaceAll("#", "")
                // correct all multiple white spaces to a single white space
                .replaceAll("[\\s]+", " ");
    }
}
