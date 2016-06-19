package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ChooseWordsActivity extends AppCompatActivity {

    private String finishedStory;
    private int count = 0;
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

        Scanner s = new Scanner(getResources().openRawResource(R.raw.madlib_tarzan));
        story = new Story(s);

        setInformation(0);

    }

    public void submitButtonClick(View view) {
        int totalCount = story.getPlaceholderCount();

        String wordChoice = input.getText().toString();

        if(count == totalCount) {
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra("story", finishedStory);
            startActivity(intent);
        }
    }

    public void setInformation(int index) {
        int totalCount = story.getPlaceholderCount();
        String wordType = story.getPlaceholder(0);

        wordsLeft.setText(totalCount - index + " word(s) left");
        type.setText("Please enter a " + wordType);

    }
}
