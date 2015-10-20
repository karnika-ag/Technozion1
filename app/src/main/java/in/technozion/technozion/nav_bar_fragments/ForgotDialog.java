package in.technozion.technozion.nav_bar_fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.R;

/**
 * Created by Sai Teja on 10/20/2015.
 */

public class ForgotDialog extends DialogFragment implements View.OnClickListener {
    String s;
    EditText editText;
    Button yes;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialog_forgotpassword,null);
        getDialog().setTitle("Please Enter EmailId");

        editText= (EditText) v.findViewById(R.id.forgotEmail);
        yes= (Button) v.findViewById(R.id.buttonYes);
        yes.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonYes)
        {

            s = String.valueOf(editText.getText());
            new ForgotTask().execute(s);
            dismiss();
        }
    }


    public class ForgotTask extends AsyncTask< String,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            Log.d("email",s);
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Sending..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0]==null) return null;
            HashMap<String,String> data=new HashMap();
            data.put("email", strings[0]);
            String jsonstr= Util.getStringFromURL(URLS.forgotLink, data);
            Log.d("forgotMessage",jsonstr);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    String s=jsonObject.getString("message");
                    return s;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void  onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            if(s==null)
                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
            else
            {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();}
            return;
        }
    }
}
