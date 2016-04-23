package rbh9dm.cs4720.com.scavengerhunt;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;

public class Tab2 extends Fragment {
    public static ArrayList<String> huntList = new ArrayList<>();
    public static ArrayAdapter<String> huntsAdapter;
    public static final String TITLE = "title";


    public static Firebase ref;
    public static Query qref;
    public static ValueEventListener listener;
    public static int numToShow;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);

        huntsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, huntList);

        numToShow = 5;
        Firebase.setAndroidContext(getActivity());
        Firebase ref = new Firebase("https://cs4720scavhunt.firebaseio.com/hunts");
        qref = ref.limitToLast(numToShow);


        ListView listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(huntsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), HuntDownload.class);
                intent.putExtra(TITLE, huntList.get(position));
                startActivity(intent);

            }
        });

        qref.addValueEventListener( listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                huntList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    huntList.add(child.getKey());
                }
                huntsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return v;

    }

}
