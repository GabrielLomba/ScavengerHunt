package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddScavengerHunt extends AppCompatActivity {
    String old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scavenger_hunt);

        if (Tab1.myDB == null) {
            finish();
        } else {
            /*** Set up toolbar ***/
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            old = "";
            Intent intent = getIntent();
            if (intent.hasExtra("name")) {
                EditText name = (EditText) findViewById(R.id.editText);
                name.setText(intent.getStringExtra("name"));
                old = intent.getStringExtra("name");
                getSupportActionBar().setTitle("Edit the Scavenger Hunt");
            } else
                getSupportActionBar().setTitle("Add a Scavenger Hunt");

            /*** Set up FAB ***/
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText nameField = (EditText) findViewById(R.id.editText);
                    String name = nameField.getText().toString();
                    /*** Don't add if name is empty ***/
                    if (name.equals("")) {
                        Context context = getApplicationContext();
                        CharSequence text = "Please name your Scavenger Hunt";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else if (name.equals(old)) {
                        finish();
                    }
                    /** Don't add if the name is not unique **/
                    else if (Tab1.myDB.exists(name)) {
                        Context context = getApplicationContext();
                        CharSequence text = "Names of the Scavenger Hunts must be unique";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    /*** Add/edit otherwise ***/
                    else {
                        Intent intent = getIntent();
                        if (intent.hasExtra("name")) {
                            Tab1.myDB.updateHunt(intent.getStringExtra("name"), name);
                            Tab1.myHuntDB.updateHunt(intent.getStringExtra("name"), name);
                            Tab1.myImgDB.updateName(intent.getStringExtra("name"), name);
                            int index = Tab1.huntList.indexOf(intent.getStringExtra("name"));
                            Tab1.huntList.remove(intent.getStringExtra("name"));
                            Tab1.huntList.add(index, name);
                            Tab1.huntsAdapter.notifyDataSetChanged();
                        } else {
                            Tab1.myDB.insertHunt(name, false);
                            Tab1.huntList.add(name);
                            Tab1.huntDoneList.add("Incomplete");
                            Tab1.huntsAdapter.notifyDataSetChanged();
                        }
                        finish();
                    }
                }
            });
        }
    }

}
