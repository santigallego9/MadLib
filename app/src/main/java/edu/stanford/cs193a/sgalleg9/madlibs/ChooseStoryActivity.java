package edu.stanford.cs193a.sgalleg9.madlibs;

import android.content.Intent;
import android.os.Bundle;
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
    private HashMap<Integer, String> stories;
    private int endOfNativeStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_story);

        ListView list = find(R.id.stories_list);
        list.setOnItemClickListener(this);

        availableStories = new ArrayList<>();
        stories = new HashMap<>();

        loadStories();

        SimpleList.with(this).setItems(findListView(R.id.stories_list), availableStories);
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

                intent.putExtra("filename", stories.get(index));
            }
        } else {
            type = "user";

            intent.putExtra("filename", stories.get(index));
        }

        intent.putExtra("type", type);

        startActivity(intent);
    }

    public void loadStories() {

        Scanner s = new Scanner(getResources().openRawResource(R.raw.stories));

        do {
            ArrayList<String> storyInfo = new ArrayList<>();
            int counter = 0;

            String name = s.next();
            String filename = s.next();

            availableStories.add(name);
            stories.put(counter, filename);

            counter++;
        } while(s.hasNextLine());

        endOfNativeStories = availableStories.size();

        try {

            Scanner u = new Scanner(openFileInput("user_stories.txt"));

            do {
                ArrayList<String> storyInfo = new ArrayList<>();
                int counter = endOfNativeStories;

                String name = u.next();
                String filename = u.next();

                availableStories.add(name);
                stories.put(counter, filename);

                counter++;
            } while(u.hasNextLine());
        } catch(Exception e) {
            log("Failed to find user input file");
        }

        availableStories.add("Random");
    }

    public void addButtonClick(View view) {
        toast("CLICKED");
    }
}
