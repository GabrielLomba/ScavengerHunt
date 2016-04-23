package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    /*
    public static ArrayList<String> huntList = new ArrayList<String>();
    public static ArrayAdapter<String> huntsAdapter;
    public static final String TITLE = "title";
    */

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Your Scavenger Hunts","Browse Online"};
    int Numboftabs =2;

    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Declaring Your View and Variables

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Scavenger Hunts");

        /*** Add FAB ***/
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddScavengerHunt.class);
                //Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

         // Assigning ViewPager View and setting the adapter
         pager = (ViewPager) findViewById(R.id.pager);
         pager.setAdapter(adapter);
        //pager.addOnPageChangeListener(new InternalViewPagerListener());

         // Assiging the Sliding Tab Layout View
         tabs = (SlidingTabLayout) findViewById(R.id.tabs);
         tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

         // Setting Custom Color for the Scroll bar indicator of the Tab View
         tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                 return getResources().getColor(R.color.colorAccent);
             }
            });

         // Setting the ViewPager For the SlidingTabsLayout
         tabs.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
            if(position == 0) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_18dp, getTheme()));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddScavengerHunt.class);
                        startActivity(intent);
                    }
                });

                Tab2.numToShow = 5;

                Tab2.qref.removeEventListener(Tab2.listener);
                Tab2.qref = Tab2.ref.limitToLast(Tab2.numToShow);

                Tab2.qref.addValueEventListener(Tab2.listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Tab2.huntList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Tab2.huntList.add(child.getKey());
                        }
                        Tab2.huntsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
            else {

                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp, getTheme()));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Tab2.numToShow += 5;

                        Tab2.qref.removeEventListener(Tab2.listener);
                        Tab2.qref = Tab2.ref.limitToLast(Tab2.numToShow);

                        Tab2.qref.addValueEventListener(Tab2.listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Tab2.huntList.clear();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Tab2.huntList.add(child.getKey());
                                }
                                Tab2.huntsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

            }
        }

    }

}
