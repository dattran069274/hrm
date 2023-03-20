package com.example.hrm;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.databinding.AssignPropertyDialogBinding;
import com.example.hrm.databinding.FragmentDetailPropertyBinding;
import com.example.hrm.Response.PropertyAttributes;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailPropertyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailPropertyFragment extends Fragment {
    public  static final String MY_TAG= "DetailPropertyFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailPropertyFragment() {
        // Required empty public constructor
    }
    PropertyAttributes attributes;
    public DetailPropertyFragment(PropertyAttributes att) {
    this.attributes=att;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailPropertyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailPropertyFragment newInstance(String param1, String param2) {
        DetailPropertyFragment fragment = new DetailPropertyFragment();
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
    FragmentDetailPropertyBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentDetailPropertyBinding.inflate(inflater);
        if(attributes.getStatus().equals(Common.STATUS_USED)){
            binding.btnAssign.setVisibility(View.GONE);
            binding.txtStatus.setBackground(getContext().getDrawable(R.drawable.layout_rounded_border_red));
            binding.txtStatus.setTextColor(getContext().getColor(R.color.toast_failed_bold));
        } else {
            binding.btnRecall.setVisibility(View.GONE);
            //GradientDrawable myGrad = (GradientDrawable)binding.txtStatus.getBackground();
            //myGrad.setStroke(Common.convertDpToPx(3), getContext().getColor(R.color.toast_success_bold));
            binding.txtStatus.setBackground(getContext().getDrawable(R.drawable.layout_rounded_border_green));
            binding.txtStatus.setTextColor(getContext().getColor(R.color.toast_success_bold));
        }
        binding.txtStatus.setText(attributes.getStatus());
        binding.txtName.setText(attributes.getName());
        binding.txtBrand.setText(attributes.getBrand());
        binding.txtBuyDate.setText(attributes.getDateBuy());
        binding.txtCodeSeri.setText(attributes.getCodeSeri());
        binding.txtNumberOfRepairs.setText(attributes.getNumberOfRepairs().toString());
        binding.txtGroupProperty.setText(attributes.getGroupProperty().getName());
        binding.txtPrice.setText(attributes.getPrice().toString());
        binding.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssignDialog(view);
            }
        });
        return binding.getRoot();
    }
    List<StaffAttributes> staff;
    ArrayList<String> staffNames=new ArrayList<>();
    interface  Ido{
        void updateDialog(AssignPropertyDialogBinding viewBinding );
    }
    int staffId=-1;
    private void showAssignDialog(View view) {
        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.assign_property_dialog, null);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view2.findViewById(R.id.AutoCompleteTextView);
        final ImageView btn_close = (ImageView) view2.findViewById(R.id.btn_close);
        final TextView txtSubmit = (TextView) view2.findViewById(R.id.txtSubmit);
        final TextView txt_mess = (TextView) view2.findViewById(R.id.txt_mess);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            if(staff==null) {
                staff=new ArrayList<>();
                Call<DataResponseList<DatumTemplate<StaffAttributes>>> call = APIService.getService().getAllStaff(Common.getToken());
                try {
                    Response<DataResponseList<DatumTemplate<StaffAttributes>>> response = call.execute();
                    response.body().getData().forEach(item->{staff.add(item.getAttributes());staffNames.add(item.getAttributes().getFullname());});
                    autoCompleteTextView.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,staffNames.toArray(new String[staffNames.size()])));
                    alertDialog.setView(view2);
                    alertDialog.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                autoCompleteTextView.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,staffNames.toArray(new String[staffNames.size()])));
                alertDialog.setView(view2);
                alertDialog.show();
        }
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autoCompleteTextView.showDropDown();
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                staffId=i;
            }
        });
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String staff=autoCompleteTextView.getText().toString();
                    if(TextUtils.isEmpty(staff)){
                        txt_mess.setVisibility(View.GONE);
                        return;
                    }
                    if(staffId!=-1) {
                        JSONObject data=new JSONObject();
                        try {
                            data.put("receiver_id", String.valueOf(staffId));
                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data.toString());
                            Call<ResponseBody> call = APIService.getService().responsePropertyRequest(Common.getToken(), attributes.getId(), body);
                            Response<ResponseBody> res = call.execute();
                            View toast=null;
                            if(res.isSuccessful()){
                                //data change
                                alertDialog.dismiss();
                                toast=getLayoutInflater().inflate(R.layout.toast_success,null);
                            }
                            else {
                                toast=getLayoutInflater().inflate(R.layout.toast_failed,null);
                            }

                            toast.setLayoutParams(new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            toast.setId(Integer.parseInt("06901"));
                            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) toast.getLayoutParams();
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            binding.relativeMain.addView(toast);
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.relativeMain.removeView(binding.getRoot().findViewById(Integer.parseInt("06901")));
                                }
                            },2000);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            }
        });
    }

}