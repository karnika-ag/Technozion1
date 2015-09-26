package in.technozion.technozion.nav_bar_fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.technozion.technozion.R;


public class TShirtsFragment extends Fragment {
    public static final String TAG = "Tshirts";
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TShirtsFragment newInstance(int sectionNumber) {
        TShirtsFragment fragment = new TShirtsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_t_shirts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
