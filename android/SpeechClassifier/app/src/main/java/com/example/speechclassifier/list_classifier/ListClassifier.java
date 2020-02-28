package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;
import com.example.speechclassifier.list_classifier.ListClassifierDriver.ListEntity;

public class ListClassifier{

    private static final String TAG = "ListClassifier";

    private static ListClassifier classifier;

    public static ListClassifier getInstance(){
        if(classifier == null )
            classifier = new ListClassifier();
        return classifier;
    }

    private String wakeword;

    private ListClassifierDriver driver;

    private String recentPhrase;
    private List<ListEntity> listEntities;

    /**
     * Default constructor for list classifier. By default uses the
     * online driver.
     */
    private ListClassifier() {
        driver = new OnlineDriver();
        listEntities = null;
        wakeword = "john";
    }

    /**
     * Returns if the given phrase can be classified
     * @param phrase the String phrase to be classified
     * @return true if the phrase can be classified, false otherwise
     */
    public boolean isClassifiable(String phrase){
        return driver.isQuestion(phrase);
    }

    /**
     * classifies the given phrase.
     * If classified, sets the internal state.
     * Internal state hold the most recent phrase, the extracted list entities, and the associated Livox images.
     *
     * @param phrase the String phrase to be classified
     * @return true if the phrase was classified, false otherwise
     */
    public boolean classify(String phrase) {
        clear();

        int wwIndex = phrase.indexOf(wakeword);
        if(wwIndex < 0)
            return false;
        String wwPhrase = phrase.substring(wwIndex + wakeword.length() + 1);

        listEntities = driver.getListEntities(wwPhrase);
        if(listEntities == null)
            return false;

        recentPhrase = phrase;
        return true;
    }

    /**
     *
     * @return True if the most recent phrase was successfully classified
     */
    public boolean isClassified(){
        return recentPhrase != null;
    }

    /**
     *  Resets the internal state of the classifier
     */
    public void clear(){
        recentPhrase = null;
        listEntities = null;
    }

    /**
     * Sets the wakeword
     *
     * @param wakeword the new wakeword
     */
    public void setWakeword(String wakeword){
        this.wakeword = wakeword;
    }

    /**
     * Sets the ListClassifier driver
     *
     * @param driver the driver to be used
     */
    public void setDriver(ListClassifierDriver driver){
        this.driver = driver;
    }

    /**
     * returns null if most recent phrase failed to be classified
     * @return the most recent phrase to be classified.
     */
    public String getPhrase(){
        if(!isClassified()){
            return null;
        }
        return recentPhrase;
    }

    /**
     * returns null if the most recent phrase failed to be classified
     * @return the set of list entities for the most recently classified phrase
     */
    public List<String> getListEntities(){
        if(!isClassified()){
            return null;
        }
        List<String> textEntities = new ArrayList<>();
        for(ListEntity le: this.listEntities){
            textEntities.add(le.getEntity());
        }
        return textEntities;
    }

    /**
     * Gets the name of the image associated with a given list entity
     * @param listEntity the list entity to find an image for
     * @return the image name of the most colsely associated image to the given list entity
     */
    public String getImageID(String listEntity){
        for(ListEntity le: listEntities){
            if(le.getEntity().equals(listEntity)){
                return le.getImage();
            }
        }
        return null;
    }
}
