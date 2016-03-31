package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Add_Hunt_Item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__hunt__item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a Task");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText nameField = (EditText) findViewById(R.id.nameHuntItem);
                String name = ""+nameField.getText();
                if (name.equals("")) {
                    Context context = getApplicationContext();
                    CharSequence text = "Please name your task.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    EditText descField = (EditText) findViewById(R.id.description);
                    String desc = "" + descField.getText();
                    CheckBox picReqField = (CheckBox) findViewById(R.id.picReq);
                    boolean picReq = picReqField.isChecked();
                    CheckBox locReqField = (CheckBox) findViewById(R.id.locReq);
                    boolean locReq = locReqField.isChecked();


                    LineItem item = new LineItem(name, desc, picReq, locReq);
                    HuntItems.itemList.add(item);

                    Intent intent = getIntent();

                    Tab1.myHuntDB.insertHunt(intent.getStringExtra("name"), name, desc, picReq, locReq);

                    HuntItems.itemAdapter.notifyDataSetChanged();

                    finish();
                }
            }
        });


    }

}
