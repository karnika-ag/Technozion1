package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.MainActivity;
import in.technozion.technozion.R;
import in.technozion.technozion.WebViewActivity;
import in.technozion.technozion.WorkshopPaymentDetailsActivity;


public class TShirtsFragment extends Fragment {
    public static final String TAG = "Tshirts";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private EditText editText;
    private LinearLayout linearLayout;


    public static TShirtsFragment newInstance(int sectionNumber) {
        TShirtsFragment fragment = new TShirtsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_t_shirts, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayout=(LinearLayout)getActivity().findViewById(R.id.linearLayoutTshirts);
        editText = (EditText)getActivity().findViewById(R.id.editTextNoOfParticipants);
        getActivity().findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (!hasFocus) {
//                            //SAVE THE DATA
                            linearLayout.removeAllViews();
                            String text = editText.getText().toString();
                            Log.d("tshirt string:", text);
                            int number = 0;
                            if (text != null)
                                number = Integer.parseInt(text);
                            //      int number = 2;

                            int cost = 220*number;
                            Log.d("cost",String.valueOf(cost));
                            ((TextView)getActivity().findViewById(R.id.textViewPayment)).setText("You have to pay " + String.valueOf(cost));
                            getActivity().findViewById(R.id.textViewPayment).setVisibility(View.VISIBLE);

                            if(number>0)
                                getActivity().findViewById(R.id.textViewSize).setVisibility(View.VISIBLE);
                            else {
                                getActivity().findViewById(R.id.textViewSize).setVisibility(View.INVISIBLE);
                            }


                            String[] strings = {"Small", "Medium", "Large", "XL", "XXL"};
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);

                            int j;
                            for (j = 0; j < number; j++) {
                                Log.d("hello", ".");
                                View view2 = getActivity().getLayoutInflater().inflate(R.layout.spinner_tshirt_size, null);
                                ((Spinner) view2).setAdapter(arrayAdapter);
                                linearLayout.addView(view2);
                            }
//                        }

//                    }
//                });

            }
            });

  /*      yourEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                linearLayout.removeAllViews();
                String text = yourEditText.getText().toString();
                Log.d("tshirt string:", text);
           /*     int min = 2;
                int j;
                for(j=0;j<min;j++){
                    Log.d("hello",".");
                    View view1=getActivity().getLayoutInflater().inflate(R.layout.edit_text_tz_id,null);
//            view1.setEnabled(false);
                    ((EditText)view1).setHint("*required");
                    linearLayout.addView(view1);
                }

                int number=0;
                if(text!=null)
                number=Integer.parseInt(text);
         //      int number = 2;

                String[] strings={"Small","Medium","Large","XL","XXL"};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);

                int j;
                for(j=0;j<number;j++) {
                    Log.d("hello",".");
                    View view2 = getActivity().getLayoutInflater().inflate(R.layout.spinner_tshirt_size, null);
                    ((Spinner) view2).setAdapter(arrayAdapter);
                    linearLayout.addView(view2);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("hmm",".");
            }
        });

*/

        getActivity().findViewById(R.id.buttonPurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String, String> hashMap = new HashMap<String, String>();
                int count=linearLayout.getChildCount();
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String tzid=sharedPreferences.getString("userid","");
                    hashMap.put("userid",tzid);
                    hashMap.put("numtshirt", String.valueOf(count));
                    try {
                        int j=0;
                        String[] tshirt_size = new String[count];
                        for(j=0;j<count;j++)
                        {
                            String text=((Spinner) linearLayout.getChildAt(j)).getSelectedItem().toString();
                            if(text.equalsIgnoreCase("Small"))
                                tshirt_size[j] ="S";
                            if(text.equalsIgnoreCase("Medium"))
                                tshirt_size[j] ="M";
                            if(text.equalsIgnoreCase("Large"))
                                tshirt_size[j] ="L";
                            if(text.equalsIgnoreCase("XL"))
                                tshirt_size[j] ="XL";
                            if(text.equalsIgnoreCase("XXL"))
                                tshirt_size[j] ="XXL";

                            Log.d("check-size", tshirt_size[j]);
                            jsonArray.put(j,tshirt_size[j]);

                        }
                        jsonObject.put("tshirt_size",jsonArray);
                        Log.d("sizes",jsonObject.toString());
                        hashMap.put("data", jsonObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Please check the details", Toast.LENGTH_SHORT).show();
                }


                new BuyTShirtTask().execute(hashMap);


            }
        });

            }

    public class BuyTShirtTask extends AsyncTask<HashMap<String,String>, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Varifying Info...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String,String>... hashMaps) {

            String jsonstr = Util.getStringFromURL(URLS.BUY_TSHIRT_URL, hashMaps[0]);

            if (jsonstr != null) {
                Log.d("GOT FROM HTTP", jsonstr);

                return jsonstr;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
                if (string == null) {
                    Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getActivity(),"Going to launch webViewer", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(), WebViewActivity.class);
                    i.putExtra("data", string);
                    startActivity(i);
                }
        }
    }


}