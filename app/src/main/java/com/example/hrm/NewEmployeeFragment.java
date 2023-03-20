package com.example.hrm;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrm.Response.Attributes;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.FragmentNewEmployeeBinding;
import com.example.hrm.viewmodel.NewEmployeeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEmployeeFragment extends Fragment {
    public  static final String MY_TAG= "NewEmployeeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewEmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEmployeeFragment newInstance(String param1, String param2) {
        NewEmployeeFragment fragment = new NewEmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    List<DatumTemplate<Attributes>> departmentsAtts,jobtitlesAtts,positionsAtts;
    List<DatumTemplate<StaffAttributes>> staffAtts;
    String[] departmentNames,jobtitleNames,positionNames,staffNames;
    ArrayAdapter<String> departmentAdapter,jobtitleAdapter,positionAdapter,staffAdapter;
    int posId,jobId,deId,staffId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            departmentsAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("departmentAtts");
            jobtitlesAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("jobtitleAtts");
            positionsAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("positionAtts");
            staffAtts= (List<DatumTemplate<StaffAttributes>>) getArguments().getSerializable("staffAtts");
            departmentNames=getArguments().getStringArray("departmentNames");
            jobtitleNames=getArguments().getStringArray("jobtitleNames");
            positionNames=getArguments().getStringArray("positionNames");
            staffNames=getArguments().getStringArray("staffNames");
            //
            departmentAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, departmentNames);
            jobtitleAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, jobtitleNames);
            positionAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, positionNames);
            staffAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, staffNames);
        }
    }
    FragmentNewEmployeeBinding fragmentNewEmployeeBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        // Inflate the layout for this fragment
        NewEmployeeViewModel viewModel=new NewEmployeeViewModel();
        fragmentNewEmployeeBinding=FragmentNewEmployeeBinding.inflate(inflater);
        fragmentNewEmployeeBinding.setNewEmployeeViewModel(viewModel);
        fragmentNewEmployeeBinding.edtDepartments.setAdapter(departmentAdapter);
        fragmentNewEmployeeBinding.edtDepartments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtDepartments.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deId=departmentsAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtJobtitle.setAdapter(jobtitleAdapter);
        fragmentNewEmployeeBinding.edtJobtitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtJobtitle.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtJobtitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jobId=jobtitlesAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtPositions.setAdapter(positionAdapter);
        fragmentNewEmployeeBinding.edtPositions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtPositions.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtPositions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posId=positionsAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtManager.setAdapter(staffAdapter);
        fragmentNewEmployeeBinding.edtManager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtManager.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                staffId=staffAtts.get(i).getAttributes().getId();
            }
        });
        //
        final EditText edt_birth_day = fragmentNewEmployeeBinding.idEdtDateOfBirth;
        //TextView txt_message= view2.findViewById(R.id.txt_message);
        edt_birth_day.setText("");
        edt_birth_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "-" + (++i1) + "-" + i;
                        edt_birth_day.setText(date);
                    }
                }, LocalDate.now().getYear(),LocalDate.now().getMonth().getValue()-1,LocalDate.now().getDayOfMonth());
                datePickerDialog.show();
            }
        });

        final EditText edt_join_day= fragmentNewEmployeeBinding.idEdtJoinDate;
        edt_join_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "-" + (++i1) + "-" + i;
                        edt_join_day.setText(date);
                    }
                },LocalDate.now().getYear(),LocalDate.now().getMonth().getValue()-1,LocalDate.now().getDayOfMonth());
                datePickerDialog.show();
            }
        });
        fragmentNewEmployeeBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.isSubmited()){
                    sendData();
                }
                viewModel.setSubmited(true);

            }
        });
        return fragmentNewEmployeeBinding.getRoot();
    }

    private void sendData() {
        JSONObject dataParent=new JSONObject();
        JSONObject dataChild=new JSONObject();
        try {
            dataChild.put("address", fragmentNewEmployeeBinding.txtAddress.getText().toString().trim());
            dataChild.put("date_of_birth", (new StringBuffer(fragmentNewEmployeeBinding.idEdtDateOfBirth.getText().toString().trim())).reverse());
            dataChild.put("department_id", deId);
            dataChild.put("email", fragmentNewEmployeeBinding.txtEmail.getText().toString().trim());
            dataChild.put("fullname", fragmentNewEmployeeBinding.txtFullname.getText().toString().trim());
            dataChild.put("gender", fragmentNewEmployeeBinding.txtGender.getText().toString().trim());
            dataChild.put("job_title_id", jobId);
            dataChild.put("join_date", (new StringBuffer(fragmentNewEmployeeBinding.idEdtJoinDate.getText().toString().trim())).reverse());
            dataChild.put("password", fragmentNewEmployeeBinding.txtPassword.getText().toString().trim());
            dataChild.put("phone", fragmentNewEmployeeBinding.txtPhone.getText().toString().trim());
            dataChild.put("position_id", posId);
            dataChild.put("staff_id",staffId);
            dataParent.put("staff",dataChild);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), dataParent.toString());
            Call call= APIService.getServiceJson().addStaff(body,Common.getToken());
            Response response=call.execute();
            LayoutInflater layoutInflater=getLayoutInflater();
            View layout=null;
            int px=Common.getValueInPixel(getContext(),300)/10;
            if(response.isSuccessful()){
                //Toast.makeText(getContext(), "Add staff successfully", Toast.LENGTH_SHORT).show();

                layout = layoutInflater.inflate(R.layout.toast_success,(ViewGroup) (getActivity().findViewById(R.id.custom_toast_layout)));
                Toast toast = new Toast(getContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setView(layout);//setting the view of custom toast layout
                toast.show();

                Log.d("addstaff","ok");
                new Timer().scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run(){
                        LinearLayout line=toast.getView().findViewById(R.id.line_count_down);
                        ViewGroup.LayoutParams params=line.getLayoutParams();
                        params.width=params.width-px;
                        line.setLayoutParams(params);
                        Log.d("linew", String.valueOf(line.getLayoutParams().width));
                        Log.d("linewpx", String.valueOf(px));
                    }
                },0,100);
            } else {
                layout = layoutInflater.inflate(R.layout.toast_failed,(ViewGroup) (getActivity().findViewById(R.id.custom_toast_layout)));
                Toast toast = new Toast(getContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setView(layout);//setting the view of custom toast layout
                //toast.show();
                Log.d("addstaff","failed");
//                Handler handler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    }
//                });
                LinearLayout line=toast.getView().findViewById(R.id.line_count_down);
                CountDownTimer countDownTimer;
                countDownTimer = new CountDownTimer(5000, 200) {
                    public void onTick(long millisUntilFinished) {

                        ViewGroup.LayoutParams params=line.getLayoutParams();
                        params.width=params.width-px;
                        line.setLayoutParams(params);
                        Log.d("linew", String.valueOf(line.getLayoutParams().width));
                        //Log.d("linewpx", String.valueOf(px));
                        toast.show();
                    }

                    public void onFinish() {
                        toast.cancel();
                    }
                };

                toast.show();
                countDownTimer.start();

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void checkIsEmpty() {

    }
}