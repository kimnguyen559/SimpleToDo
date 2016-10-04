package edu.scu.klnguyen.simpletodo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.scu.klnguyen.simpletodo.Model.Item;

// this is a custom adapter for a list view.
// it takes a list of item and displays each item in a
// custom row
public class CustomAdapter extends ArrayAdapter<Item> {
        private final Activity context;
        List<Item> items;                                       // list of items to display

        public CustomAdapter(Activity context, List<Item> items) {
            super(context, R.layout.custom_row, items);
            this.context=context;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View row = inflater.inflate(R.layout.custom_row, null, true);

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView priority = (TextView) row.findViewById(R.id.priority);

            Item item = items.get(position);

            int prio = item.getPriority();                                  // set drop-down list with 3 levels of priority
            String p = "HIGH";
            priority.setTextColor(0xFFeb65a0);
            if (prio == 1) {
                p = "MIDIUM";
                priority.setTextColor(0xFFfba827);
            }
            else if (prio == 2) {
                p = "LOW";
                priority.setTextColor(0xFF0092d7);
            }

            name.setText(item.getName());                   // display item name
            priority.setText(p);                            // set priority leve

            return row;
        }
}
