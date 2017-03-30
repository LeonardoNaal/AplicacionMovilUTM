package com.fsociety.linkutmbetty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by axl50 on 29/03/2017.
 */

public class FilterAdapter extends ArrayAdapter {
    public FilterAdapter(Context context, List<SpinnerItem> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /*
       Obtener el objeto procesado actualmente
        */
        SpinnerItem currentItem = (SpinnerItem) getItem(position);
        /*
        Obtener LayoutInflater de la actividad
         */
        LayoutInflater inflater = (LayoutInflater) parent.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
        Evitar inflar de nuevo un elemento previamente inflado
         */
        if(convertView==null){
            convertView = inflater.inflate(R.layout.layaout_spinner, parent, false);
        }
        /*
        Instancias del Texto y el Icono
         */
        TextView name = (TextView)convertView.findViewById(R.id.lblSitio);
        ImageView icon = (ImageView)convertView.findViewById(R.id.imgIcon);
        /*
        Asignar valores
         */
        name.setText(currentItem.getName());
        icon.setImageResource(currentItem.getIconId());
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /*
        Debido a que deseamos usar spinner_item.xml para inflar los
        items del Spinner en ambos casos, entonces llamamos a getView()
         */
        return getView(position,convertView,parent);
    }
}