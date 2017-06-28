package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab2.R;


public class FragmentPerfil extends Fragment {

    private ImageView pPhotoUser;
    private TextView pNameUser;
    private TextView pEmail;
    private TextView pAge;
    private DbHelper dbHelper;


    public FragmentPerfil() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Perfil");

        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);

        //pPhotoUser = (ImageView) view.findViewById(R.id.perfil_photo_usuario);
        pNameUser = (TextView) view.findViewById(R.id.perfil_nombre_usuario);
        pEmail = (TextView) view.findViewById(R.id.perfil_email);
        pAge = (TextView) view.findViewById(R.id.perfil_edad_usuario);

        //Picasso.with(pPhotoUser.getContext()).load(thisEvent.getPhoto()).into(photo_Detail);
        pNameUser.setText(getArguments().getString(DBInfomation.ColumnUser.USERNAME));
        pEmail.setText(getArguments().getString(DBInfomation.ColumnUser.EMAIL));
        pAge.setText(getArguments().getString(DBInfomation.ColumnUser.AGE));

        return  view;
    }

}
