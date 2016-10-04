package edu.scu.klnguyen.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.scu.klnguyen.simpletodo.Model.Item;

// This activity allows users to add a new item to the to-do list
public class AddItemActivity extends AppCompatActivity {
    String name;        // name of item
    String notes;       // notes of item
    Date due;           // due date
    int priority;       // priority level
    int status;         // status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        String[] levels = {"HIGH","MEDIUM","LOW"};  // 3 level of priority
        String[] status = {"TO-DO","DONE"};         // 2 status

        Spinner level = (Spinner) findViewById(R.id.level2);                // drop-down list for 3 different priority levels
        level.setAdapter(new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            levels)
        );

        Spinner statu = (Spinner) findViewById(R.id.status2);               // drop-down list for status
        statu.setAdapter(new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            status)
        );
        createActionBar();
    }

    // create an action bar with a list icon and a string
    protected void createActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFffffff));
        actionBar.setIcon(R.drawable.small_list);
        String str = "<font color='#eb65a0'>" + getString(R.string.app_name) +"</font>";
        actionBar.setTitle(Html.fromHtml(str));
        actionBar.setDisplayShowHomeEnabled(true);
    }

    // add a menu of save and close icon
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    // listeners for the save and close icon
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent intent;
        switch (item.getItemId()) {
            case R.id.close:    finish();
                                break;
            case R.id.save:    saveItem();
        }
        return true;
    }

    // read user's input from the view, create a new item from input,
    // sends new item back to calling activity
    public void saveItem() {
        EditText nameEt = (EditText) findViewById(R.id.name2);
        DatePicker datePicker = (DatePicker) findViewById(R.id.date2);
        EditText notesEt = (EditText) findViewById(R.id.note2);
        Spinner level = (Spinner) findViewById(R.id.level2);
        Spinner statu = (Spinner) findViewById(R.id.status2);

        // read inputs
        name = nameEt.getText().toString();
        due = getDate(datePicker);
        notes = notesEt.getText().toString();
        priority = level.getSelectedItemPosition();
        status = statu.getSelectedItemPosition();

        Item item = new Item(name,due,notes,priority,status);   // create new item with input

        Intent data = new Intent();
        data.putExtra("item", item);

        setResult(RESULT_OK, data);                             // set result code and bundle data for response
        finish();                                               // closes the activity, pass data to parent/caller
    }

    // read input from a DatePicker and convert to a Date object
    private Date getDate(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
