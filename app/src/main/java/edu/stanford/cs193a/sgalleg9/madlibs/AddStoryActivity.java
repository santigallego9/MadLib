package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import stanford.androidlib.SimpleActivity;

public class AddStoryActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

    }

    public void submitButtonClick(View view) {
        EditText storyTitle = (EditText) findViewById(R.id.title_input);
        EditText storyInput = (EditText) findViewById(R.id.story_input);

        String title = storyTitle.getText().toString();
        String story = storyInput.getText().toString();

        saveStory(story, title);
    }


    public void saveStory(String story, String title) {

        ArrayList<String> errors;

        title = title.replaceAll(" ", "_");

        errors = checkStory(story, title);

        if (errors.size() == 0) {
            String filename = "madlib_" + title.toLowerCase() + ".txt";

            try {
                PrintStream out = new PrintStream(openFileOutput(filename, MODE_PRIVATE));
                out.print(story);
                out.close();
            } catch (Exception e) {
                toast("FILE NOT FOUND FOR SOME FUCKING REASON GODDAMMIT");
            }

            try {
                PrintStream out = new PrintStream(openFileOutput("user_stories.txt", MODE_APPEND));
                out.append(title + "\t" + filename + "\n");
                out.close();
            } catch (Exception e) {
                toast("FILE NOT FOUND FOR SOME FUCKING REASON GODDAMMIT");
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            TextView message = (TextView) findViewById(R.id.add_story_text_view);
            message.setTextColor(Color.parseColor("#ff0000"));
            String error = "";

            for(int i = 0; i < errors.size(); i++) {
                error += errors.get(i) + "\n";
            }

            message.setText(error);
        }
    }

    public ArrayList<String> checkStory(String story, String title) {
        ArrayList<String> errors = new ArrayList<>();
        int greaterThan = 0, lessThan = 0;

        if(title.length() > 0) {
            String[] invalid_characters = {"/", "\\", ".", "<", ">"};

            for (int i = 0; i < invalid_characters.length; i++) {
                if (title.indexOf(invalid_characters[i]) > -1) {
                    errors.add("Invalid character " + invalid_characters[i] + " in title.");
                }
            }
        } else {
            errors.add("Title field cannot be blank. Please enter a title");
        }

        if(story.length() > 0) {

            int place = 0;
            while (story.indexOf('<', place) > -1) {
                place = story.indexOf('<', place) + 1;
                lessThan++;
            }

            place = 0;
            while (story.indexOf('>', place) > -1) {
                place = story.indexOf('>', place) + 1;
                greaterThan++;
            }

            int difference = lessThan - greaterThan;

            toast("LT: " + lessThan + " GT: " + greaterThan);
            toast("DIF: " + difference);

            if(lessThan == 0 && greaterThan == 0) {
                errors.add("Please include at least one tag: <description>");
            } else if (difference != 0) {
                errors.add("Your open tags < do not match your close tags >");
            } else {
                int startOfType;
                int endOfType = 0;

                while (story.indexOf('<', endOfType) > -1) {
                    startOfType = story.indexOf('<', endOfType);
                    endOfType = story.indexOf('>', startOfType);
                    int empty_tags = 0;

                    if (endOfType - startOfType == 1) {
                        empty_tags++;
                    }

                    if (empty_tags == 1) {
                        errors.add("You have an empty tag: <>");
                    } else if (empty_tags > 1) {
                        errors.add("You have " + empty_tags + " empty tags: <>");
                    }
                }

            }
        } else {
            errors.add("Story field cannot be blank. Please enter a story");
        }

        return errors;
    }
}
