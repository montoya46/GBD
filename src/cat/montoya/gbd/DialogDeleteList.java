package cat.montoya.gbd;

import java.util.List;
import cat.montoya.gbd.adapters.DeleteListAdapter;
import cat.montoya.gbd.entity.Chip;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DialogDeleteList extends DialogFragment {

	private List<Chip> chips;
	private OnChipSelectedListener listener;
	private View currentView;
	private Chip currentChip;
	private ListView deleteList;
	
	public interface OnChipSelectedListener {
		void onChipSelectedOccurred(View v, Chip chip);
	}
	
	public void setOnChipSelectedListener(OnChipSelectedListener l) {
        listener = l;
	}
	
	public DialogDeleteList(){
		
	}
	
    public void SetChipList(List<Chip> _chips) {
    	chips = _chips;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	setRetainInstance(true);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.TituloSeleccionaFicha);
        
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                listener.onChipSelectedOccurred(currentView, currentChip);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_list, null);
        deleteList = (ListView) currentView.findViewById(R.id.lvDeleteList);
        deleteList.setAdapter(new DeleteListAdapter(getActivity(), chips));
        deleteList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentChip = chips.get(arg2);
			}
		});
                
        builder.setView(currentView);
        return builder.create();
    }
}
