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

public class DialogDicePicker extends DialogFragment {

	private List<Integer> formas;
	private OnShapeSelectedListener listener;
	private View currentView;
	private GridView gvDice;
	private int currentDice;
	private ImageView ibCurrentDice;
	
	public interface OnShapeSelectedListener {
		void onShapeSelectedOccurred(View v, int shape);
	}
	
	public void setOnShapeSelectedListener(OnShapeSelectedListener l) {
        listener = l;
	}
	
	public int GetDefaultShape(){
		return formas.get(0);
	}
	
    public DialogDicePicker() {
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
                listener.onShapeSelectedOccurred(currentView, currentDice);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_shape_picker, null);
        currentDice = formas.get(0);
        
        gvDice = (GridView) currentView.findViewById(R.id.gvDialogDices);
        gvDice.setAdapter(new ShapeAdapter(getActivity(), formas));
        gvDice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentDice = formas.get(arg2);
				ibCurrentDice.setImageResource(currentDice);
			}
		});
        
        ibCurrentDice = (ImageView) currentView.findViewById(R.id.ibCurrentForma);
        ibCurrentDice.setImageResource(currentDice);
        
        builder.setView(currentView);
        return builder.create();
    }
}
