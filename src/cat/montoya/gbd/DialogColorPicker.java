package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;

import cat.montoya.gbd.adapters.ColorAdapter;
import cat.montoya.gbd.adapters.ImageAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;

public class DialogColorPicker extends DialogFragment {

	private List<Integer> colores;
	
    public DialogColorPicker() {
        // Empty constructor required for DialogFragment
    	colores = new ArrayList<Integer>();
    	colores.add(Color.BLACK);
    	colores.add(Color.DKGRAY);
    	colores.add(Color.GRAY);
    	colores.add(Color.LTGRAY);
    	colores.add(Color.BLUE);
    	colores.add(Color.CYAN);
    	colores.add(Color.GREEN);
    	colores.add(Color.RED);
    	colores.add(Color.MAGENTA);
    	colores.add(Color.YELLOW);
    	
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    	
//        View view = inflater.inflate(R.layout.dialog_color_picker, container);
//        GridView gvColores = (GridView) view.findViewById(R.id.gvColores);
//
//        return view;
//    }
    
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        super.onViewCreated(view, savedInstanceState);
//    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle(R.string.TituloSeleccionaColor);
        
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar,
        	    new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
            }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        GridView gvColores = (GridView) view.findViewById(R.id.gvDialogColores);
        gvColores.setAdapter(new ColorAdapter(getActivity(), colores));
        builder.setView(view);

        return builder.create();
    }
}
