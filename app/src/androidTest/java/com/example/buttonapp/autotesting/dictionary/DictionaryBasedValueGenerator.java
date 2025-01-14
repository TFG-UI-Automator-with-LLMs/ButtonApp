package com.example.buttonapp.autotesting.dictionary;


import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DictionaryBasedValueGenerator {
    public static final int DEFAULT_NUMBER_OF_WORDS=3;
    public static final String[] IGNORED_WORDS= {"is","of","and","where","this","that"};


    private Dictionary dict;
    public static POS defaultPOS=null;
    protected int maxWords;
    protected int minWords;
    protected Random randomGenerator;


    public DictionaryBasedValueGenerator()
    {
        this(DEFAULT_NUMBER_OF_WORDS,DEFAULT_NUMBER_OF_WORDS, new Random().nextLong());
    }
    public DictionaryBasedValueGenerator(int words, Long seed) {
        this(words,words, seed);
    }



    public DictionaryBasedValueGenerator(int minWords,int maxWords, Long seed) {
        this.minWords=minWords;
        this.maxWords = maxWords;
        this.randomGenerator = new Random(seed);

    }

    //@Override
    public Object generate() throws JWNLException {
        List<POS> poses=POS.getAllPOS();
        POS pos=defaultPOS;
        IndexWord word;
        StringBuilder sb=new StringBuilder();
        int nWords=minWords;
        if(randomGenerator==null)
            randomGenerator=new Random();
        if(minWords!=maxWords)
            nWords+=randomGenerator.nextInt(maxWords-minWords);
        try {
            if(dict==null)
                dict=Dictionary.getDefaultResourceInstance();
            for(int i=0;i<nWords;i++){
                if(defaultPOS==null)
                    pos=poses.get(randomGenerator.nextInt(poses.size()));
                dict.setRandom(randomGenerator);
                word=dict.getRandomIndexWord(pos);
                if(!shouldBeIgnored(word.getLemma()))
                    sb.append(word.getLemma());
                else
                    i--;
                if(i!=nWords-1)
                    sb.append(" ");
            }
        } catch (JWNLException ex) {
            Logger.getLogger(DictionaryBasedValueGenerator.class.getName()).log(Level.SEVERE, "Unable to instantiate dictionary!!!", ex);
        }
        return sb.toString();
    }

    public int getMaxWords() {
        return maxWords;
    }

    public int getMinWords() {
        return minWords;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    public void setMinWords(int minWords) {
        this.minWords = minWords;
    }

    @SuppressWarnings("unlikely-arg-type")
    private boolean shouldBeIgnored(String word)
    {
        boolean result=false;
        for(int i=0;i<IGNORED_WORDS.length && !result;i++)
            if(IGNORED_WORDS.equals(word))
                result=true;
        return result;
    }

}