package rbh9dm.cs4720.com.scavengerhunt;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;


public class Tab1 extends Fragment {

    public static ArrayList<String> huntList = new ArrayList<>();
    public static ArrayAdapter<String> huntsAdapter;
    public static ListView listView;
    public static final String ID = "id";
    public static ScavengerHuntDBHelper myDB;
    public static HuntItemDBHelper myHuntDB;
    public static MoreInfoDBHelper myImgDB;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1, container, false);

        /*** connect to DB and load items ***/
        myDB = new ScavengerHuntDBHelper(getActivity());
        myHuntDB = new HuntItemDBHelper(getActivity());
        myImgDB = new MoreInfoDBHelper(getActivity());
        huntList = myDB.getAllHunts();
        huntsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, huntList);

        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(huntsAdapter);

        /*** Navigate to hunt items screen on click ***/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();

                Intent intent = new Intent(getActivity(), HuntItems.class);
                intent.putExtra(ID, position);
                startActivity(intent);
            }
        });
        
        /*** Add FAB ***/
        //FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddScavengerHunt.class);
                //Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
