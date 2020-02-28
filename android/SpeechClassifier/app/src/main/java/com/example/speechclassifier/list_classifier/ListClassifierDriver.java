package com.example.speechclassifier.list_classifier;

import java.util.List;

public interface ListClassifierDriver{

    /**
     * Stores the information for a single list entity's icon
     */
    class ListEntity{

        private String entity;
        private String image;

        /**
         * Constructor for a list entity object
         * @param entity the text for the given entity
         * @param image the name an associated image
         */
        public ListEntity(String entity, String image){
            this.entity = entity;
            this.image = image;
        }

        public String getEntity(){
            return this.entity;
        }

        public String getImage(){
            return this.image;
        }

        public String getImageURL(){
            return "https://storage.googleapis.com/livox-images/full/" + this.image + ".png";
        }
    }

    /**
     * Returns if the given phrase can be classified
     * @param phrase the String phrase to be classified
     * @return true if the phrase can be classified, false otherwise
     */
    public boolean isQuestion(String phrase);

    /**
     * Extracts a list of ListEntity objects from the given phrase
     * returns null if an error occurs during parsing.
     *
     * @param phrase the phrase to be parsed
     * @return a list of ListEntities representing the extracted entities and associated images
     */
    public List<ListEntity> getListEntities(String phrase);

}