package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr08_20171.Lab2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAbout extends Fragment {


    public FragmentAbout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Acerca de");
        return inflater.inflate(R.layout.fragment_fragment_about, container, false);

    }

}
