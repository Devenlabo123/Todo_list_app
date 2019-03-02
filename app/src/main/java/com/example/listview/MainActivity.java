package com.example.listview;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //Define variables
    ListView listView;
    TextView textView;
    EditText note_input;
    List<String> listItem = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private static final String tag = "Widgets";
    int current_pos; //When user clicks Item of list this points to that position in the ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Variables for XML components
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);              //attach listener
        textView = (EditText) findViewById(R.id.textView);
        note_input = findViewById(R.id.editText);

        //Initializes and sets adapter
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        try {
            current_pos = position;
            String value = (String) parent.getItemAtPosition(position);

            //Sets edit textbox to the selected value
            note_input.setText(value.substring(3).trim());

            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Takes text in the edit text box
        String input = note_input.getText().toString();

        switch (item.getItemId()) {
            case R.id.add:
                try {
                    Log.i(tag, "ADD: " + input);

                    //Adds item to adapter
                    adapter.add(adapter.getCount() + 1 + ".    " + input);
                    adapter.notifyDataSetChanged();

                    //Every time after first adding new items. It appends the ArrayList too
                    if (adapter.getCount() != listItem.size()) {
                        Log.i(tag, "Added by list...");
                        listItem.add(listItem.size() + 1 + ".    " + input);
                    }

                    //Resets the EditText box
                    note_input.setText("");

                    for (int i = 0; i < listItem.size(); i++) {
                        Log.i(tag, i + ": " + listItem.get(i));
                    }

                    current_pos = -1; //Resets position
                    return true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error, nothing selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.delete:
                try {
                    Log.i(tag, "Delete: " + input);
                    Log.i(tag, "Current pos: " + current_pos);


                    listItem.remove(current_pos); //Removes item from list
                    listItem = remake_array(listItem); //Remakes list so that number labels are in order

                    Log.i(tag, "After Remake");

                    for (int i = 0; i < listItem.size(); i++) {
                        Log.i(tag, i + ": " + listItem.get(i));
                    }

                    //Following three lines are to update the adapter
                    adapter.clear();
                    adapter.addAll(listItem);
                    adapter.notifyDataSetChanged();

                    //Resets the edit text box and current position
                    note_input.setText("");
                    current_pos = -1;
                    return true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Nothing selected to delete", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.update:
                try {

                    Log.i(tag, "Update: " + input);
                    Log.i(tag, "Current pos: " + current_pos);

                    //Updates the adapter
                    adapter.remove(listItem.get(current_pos));
                    adapter.insert(current_pos+1 + ".    " + input,current_pos);

                    //Updates the ArrayList
                    listItem.remove(current_pos); //Removes item from list
                    listItem.add(current_pos, current_pos+1 + ".    " + input);
                    listItem = remake_array(listItem); //Remakes list so that number labels are in order

                    adapter.notifyDataSetChanged();

                    Log.i(tag, "After Update");

                    for (int i = 0; i < listItem.size(); i++) {
                        Log.i(tag, i + ": " + listItem.get(i));
                    }



                    note_input.setText("");
                    current_pos = -1;
                    return true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Nothing selected to update", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //If the user presses the close or save button it closes the app
            case R.id.close:
                finish();
                return true;

            case R.id.save:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Function to remake the array so number labels are accurate
    public List<String> remake_array(List<String> lists){
        List<String> listItemtemp = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            listItemtemp.add(i + 1 + ".    " + lists.get(i).substring(3).trim());
        }
        return listItemtemp;

    }

    public ArrayAdapter<String> remake_adapter(ArrayAdapter<String> a) {
        ArrayAdapter<String> adapter_temp;
        adapter_temp = a;
        a.clear();
        for(int i = 0; i<adapter_temp.getCount();i++) {
            a.add(i + 1 + ".    " + adapter_temp.getItem(i));
            Log.i(tag, "remake array"  +i + 1 + ".    " + adapter_temp.getItem(i));
        }
        return a;
    }
}