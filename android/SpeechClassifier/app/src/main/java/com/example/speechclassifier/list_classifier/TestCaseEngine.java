package com.example.speechclassifier.list_classifier;

import android.content.Context;
import android.util.Log;

import com.example.speechclassifier.list_classifier.ListClassifierDriver.ListEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCaseEngine {

    private static final String TAG = "TestCases";

    public ArrayList<TestCase> testCases = new ArrayList<TestCase>(){{
        String[] responses;
        responses = new String[]{"hot dog", "hamburger", "french fries"}; add(new TestCase("what do you want for dinner the hot dog the hamburger or the french fries", responses));
        responses = new String[]{"chocolate chip cookie", "pumpkin pie", "brownie"}; add(new TestCase("would you rather have the chocolate chip cookie the pumpkin pie or a brownie", responses));
        responses = new String[]{"scrambled", "fried", "poached"}; add(new TestCase("how would you like your eggs cooked scrambled fried or poached", responses));
        responses = new String[]{"cat", "golden retriever", "bichon"}; add(new TestCase("do you want the cat golden retriever or bichon", responses));
        responses = new String[]{"park", "bowling alley", "theatre"}; add(new TestCase("where do you want to go the park bowling alley or the theatre", responses));
        responses = new String[]{"eleven", "twelve"}; add(new TestCase("what time do you want to go to dinner eleven or twelve", responses));
        responses = new String[]{"girl", "boy"}; add(new TestCase("are you a girl or boy", responses));
        responses = new String[]{"flowers", "french fries"}; add(new TestCase("would you rather only eat flowers or french fries", responses));
        responses = new String[]{"trees", "bowling alleys"}; add(new TestCase("would you rather eat trees or bowling alleys", responses));
        responses = new String[]{"happy", "sad", "angry"}; add(new TestCase("how are you feeling today happy sad or angry", responses));
        responses = new String[]{"blue", "red", "green"}; add(new TestCase("whats your favorite color blue red or green", responses));
        responses = new String[]{"lindsay lohan", "barack obama"}; add(new TestCase("whos a better singer lindsay lohan or barack obama", responses));
        responses = new String[]{"now", "later"}; add(new TestCase("do you want to go now or later", responses));
        responses = new String[]{"macaroni and cheese", "chicken"}; add(new TestCase("whats your favorite food macaroni and cheese or chicken", responses));
        //No mapping for the question word why?
        //responses = new String[]{"it reflects the water", "it secretly is water"}; add(new TestCase("why is the sky blue because it reflects the water or it secretly is water", responses));
        responses = new String[]{"Rich", "Andrew", "Cole", "Zachary"}; add(new TestCase("who benches more Rich Andrew Cole or Zachary", responses));
        responses = new String[]{"tomoto soup", "chicken soup"}; add(new TestCase("what do you want for dinner tomato or chicken soup", responses));
        responses = new String[]{"red", "green", "blue"}; add(new TestCase("is your favorite color red green or blue", responses));
        responses = new String[]{"go to the beach", "eat dinner"}; add(new TestCase("do you want to go to the beach or eat dinner", responses));
        //responses = new String[]{"NULL"}; add(new TestCase("where are you", responses));
        responses = new String[]{"macaroni and cheese", "fish and chips"}; add(new TestCase("whats your favorite food macaroni and cheese or fish and chips", responses));
        responses = new String[]{"hot dog", "pug"}; add(new TestCase("do you want a hot dog or a pug", responses));
        responses = new String[]{"hot dog", "pug"}; add(new TestCase("do you want to eat a hot dog or a pug", responses));
        //responses = new String[]{"NULL"}; add(new TestCase("do you want lunch and dessert", responses));
        responses = new String[]{"McDonalds", "salad"}; add(new TestCase("do you want to eat McDonalds or salad", responses));
        responses = new String[]{"red", "green", "blue"}; add(new TestCase("what is your favorite color red green or maybe blue", responses));
        //gives index out of bounds exception
        //responses = new String[]{"help"}; add(new TestCase("what do you need help or assistance", responses));
        //gives index out of bounds exception
        //responses = new String[]{"help", "food"}; add(new TestCase("what do you need help or assistance or food", responses));
        responses = new String[]{"hamburger", "peanut butter and jelly sandwich"}; add(new TestCase("what do you want for dinner a hamburger or a peanut butter and jelly sandwich", responses));
    }};

    private OnlineDriver onDriver;
    private OfflineDriver offDriver;

    public class TestCase{

        String phrase;
        List<String> expectedResponses;

        boolean onlineIsListQuestion;
        List<String> onlineResponses;
        List<String> onlineImages;

        boolean offlineIsListQuestion;
        List<String> offlineResponses;
        List<String> offlineImages;


        public TestCase(String phrase, String[] responses){
            this.phrase = phrase;
            this.expectedResponses = Arrays.asList(responses);
        }

        public void runTestCase(){
            onlineIsListQuestion = onDriver.isQuestion(phrase);
            List<ListEntity> onResponses = onDriver.getListEntities(phrase);
            onlineResponses = new ArrayList<String>();
            onlineImages = new ArrayList<String>();
            if( onResponses != null) {
                for (ListEntity e : onResponses) {
                    onlineResponses.add(e.getEntity());
                    onlineImages.add(e.getImage());
                }
            }
            offlineIsListQuestion = offDriver.isQuestion(phrase);
            List<ListEntity> offResponses = offDriver.getListEntities(phrase);
            offlineResponses = new ArrayList<String>();
            offlineImages = new ArrayList<String>();
            if( offResponses != null) {
                for (ListEntity e : offResponses) {
                    offlineResponses.add(e.getEntity());
                    offlineImages.add(e.getImage());
                }
            }
        }

        public String toString(){
            String line1 = "Test Case: " + this.phrase;
            String line2 = "Category (expected): online | offline";
            String line3 = "isListQuestion (True): " + this.onlineIsListQuestion + " | " + this.offlineIsListQuestion;
            String line4 = "Extracted entities (" + this.expectedResponses.toString() + "): " + this.onlineResponses.toString() + " | " + this.offlineResponses.toString();
            String line5 = "Matched Images (): " + this.onlineImages.toString() + " | " + this.offlineImages.toString();
            return line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n";
        }
    }

    public TestCaseEngine(Context context){
        this.onDriver = new OnlineDriver();
        this.offDriver = new OfflineDriver(context);
    }

    public void runTestCases(){
        for(TestCase t: this.testCases){
            t.runTestCase();
            Log.d(TAG, t.toString());
        }
    }


}
