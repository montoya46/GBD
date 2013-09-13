package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;
import cat.montoya.gbd.adapters.ShapeAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class DialogShapePicker extends DialogFragment {

	private List<Integer> formas;
	private OnShapeSelectedListener listener;
	private View currentView;
	private GridView gvFormas;
	private int currentForma;
	private ImageView ibCurrentForma;
	
	public interface OnShapeSelectedListener {
		void onShapeSelectedOccurred(View v, int shape);
	}
	
	public void setOnShapeSelectedListener(OnShapeSelectedListener l) {
        listener = l;
	}
	
    public DialogShapePicker() {
    	formas = new ArrayList<Integer>();
    	formas.add(R.drawable.circle128);
    	formas.add(R.drawable.square128);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	setRetainInstance(true);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.TituloSeleccionaForma);
        
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                listener.onShapeSelectedOccurred(currentView, currentForma);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_shape_picker, null);
        currentForma = formas.get(0);
        
        gvFormas = (GridView) currentView.findViewById(R.id.gvDialogFormas);
        gvFormas.setAdapter(new ShapeAdapter(getActivity(), formas));
        gvFormas.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentForma = formas.get(arg2);
				ibCurrentForma.setImageResource(currentForma);
			}
		});
        
        ibCurrentForma = (ImageView) currentView.findViewById(R.id.ibCurrentForma);
        ibCurrentForma.setImageResource(currentForma);
        
        builder.setView(currentView);
        return builder.create();
    }
}
