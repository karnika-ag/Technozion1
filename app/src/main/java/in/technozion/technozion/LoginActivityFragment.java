package in.technozion.technozion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Login
        getActivity().findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged_in", true);
                editor.apply();
                Intent intent=new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        getActivity().findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });


        //Logout
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putBoolean("logged_in",true);
//        editor.apply();


        //Check
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean logged_in=sharedPreferences.getBoolean("logged_in", false);
        if (logged_in) {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean logged_in=sharedPreferences.getBoolean("logged_in", false);
        if (logged_in) {
            getActivity().onBackPressed();
//            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }
}
