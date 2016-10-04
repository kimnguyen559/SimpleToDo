package edu.scu.klnguyen.simpletodo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.scu.klnguyen.simpletodo.Model.Item;

// This activity receives an item as an extra from calling activiy.
// It displays the item and allows user to make changes.
// It then creates a new item from the input and sends it back to calling activity
public class EditItemActivity extends AppCompatActivity {
    Item item;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // get the item from calling activity
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        pos = intent.getIntExtra("pos", 0);

        // show the item
        EditText name = (EditText)findViewById(R.id.name2);             // display name
        name.setText(item.getName());
        name.requestFocus();
        name.setSelection(0);

        DatePicker datePicker = (DatePicker)findViewById(R.id.date2);   // display due date in a DataPicker view
        setDatePicker(item.getDueDate(),datePicker);

        EditText notes = (EditText)findViewById(R.id.note2);            // display notes
        notes.setText(item.getNotes());

        String[] levels = {"HIGH","MEDIUM","LOW"};
        String[] status = {"TO-DO","DONE"};

        Spinner level = (Spinner) findViewById(R.id.level2);            // show priority level in a drop-down list
        level.setAdapter(new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        levels)
        );
        level.setSelection(item.getPriority());

        Spinner statu = (Spinner) findViewById(R.id.status2);           // show status in a drop-down list
        statu.setAdapter(new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        status)
        );
        statu.setSelection(item.getStatus());
        createActionBar();
    }

    // receive a Date object, set date, month and year of
    // the DatePicker to match with the Date object
    private void setDatePicker(Date date, DatePicker datePicker){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);
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

    // add a menu to the action bar with close and save icons
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    // listeners for the close and save icon
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:    close();
                                 break;
            case R.id.save:    save();
        }
        return true;
    }

    // bundle input and send back to calling activity
    private void close() {
        Intent data = new Intent();
        data.putExtra("pos", pos);
        data.putExtra("item", item);
        data.putExtra("isChanged",0);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    // read user's input, create a new item with input,
    // sends new item back to calling activity
    public void save() {
        EditText nameEt = (EditText) findViewById(R.id.name2);
        DatePicker datePicker = (DatePicker) findViewById(R.id.date2);
        EditText notesEt = (EditText) findViewById(R.id.note2);
        Spinner level = (Spinner) findViewById(R.id.level2);
        Spinner statu = (Spinner) findViewById(R.id.status2);

        // read user input
        String name = nameEt.getText().toString();
        Date due = getDate(datePicker);
        String notes = notesEt.getText().toString();
        int priority = level.getSelectedItemPosition();
        int status = statu.getSelectedItemPosition();
        int id = item.getId();

        // create new item
        item = new Item(name,due,notes,priority,status);
        item.setId(id);

        // send new item to calling activity
        Intent data = new Intent();
        data.putExtra("item", item);
        data.putExtra("pos",pos);
        data.putExtra("isChanged",1);

        setResult(RESULT_OK, data);     // set result code and bundle data for response
        finish();                       // closes the activity, pass data to parent
    }

    // read input from DatePicker, create a new Date object from input
    private Date getDate(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
