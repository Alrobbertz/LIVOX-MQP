package com.example.speechclassifier.list_classifier.entityparser;

import android.content.Context;
import android.util.Log;
import java.util.Dictionary;
import java.util.List;
import java.util.LinkedList;


/**
 * Entity Phrase Parser
 *
 * Given a string of words, this class returns the most likely combination of entities,
 * including single word and compound words, interfaces through parse function
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 *
 */
public class EntityPhraseParser {

    private static final String TAG = "EntityPhraseParser";
    public EntityScore entityScore;
    public Dictionary STOP_WORDS;

    public EntityPhraseParser(Context context) {
        STOP_WORDS = StopWords.initializeVocabulary(context);
        Log.d(TAG, STOP_WORDS.toString());
        entityScore = new EntityScore(context);

    }

    /**
     * EntityPhrase
     *
     * container class for processing the contents of the entityphrase
     * this section of the phrase is where the list of entities should be contained
     */
    public class EntityPhrase {
        private String entityPhrase;
        private int ngramThreshold;
        private List<String> filteredEntityPhrase;

        /**
         * constructor for EntityPhrase
         * @param entityPhrase phrase to parse entities
         * @param ngramThreshold ngram limit threshold to search for entities(2 = bi-gram such as "hot dog", 3 = tri-gram such as "chocolate chip cookies")
         */
        public EntityPhrase(String entityPhrase, int ngramThreshold) {
            this.entityPhrase = entityPhrase;
            this.ngramThreshold = ngramThreshold;
            this.filteredEntityPhrase = new LinkedList<String>();
        }

        public EntityPhrase(String entityPhrase) {
            this(entityPhrase, 2);
        }

        /**
         * pre-process entityPhrase adding to filteredEntityPhrase List class object
         */
        public void cleanEntityPhrase() {
            String cleanPhrase = this.entityPhrase.replace(" and ", "_and_");
            String[] words = cleanPhrase.split("\\s");
            for (String word: words) {
                if(STOP_WORDS.get(word) == null) {
                    filteredEntityPhrase.add(word);
                }
            }
        }


        /**
         * get possible ngram combinations for the ngram-threshold
         * @return list of list of entities at each possible ngram size
         */
        public List<List<String>> ngrams() {
            List entityCombinations = new LinkedList<List<String>>();
            for(int i=1; i <= this.ngramThreshold;i++) {
                entityCombinations.add(this.ngramOfSize(i));
            }
            return entityCombinations;
        }


        /**
         * get all possible ngrams for ngrams up until the given ngram-size
         * @param ngramSize
         * @return list of ngrams of size N
         */
        private List<String> ngramOfSize(int ngramSize) {
            int phraseLength = filteredEntityPhrase.size();
            if (phraseLength < ngramSize) {
                ngramSize = phraseLength;
            }
            List<String>  ngrams = new LinkedList<String>();
            for(int i=0;i < (phraseLength - (ngramSize - 1));i++) {
                String entity = "";
                for(int j=0; j < ngramSize; j++) {
                    entity += this.filteredEntityPhrase.get(i + j) + " ";
                }
                entity = entity.substring(0, entity.length() - 1);
                ngrams.add(entity);
            }
            return ngrams;
        }

        /**
         * given ordered list of ngrams, find all possible combinations of the entities
         * @param ngrams list of list of ngram combinations
         * @return all possible combinations
         */
        private List<List<String>> entityCombinations(List<List<String>> ngrams) {
            List<List<String>> combinations = this.entityCombinationsHelper(0,
                    this.filteredEntityPhrase.size(), ngrams, new LinkedList<String>());
            Log.d(TAG, combinations.toString());
            return combinations;
        }


        private List<List<String>> entityCombinationsHelper(int startingPoint, int phraseLength,
                                                           List<List<String>> words, List<String> path) {
            List<List<String>> combinations = new LinkedList<List<String>>();
            if (startingPoint == phraseLength) {
                List<List<String>> return_path = new LinkedList<List<String>>();
                return_path.add(path);
                return return_path;
            }
            for(int gramCount=0; gramCount < this.ngramThreshold;gramCount++) {
                if ((startingPoint + (gramCount + 1)) <= phraseLength) {
                    List<String> newWord = new LinkedList<String>();
                    newWord.add(words.get(gramCount).get(startingPoint));
                    List<String> newPath = new LinkedList<String>();
                    newPath.addAll(path);
                    newPath.addAll(newWord);
                    List<List<String>> newCombinations = this.entityCombinationsHelper(
                            (startingPoint + (gramCount + 1)), phraseLength, words, newPath);
                    combinations.addAll(newCombinations);
                }
                else {
                    return combinations;
                }
            }
            return combinations;
        }

        /**
         * get the most likely combination of entities using entity scoring
         * @param entList list of list of all possible entity combinations
         * @return list of string most likely ngram combination
         */
        public List<String> bestEntities(List<List<String>> entList) {
            List<Double> scores = new LinkedList<Double>();
            for (List<String> entities:entList) {
                double combSum = 0.0;
                for(String entity: entities) {
                    combSum += entityScore.getNgramScore(entity);
                }
                scores.add(combSum/ (entities.size()));
            }
            int maxIndex = 0;
            double max = Double.NEGATIVE_INFINITY;
            for(int i=0; i < entList.size();i++) {
                if(scores.get(i) > max) {
                    maxIndex = i;
                    max = scores.get(i);
                }
            }
            List<String> bestComb = entList.get(maxIndex);
            for(int i=0;i < bestComb.size();i++) {
                bestComb.set(i, (bestComb.get(i).replace(" and ", "_and_")));
            }
            return bestComb;
        }



    }


    /**
     * get most likely combination of entities from an entity phrase (a hot dog, hamburger or sandwich -> [hot dog, hamburger, sandwich]
     * @param entityPhrase phrase of list portion of phrase
     * @param ngramThreshold max ngram (compound word) size to search for (2 = bi-gram such as "hot dog", 3 = tri-gram such as "chocolate chip cookies")
     * @return list of entities found in the entityPhrase
     */
    public List<String> parse(String entityPhrase, int ngramThreshold) {
        EntityPhrase phraseObject = new EntityPhrase(entityPhrase, ngramThreshold);
        phraseObject.cleanEntityPhrase();
        List<String> responseParse = phraseObject.bestEntities(phraseObject.entityCombinations(phraseObject.ngrams()));
        Log.d(TAG, responseParse.toString());
        return responseParse;
    }

    /**
     * get most likely combination of entities from an entity phrase (a hot dog, hamburger or sandwich -> [hot dog, hamburger, sandwich]
     * ngramThreshold is defaulted to 2
     * @param entityPhrase phrase of list portion of phrase
     * @return list of entities found in the entityPhrase
     */
    public List<String> parse(String entityPhrase) {
        return this.parse(entityPhrase, 2);
    }

}
