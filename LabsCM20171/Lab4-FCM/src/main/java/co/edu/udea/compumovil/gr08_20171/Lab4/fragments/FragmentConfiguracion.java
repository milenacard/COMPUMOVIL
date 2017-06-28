package co.edu.udea.compumovil.gr08_20171.Lab4.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import co.edu.udea.compumovil.gr08_20171.Lab4.R;

public class FragmentConfiguracion extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private OnFragmentInteractionListener mListener;

    public FragmentConfiguracion() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        getActivity().setTitle("Configuraciones");
        android.support.v7.preference.Preference button = findPreference("editarPerfil");
        button.setOnPreferenceClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void editarPerfil() {
        if (mListener != null) {
            mListener.llamadoAEditarPerfil();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        editarPerfil();

        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void llamadoAEditarPerfil();
    }
}
