package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.SimpleList;

public class ChooseStoryActivity extends SimpleActivity {


    private ArrayList<String> availableStories;
    private ArrayList<String> filenames;
    private int endOfNativeStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_story);
    }

    @Override
    public void onItemClick(ListView list, int index) {
        toast("Selected: " + availableStories.get(index));
        String type;

        Intent intent = new Intent(this, ChooseWordsActivity.class);

        if (index < endOfNativeStories) {
            type = "native";

            intent.putExtra("filename", availableStories.get(index));
        } else if (index == availableStories.size() - 1) {
            Random r = new Random();

            index = r.nextInt(availableStories.size() - 1);

            if (index < endOfNativeStories) {
                type = "native";

                intent.putExtra("filename", availableStories.get(index));
            } else {
                type = "user";

                intent.putExtra("filename", filenames.get(index));
            }
        } else {
            type = "user";

            intent.putExtra("filename", filenames.get(index));
        }

        intent.putExtra("type", type);

        startActivity(intent);
    }

    public void loadStories() {

        Scanner s = new Scanner(getResources().openRawResource(R.raw.stories));

        do {
            String name = s.next();
            String filename = s.next();

            availableStories.add(name);
            filenames.add(filename);
        } while(s.hasNextLine());

        endOfNativeStories = availableStories.size();

        try {

            Scanner u = new Scanner(openFileInput("user_stories.txt"));

            do {
                String name = u.next();
                String filename = u.next();

                name = name.replaceAll("_", " ");

                availableStories.add(name);
                filenames.add(filename);

            } while(u.hasNextLine());
        } catch(Exception e) {
            log("Failed to find user input file");
        }

        availableStories.add("Random");
    }

    public void addButtonClick(View view) {
        Intent intent = new Intent(this, AddStoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView list = find(R.id.stories_list);
        list.setOnItemClickListener(this);

        availableStories = new ArrayList<>();
        filenames = new ArrayList<>();

        loadStories();

        SimpleList.with(this).setItems(findListView(R.id.stories_list), availableStories);
    }
}
