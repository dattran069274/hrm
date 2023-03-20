package com.example.hrm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrm.Adapters.DepartmentAdapter;
import com.example.hrm.Adapters.UserAdapter;
import com.example.hrm.Helpler.Helper;
import com.example.hrm.Response.Attributes;
import com.example.hrm.Response.Data;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DataStaff;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.JobTitle;
import com.example.hrm.Response.Position;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public  static final String MY_TAG= "StaffFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        Log.d("StaffFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("StaffFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("StaffFragment","onDestroy");
        super.onDestroy();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffFragment newInstance(String param1, String param2) {
        StaffFragment fragment = new StaffFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View mView;
    private RecyclerView rcvDes;
    private LinearLayout btn_add;
    private Button btnSearch;
    private EditText edtStaffName;
    UserAdapter userAdapter;
    private AutoCompleteTextView departments;
    private AutoCompleteTextView jobtitles;
    private AutoCompleteTextView positions;
    ArrayAdapter<String> departmentAdapter,jobtitleAdapter,positionAdapter;
    List<DatumTemplate<Attributes>> departmentsAtts,jobtitlesAtts,positionsAtts;
    List<DatumTemplate<StaffAttributes>> staffAtts;
    String[] departmentNames,jobtitleNames,positionNames,staffNames;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_staff, container, false);
        rcvDes=mView.findViewById(R.id.rcv_departments);
        btn_add=mView.findViewById(R.id.btn_add);
        userAdapter=new UserAdapter();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvDes.addItemDecoration(dividerItemDecoration);
        rcvDes.setAdapter(userAdapter);
        rcvDes.setLayoutManager(linearLayoutManager);
        getData();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "btn_add", Toast.LENGTH_SHORT).show();
                    showFormAddDepartment();
            }
        });
        edtStaffName=mView.findViewById(R.id.edtStaffName);
        btnSearch=mView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String staffName=edtStaffName.getText().toString();
                if(!staffName.equals("")){
                    userAdapter.showLike(staffName);
                } else {
                    userAdapter.showAll();
                }
            }
        });
        departments=mView.findViewById(R.id.edtDepartments);
        jobtitles=mView.findViewById(R.id.edtJobtitle);
        positions=mView.findViewById(R.id.edtPositions);
        Log.d("StaffFragment","onCreateView");
        return mView;
    }

    private void showFormAddDepartment() {
//        final View view = LayoutInflater.from(getContext()).inflate(R.layout.add_staff_dialog, null);
//        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//        alertDialog.setTitle("New Employee");
//        alertDialog.setIcon(R.drawable.add);
//        alertDialog.setCancelable(true);
////        alertDialog.setMessage("Your Message Here");
//        alertDialog.setView(view);
//        alertDialog.show();
            HomeActivity homeActivity= (HomeActivity) getActivity();
            NewEmployeeFragment fragment=new NewEmployeeFragment();
            final Bundle args = new Bundle();
            args.putString("TAG", NewEmployeeFragment.MY_TAG);
            args.putSerializable("departmentAtts", (Serializable) departmentsAtts);
            args.putSerializable("jobtitleAtts", (Serializable) jobtitlesAtts);
            args.putSerializable("positionAtts", (Serializable) positionsAtts);
        args.putSerializable("staffAtts", (Serializable) staffAtts);
            args.putStringArray("departmentNames", departmentNames);
            // Log.d("positionNames put", String.valueOf(departmentNames.length));
            args.putSerializable("jobtitleNames", jobtitleNames);
            args.putSerializable("positionNames", positionNames);
        args.putSerializable("staffNames", staffNames);
            fragment.setArguments(args);
            homeActivity.relaceFragment(fragment);
    }

    private void getData() {
        Call<DataStaff> call=APIService.getService().get_all_staff(Common.getToken());
        call.enqueue(new Callback<DataStaff>() {
            @Override
            public void onResponse(Call<DataStaff> call, Response<DataStaff> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                userAdapter.setData(response.body().getData(),getContext(),(HomeActivity) getActivity());
            }

            @Override
            public void onFailure(Call<DataStaff> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<Attributes>>> call2=APIService.getService().getAllDepartments(Common.getToken());
        call2.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                departmentNames=new String[response.body().getData().size()];
                departmentsAtts= response.body().getData();
                departmentAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, departmentNames);
                for(int i=0;i<departmentsAtts.size();i++){
                    departmentNames[i]=departmentsAtts.get(i).getAttributes().getName();
                }

                departments.setAdapter(departmentAdapter);
                departments.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        departments.showDropDown();
                        return false;
                    }
                });
                departments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("itemclick",departmentsAtts.get(i).getAttributes().getName());
                        Helper.closeKeyboard(getActivity());
                    }
                });
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        //
        Call<DataResponseList<DatumTemplate<Attributes>>> call3=APIService.getService().getAllJobTitles(Common.getToken());
        call3.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                jobtitleNames=new String[response.body().getData().size()];
                jobtitlesAtts= response.body().getData();
                jobtitleAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, jobtitleNames);
                for(int i=0;i<jobtitlesAtts.size();i++){
                    jobtitleNames[i]=jobtitlesAtts.get(i).getAttributes().getTitle();
                }

                jobtitles.setAdapter(jobtitleAdapter);
                jobtitles.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        jobtitles.showDropDown();
                        return false;
                    }
                });
                jobtitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Log.d("itemclick",jobtitlesAtts.get(i).getAttributes().getName());
                        Helper.closeKeyboard(getActivity());
                    }
                });
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<Attributes>>> call4=APIService.getService().getAllPositions(Common.getToken());
        call4.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                positionNames=new String[response.body().getData().size()];
                positionsAtts = response.body().getData();
                positionAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, positionNames);
                for(int i=0;i<positionsAtts.size();i++){
                    positionNames[i]=positionsAtts.get(i).getAttributes().getName();
                }

                positions.setAdapter(positionAdapter);
                positions.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        positions.showDropDown();
                        return false;
                    }
                });
                positions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("itemclick",positionsAtts.get(i).getAttributes().getName());
                        Helper.closeKeyboard(getActivity());
                    }
                });
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<StaffAttributes>>> call5= APIService.getService().getAllStaff(Common.getToken());
        call5.enqueue(new Callback<DataResponseList<DatumTemplate<StaffAttributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Response<DataResponseList<DatumTemplate<StaffAttributes>>> response) {
                Log.d("getAllStaff","onResponse");
                List<StaffAttributes> staffAttributes=new ArrayList<>();

                DataResponseList<DatumTemplate<StaffAttributes>> res=response.body();
                staffAtts=res.getData();
                staffNames=new String[res.getData().size()];
                for(int i=0;i<res.getData().size();i++){
                    StaffAttributes att=res.getData().get(i).getAttributes();
                    staffNames[i]=att.getFullname();
                }
                Common.setStaffs(staffAttributes);
                Common.setStaffNames(staffNames);
                Log.d("names",staffNames[0]);
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Throwable t) {
                Log.d("getAllStaff","onFailure");
            }
        });
    }
}