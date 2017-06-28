package co.edu.udea.compumovil.gr08_20171.Lab3.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.adapters.AdapterEvents;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;

public class FragmentEvents extends Fragment {

    public RecyclerView recyclerViewEvents;
    public List<Event> eventList;
    public DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerViewEvents = (RecyclerView) rootView.findViewById(R.id.recycler_view_events);

        dbHelper = new DbHelper(getContext()); //Instancia de DbHelper
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(llm);
        recyclerViewEvents.setHasFixedSize(true);
        update();
        getActivity().setTitle("Eventos");

        return rootView;
    }


    public void initializeData() {
        eventList = dbHelper.getAllEvents();
    }

    public void initializeAdapter() {
        AdapterEvents adapter = new AdapterEvents(eventList);
        recyclerViewEvents.setAdapter(adapter);
    }

    public void update() {
        initializeData();
        initializeAdapter();
    }

    public class rvAdapter{
        void update(){
            FragmentEvents.this.update();

        }

    };


}
