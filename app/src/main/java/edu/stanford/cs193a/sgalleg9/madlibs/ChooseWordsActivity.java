package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import stanford.androidlib.SimpleActivity;

public class ChooseWordsActivity extends SimpleActivity {

    private String finishedStory;
    private int count = 1, clicks = 1;
    private Story story;
    EditText input;
    TextView wordsLeft, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_words);

        input = (EditText) findViewById(R.id.edit_text);
        wordsLeft = (TextView) findViewById(R.id.words_left_text_view);
        type = (TextView) findViewById(R.id.type_text_view);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String filename = intent.getStringExtra("filename");

        Log.d("FILENAMES", "Type: " + type + " Filename: " + filename);

        Scanner s;

        if(type.equals("native")) {
            s = chooseNativeFile(filename);
        } else {
            s = new Scanner(openFileInput(filename));
        }

        story = new Story(s);

        setInformation(0);

    }

    public void submitButtonClick(View view) {
        int totalCount = story.getPlaceholderCount();

        String wordChoice = input.getText().toString();

        story.setPlaceholder(count - 1, wordChoice);

        if(count == totalCount) {
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra("story", story.getStory());
            startActivity(intent);
        } else if(count == totalCount - 1) {
            Button add = (Button) findViewById(R.id.add_button);

            add.setText("Finish");
            setInformation(clicks);
        } else {
            setInformation(clicks);
        }

        clicks++;
        count++;
    }

    public void setInformation(int index) {
        int totalCount = story.getPlaceholderCount();
        String wordType = story.getPlaceholder(index);

        if(totalCount - count > 1) {
            wordsLeft.setText(totalCount - index + " words left");
        } else {
            wordsLeft.setText(totalCount - index + " word left");
        }

        if(startsWithVowel(wordType)) {
            type.setText("Please enter an " + wordType);
        } else {
            type.setText("Please enter a " + wordType);
        }


        input.setText("");
    }

    public boolean startsWithVowel(String word) {
        String[] vowels = {"a", "e", "i", "o", "u"};

        for(int i = 0; i < 5; i++) {
            if(word.substring(0, 1).equals(vowels[i])) {
                return true;
            }
        }

        return false;
    }

    public Scanner chooseNativeFile(String filename) {
        Scanner s;

        switch(filename) {
            case "Tarzan":
                s = new Scanner(getResources().openRawResource(R.raw.madlib_tarzan));
                break;
            case "Dating":
                s = new Scanner(getResources().openRawResource(R.raw.madlib_how_to_date));
                break;
            case "Holiday":
                s = new Scanner(getResources().openRawResource(R.raw.madlib_holiday));
                break;
            default:
                toast("ERROR: File not found. Using default file");
                Log.d("ERROR", "NO NATIVE FILE FOUND, KEY: " + filename);
                s = new Scanner(getResources().openRawResource(R.raw.madlib_tarzan));
        }

        return s;
    }
}
