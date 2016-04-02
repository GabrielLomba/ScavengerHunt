package rbh9dm.cs4720.com.scavengerhunt;

/**
 * Created by Student User on 3/30/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.IndexedNode;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab2 extends Fragment {
    public static ArrayList<String> huntList = new ArrayList<String>();
    public static ArrayAdapter<String> huntsAdapter;
    public static final String TITLE = "title";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        huntsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, huntList);

        ListView listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(huntsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();

                Intent intent = new Intent(getActivity(), HuntItems.class);
                intent.putExtra(TITLE, selected);
                startActivity(intent);
            }
        });
        Firebase.setAndroidContext(getActivity());
        Firebase ref = new Firebase("https://cs4720scavhunt.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot = dataSnapshot.child("hunts");
                huntList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tab2.huntList.add(child.getKey());
                }
                huntsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddScavengerHunt.class);
                startActivity(intent);
            }
        });

        return v;

    }

}
