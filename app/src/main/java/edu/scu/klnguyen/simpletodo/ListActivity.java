package edu.scu.klnguyen.simpletodo;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.scu.klnguyen.simpletodo.Database.DBHelper;
import edu.scu.klnguyen.simpletodo.Model.Item;

/* This is a launcher activity.
 * It reads the DB and lists all saved tasks
 */
public class ListActivity extends AppCompatActivity implements CustomDialogFrament.MyDialogFragmentListener {
    List<Item> items;                   // list of task items, this list associated with the listview
    ListView listView;                  // listview showing the items
    CustomAdapter adapter;              // adapter for listview
    DBHelper dbHelper;                  // DB helper to connect with DB
    FragmentManager fm;

    private final int ADD_CODE = 8;
    private final int EDIT_CODE = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        creaateList8();                         // create list of task items
        createActionBar();                      // create action bar with an add button
        showList();                             // display all items on the screen
    }

    // display item list
    private void showList() {
        adapter = new CustomAdapter(this, items);           // attach the list of items to the adapter
        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);                       // attach the adapter to the list view

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // add listener to each item
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Item item = items.get(position);
                showItem(item, position);               // call method to show the clicked item
            }
        });
    }

    // this method called when an item on the list got clicked
    // it displays the item in a dialog frament
    private void showItem(Item item, int pos) {
        fm = getSupportFragmentManager();
        CustomDialogFrament frag = CustomDialogFrament.newInstance(item, pos, this);
        frag.show(fm, "Edit Task");             // show the item in a
    }

    // delete an item
    public void deleteItem(int pos) {
        dbHelper.deleteItem(items.get(pos));    // call Data Helper to delete item from DB
        items.remove(pos);                      // delete item from current list
        adapter.notifyDataSetChanged();         // call adapter to change the view
    }

    // edite an item
    public void editItem(int pos) {
        Item item = items.get(pos);
        Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);    // call the activity editing the item
        intent.putExtra("item",item);
        intent.putExtra("pos", pos);
        startActivityForResult(intent, EDIT_CODE);
    }

    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // read DB to get list of item, sort items in ascending order of priority level
    private void creaateList8() {
        dbHelper = new DBHelper(this);
        items = dbHelper.getAllItems();
        Collections.sort(items);
    }

    // create action bar with a list icon and a string
    protected void createActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFffffff));
        actionBar.setIcon(R.drawable.small_list);
        String str = "<font color='#eb65a0'>" + getString(R.string.app_name) +"</font>";
        actionBar.setTitle(Html.fromHtml(str));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // add a menu (with an add icon) to the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    // listener for the 'add' icon in the action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add:
                intent = new Intent(getApplicationContext(), AddItemActivity.class);    // call the activity that adds new items
                startActivityForResult(intent, ADD_CODE);
        }
        return true;
    }

    @Override
    // listener triggered when the previously called activity ended (
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // return from add new item activity
        if (resultCode == RESULT_OK && requestCode == ADD_CODE) {
            Item item = (Item) data.getSerializableExtra("item");   // read the new item from the add activity
            dbHelper.addItem(item);             // add the new item to DB
            items.add(item);                    // add the new item to current list
            Collections.sort(items);            // sort the list in order of priority
            adapter.notifyDataSetChanged();     // update the viet
        }

        // return from edit item
        if (resultCode == RESULT_OK && requestCode == EDIT_CODE) {
            Item item = (Item) data.getSerializableExtra("item");   // read the item from the edit activity
            int pos = data.getIntExtra("pos",0);                    // read the item position in the list
            int isChanged = data.getIntExtra("isChanged",0);        // if item has been changed or not

            if (isChanged == 1 ) {                  // if there is change
                items.set(pos, item);               // update the list
                Collections.sort(items);            // and sort it
                dbHelper.updateItem(item);          // update the DB
                adapter.notifyDataSetChanged();     // update view
            }
        }
    }
}
