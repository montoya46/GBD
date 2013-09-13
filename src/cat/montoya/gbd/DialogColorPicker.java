package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;

import cat.montoya.gbd.adapters.ColorAdapter;
import cat.montoya.gbd.adapters.ImageAdapter;

import android.R.color;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class DialogColorPicker extends DialogFragment {

	private List<Integer> colores;
	
	private OnColorSelectedListener listener;
	private View currentView;
	private GridView gvColores;
	private int currentColor;
	private RelativeLayout rlCurrentColor;
	
	public interface OnColorSelectedListener {
		void onColorSelectedOccurred(View v, int color);
	}
	
	public void setOnColorSelectedListener(OnColorSelectedListener l) {
        listener = l;
	}
	
	public int GetDefaultColor(){
		return colores.get(0);
	}
	
    public DialogColorPicker() {
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
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	setRetainInstance(true);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.TituloSeleccionaColor);
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                listener.onColorSelectedOccurred(currentView, currentColor);
            }
    	});
        builder.setNegativeButton(R.string.Cancelar,
        	    new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        currentColor = colores.get(0);
        
        gvColores = (GridView) currentView.findViewById(R.id.gvDialogColores);
        gvColores.setAdapter(new ColorAdapter(getActivity(), colores));
        gvColores.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentColor = colores.get(arg2);
				rlCurrentColor.setBackgroundColor(currentColor);
			}
		});
        
        rlCurrentColor = (RelativeLayout) currentView.findViewById(R.id.rlDialogCurrentColor);
        rlCurrentColor.setBackgroundColor(currentColor);
        
        builder.setView(currentView);
        return builder.create();
    }
}
