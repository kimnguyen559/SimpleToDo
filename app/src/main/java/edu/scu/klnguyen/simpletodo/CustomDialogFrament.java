package edu.scu.klnguyen.simpletodo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.scu.klnguyen.simpletodo.Model.Item;

import static android.app.AlertDialog.*;

// This is a custom DialogFragment. It display details of an item
public class CustomDialogFrament extends DialogFragment {
    Context context;
    int pos;

    // Listener for delete and edit icons
    public interface MyDialogFragmentListener {
        public void deleteItem(int pos);
        public void editItem(int pos);
    }

    public CustomDialogFrament() {}

    public CustomDialogFrament(Context context, int pos) {
        this.context = context;
        this.pos = pos;
    }

    // receives an item and its position in the listview, returns
    // a CustomDialogFrament object bundled with the item
    public static CustomDialogFrament newInstance(Item item, int pos, Context context) {
        CustomDialogFrament frag = new CustomDialogFrament(context,pos);
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_display_item, container);

        ImageView close = (ImageView) v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });                                                             // listener for  close icon

        ImageView delete = (ImageView) v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm();
            }
        });                                                             // listener for delete icon

        ImageView edit = (ImageView) v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask();
            }
        });                                                             // listener for edit icon

        return v;
    }

    // show an alert dialog when user click the delete icon
    private void deleteConfirm() {
        Builder builder = new Builder(context);
        builder.setTitle("Deleting task")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.stop)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTask();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ;
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView tv = ((TextView) alertDialog.findViewById(android.R.id.message));
        tv.setTextColor(0xFFf79b2e);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);

        Button ybutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ybutton.setTextColor(Color.RED);
    }

    // called the event handler method when user clicked the delete icon
    public void deleteTask() {
        MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
        activity.deleteItem(pos);
        dismiss();
    }

    // called the event handler method when user clicked the edit icon
    public void editTask() {
        MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
        activity.editItem(pos);
        dismiss();
    }

    @Override
    // display the item in a dialog fragment
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Item item = (Item) getArguments().getSerializable("item");

        TextView name = (TextView) view.findViewById(R.id.name2);
        name.setText(item.getName());

        TextView date = (TextView) view.findViewById(R.id.date2);
        date.setText(new SimpleDateFormat("MM/dd/yyyy").format(item.getDueDate()));

        TextView notes = (TextView) view.findViewById(R.id.note2);
        notes.setText(item.getNotes());

        String levelStr = "HIGH";
        int level = item.getPriority();
        if (level == 1)
            levelStr = "MEDIUM";
        else if (level == 2)
            levelStr = "LOW";

        TextView priority = (TextView) view.findViewById(R.id.level2);
        priority.setText(levelStr);

        String statusStr = item.getStatus() == 0 ? "TO-DO" : "DONE";

        TextView status = (TextView) view.findViewById(R.id.status2);
        status.setText(statusStr);
    }
}
