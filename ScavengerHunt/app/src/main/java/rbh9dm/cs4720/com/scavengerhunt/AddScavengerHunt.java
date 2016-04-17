package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddScavengerHunt extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scavenger_hunt);

        /*** Set up toolbar ***/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                /** Don't add if the name is not unique **/
                else if (Tab1.myDB.exists(name)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Names of the Scavenger Hunts must be unique";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /*** Add otherwise ***/
                else {
                    Tab1.myDB.insertHunt(name, false);
                    Tab1.huntList.add(name);
                    Tab1.huntsAdapter.notifyDataSetChanged();

                    finish();
                }
            }
        });
    }

}
