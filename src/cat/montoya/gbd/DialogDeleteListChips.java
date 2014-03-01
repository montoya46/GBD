package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;
import cat.montoya.gbd.adapters.DeleteListAdapterChips;
import cat.montoya.gbd.entity.Chip;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DialogDeleteListChips extends DialogFragment {

	private List<Chip> chips;
	private OnChipSelectedListener listener;
	private View currentView;
	private Chip currentChip;
	private ListView deleteList;
	private List<Chip> chipsToDelete = new ArrayList<Chip>();
	
	public interface OnChipSelectedListener {
		void onChipSelectedOccurred(View v, List<Chip> chipsToDelete);
	}
	
	public void setOnChipSelectedListener(OnChipSelectedListener l) {
        listener = l;
	}
	
	public DialogDeleteListChips(){
		
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
                listener.onChipSelectedOccurred(currentView, chipsToDelete);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_list, null);
        deleteList = (ListView) currentView.findViewById(R.id.lvDeleteList);
        deleteList.setAdapter(new DeleteListAdapterChips(getActivity(), chips));
        
        deleteList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentChip = chips.get(arg2);
				
				if(chipsToDelete.contains(currentChip)){
					chipsToDelete.remove(currentChip);
					arg1.setBackgroundColor(Color.parseColor("#E5E5E5"));
				}
				else {
					chipsToDelete.add(chips.get(arg2));
					arg1.setBackgroundColor(Color.parseColor("#FEA408"));
				}
			}
		});
                
        builder.setView(currentView);
        return builder.create();
    }
}
