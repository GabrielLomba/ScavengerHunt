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

public class EditItem extends AppCompatActivity {

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Task");

        final Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        final EditText nameField = (EditText) findViewById(R.id.editNameItem);
        nameField.setText(HuntItems.itemList.get(pos).getName());

        final EditText descriptionField = (EditText) findViewById(R.id.editDescription);
        descriptionField.setText(HuntItems.itemList.get(pos).getDescription());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String description = descriptionField.getText().toString();
                String nameOfHunt = intent.getStringExtra("nameOfHunt");
                /*** If same name, just edit description ***/
                if(name.equals(HuntItems.itemList.get(pos).getName())){
                    Tab1.myHuntDB.updateItem(intent.getStringExtra("nameOfHunt"), HuntItems.itemList.get(pos).getName(), name, description);
                    HuntItems.itemList.get(pos).setDescription(description);
                    finish();
                }
                /*** Don't edit if name is empty ***/
                else if (name.equals("")) {
                    Context context = getApplicationContext();
                    CharSequence text = "Please name your Hunt Item";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /** Don't edit if the name is not unique **/
                else if (Tab1.myHuntDB.exists(nameOfHunt, name)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Names of items of the same Scavenger Hunt must be unique";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /*** Edit otherwise ***/
                else {
                    Tab1.myHuntDB.updateItem(intent.getStringExtra("nameOfHunt"), HuntItems.itemList.get(pos).getName(), name, description);
                    Tab1.myImgDB.updateItem(intent.getStringExtra("nameOfHunt"), HuntItems.itemList.get(pos).getName(), name);
                    HuntItems.itemList.get(pos).setName(name);
                    HuntItems.itemList.get(pos).setDescription(description);
                    HuntItems.itemAdapter.notifyDataSetChanged();
                    finish();
                }
            }
        });
    }

}
