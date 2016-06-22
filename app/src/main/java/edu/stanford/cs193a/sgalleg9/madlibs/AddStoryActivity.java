package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.io.PrintStream;

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

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    public void saveStory(String story, String title) {

        while(title.indexOf(" ") > -1) {
            title = title.replace(" ", "_");
        }
        String filename = "madlib_" + title.toLowerCase() + ".txt";

        try {
            PrintStream out = new PrintStream(openFileOutput(filename, MODE_PRIVATE));
            out.print(story);
            out.close();
        } catch (Exception e) {
            toast("FILE NOT FOUND FOR SOME FUCKING REASON");
        }

        try {
            PrintStream out = new PrintStream(openFileOutput("user_stories.txt", MODE_PRIVATE));
            out.append(title + "\t" + filename + "\n");
            out.close();
        } catch (Exception e) {
            toast("FILE NOT FOUND FOR SOME FUCKING REASON");
        }
    }
}
