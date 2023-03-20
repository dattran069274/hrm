package com.example.hrm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;

    private Toolbar toolbar;

    private NavigationView nvDrawer;
    final FragmentManager fragmentManager = getSupportFragmentManager();


    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.

    private ActionBarDrawerToggle drawerToggle;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("onCreate");
        Drawable d=getResources().getDrawable(R.drawable.menu);
        Bitmap bitmap=((BitmapDrawable)d).getBitmap();
        Drawable newD=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,20,20,true));
        //d.setBounds(0,0,1000,1000);
        toolbar.setNavigationIcon(newD);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"your icon was clicked",Toast.LENGTH_SHORT).show();
            }
        });
        setSupportActionBar(toolbar);



        // This will display an Up icon (<-), we will replace it with hamburger later

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Find our drawer view

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent();
        getAllStaff();
    }

        private void getAllStaff() {
        Call<DataResponseList<DatumTemplate<StaffAttributes>>> call= APIService.getService().getAllStaff(Common.getToken());
        call.enqueue(new Callback<DataResponseList<DatumTemplate<StaffAttributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Response<DataResponseList<DatumTemplate<StaffAttributes>>> response) {
                Log.d("getAllStaff","onResponse");
                List<StaffAttributes> staffAttributes=new ArrayList<>();

                DataResponseList<DatumTemplate<StaffAttributes>> res=response.body();
                String[] names=new String[res.getData().size()];
                for(int i=0;i<res.getData().size();i++){
                    StaffAttributes att=res.getData().get(i).getAttributes();
                    names[i]=att.getFullname();
                }
                Common.setStaffs(staffAttributes);
                Common.setStaffNames(names);
                Log.d("names",names[0]);
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Throwable t) {
                Log.d("getAllStaff","onFailure");
            }
        });
    }

        private void setupDrawerContent( ) {
            nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectDrawerItem(item);
                    return true;
                }
            });
        }
        public void selectDrawerItem(MenuItem menuItem) {
            // Create a new fragment and specify the fragment to show based on nav item clicked

            Fragment fragment = null;

            Class fragmentClass;
            String tag="";
            switch(menuItem.getItemId()) {

                case R.id.nav_department:

                    fragmentClass = DepartmentFragment.class;
                    tag=((DepartmentFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_jobtitle:

                    fragmentClass = JobTitleFragment.class;
                    tag=((JobTitleFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_position:

                    fragmentClass = PositionFragment.class;
                    tag=((PositionFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_staff:

                    fragmentClass = StaffFragment.class;
                    tag=((StaffFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_inactive_stafft:

                    fragmentClass = InactiveStaffFragment.class;
                    tag=((InactiveStaffFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_leave:

                    fragmentClass = LeaveFragment.class;
                    tag=((LeaveFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_leave_applications:

                    fragmentClass = LeaveApplicationFragment.class;
                    tag=((LeaveApplicationFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_onboarding_sample:

                    fragmentClass = OnboardingSampleFragment.class;
                    tag=((OnboardingSampleFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_performance:

                    fragmentClass = PerformanceFragment.class;
                    tag=((PerformanceFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_properties_group:

                    fragmentClass = PropertiesGroupFragment.class;
                    tag=((PropertiesGroupFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_properties:

                    fragmentClass = PropertiesFragment.class;
                    tag=((PropertiesFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_providing_histories:

                    fragmentClass = ProvidingHistoryFragment.class;
                    tag=((ProvidingHistoryFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_self_preview:

                    fragmentClass = SelfPreviewFragment.class;
                    tag=((SelfPreviewFragment) fragment).MY_TAG;
                    break;
                case R.id.nav_request_properties:

                    fragmentClass = RequestPropertyFragment.class;
                    tag=((RequestPropertyFragment) fragment).MY_TAG;
                    break;
                    default:

                    fragmentClass = DepartmentFragment.class;
                    tag=((DepartmentFragment) fragment).MY_TAG;

            }



            try {

                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {

                e.printStackTrace();

            }



            // Insert the fragment by replacing any existing fragment


            Log.d("addtoBack",tag);
            fragmentManager.beginTransaction().addToBackStack(tag).replace(R.id.flContent, fragment, tag).commit();
            Common.CURRENT_FRAGMENT_TAG=tag;


            // Highlight the selected item has been done by NavigationView

            menuItem.setChecked(true);

            // Set action bar title
            getSupportActionBar().setTitle(menuItem.getTitle());

            // Close the navigation drawer

            mDrawer.closeDrawers();

        }
        @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.

        switch (item.getItemId()) {

            case android.R.id.home:

                mDrawer.openDrawer(GravityCompat.START);

                return true;

        }



        return super.onOptionsItemSelected(item);

    }
    public void showToast(boolean success,String mess){
        View toast=null;
        if(success) {
            toast=getLayoutInflater().inflate(R.layout.toast_success,null,false);
        }
        else {
            toast=getLayoutInflater().inflate(R.layout.toast_failed,null,false);
        }
        toast.setId(Integer.parseInt("06901"));
        TextView txteMess=toast.findViewById(R.id.txtMess);
        txteMess.setText(mess);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.BOTTOM;
        toast.setLayoutParams(layoutParams);
        FrameLayout fl=findViewById(R.id.flContent);
        fl.addView(toast);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fl.removeView(fl.findViewById(Integer.parseInt("06901")));
            }
        },2000);
    }
    public void relaceFragment(Fragment fragment) {
        String tag=fragment.getArguments().getString("TAG");
        Log.d("tag",tag);
        Log.d("tag","CURRENT_FRAGMENT_TAG: "+Common.CURRENT_FRAGMENT_TAG);
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.flContent);

        //Prevent adding same fragment on top
        if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }

        //If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (fragmentManager.findFragmentByTag(Common.CURRENT_FRAGMENT_TAG) != null) {
            Log.d("tag","find tag: not null"+Common.CURRENT_FRAGMENT_TAG);
            fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else Log.d("tag","find tag: null"+Common.CURRENT_FRAGMENT_TAG);
        Common.CURRENT_FRAGMENT_TAG=tag;
        //Otherwise, just replace fragment
        fragmentManager
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.flContent, fragment, tag)
                .commit();

    }

    @Override
    public void onBackPressed() {
        int fragmentsInStack =fragmentManager.getBackStackEntryCount();
        Log.d("fragmentsInStack", String.valueOf(fragmentsInStack));
        if (fragmentsInStack > 1) { // If we have more than one fragment, pop back stack
            fragmentManager.popBackStack();
        } else if (fragmentsInStack == 1) { // Finish activity, if only one fragment left, to prevent leaving empty screen
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Log.d("Home","onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("Home","onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Home","onPause");
    }
}