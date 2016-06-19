package edu.stanford.cs193a.sgalleg9.madlibs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Santi Gallego on 6/19/16.
 **/

public class Story {

    private String rawStory, story;
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<Integer> startLocations = new ArrayList<Integer>();

    public Story() {
        //
    }

    public Story(Scanner s) {

        read(s);
        findPlaceHolders();
    }

    public void read(Scanner s) {

        rawStory = s.nextLine();
        story = rawStory;
    }

    public void findPlaceHolders() {

        int startOfType;
        int endOfType = 0;

        while(rawStory.indexOf('<', endOfType) > -1) {
            startOfType = rawStory.indexOf('<', endOfType);
            endOfType = rawStory.indexOf('>', startOfType);

            String inputType = rawStory.substring(startOfType + 1, endOfType);

            type.add(inputType);
            startLocations.add(startOfType);
        }
    }

    public void setPlaceholder(int index, String text) {
        int startOfType = startLocations.get(index);
        String replaceText = "<" + type.get(index) + ">";

        story.replace(replaceText, text);
    }

    public int getPlaceholderCount() {
        return type.size();
    }

    public String getPlaceholder(int index) {
        return type.get(index);
    }

    public String getStory() {
        return story;
    }
}
