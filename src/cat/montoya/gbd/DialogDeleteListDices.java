package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;
import cat.montoya.gbd.adapters.DeleteListAdapterDices;
import cat.montoya.gbd.entity.Dice;
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

public class DialogDeleteListDices extends DialogFragment {

	private List<Dice> dices;
	private OnDeleteDiceSelectedListener listener;
	private View currentView;
	private Dice currentDice;
	private ListView deleteList;
	private List<Dice> dicesToDelete = new ArrayList<Dice>();
	
	public interface OnDeleteDiceSelectedListener {
		void onDiceSelectedOccurred(View v, List<Dice> dicesToDelete);
	}
	
	public void setOnDeleteDiceSelectedListener(OnDeleteDiceSelectedListener l) {
        listener = l;
	}
	
	public DialogDeleteListDices(){
		
	}
	
    public void SetDiceList(List<Dice> _dices) {
    	dices = _dices;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	setRetainInstance(true);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.TituloSeleccionaFicha);
        
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                listener.onDiceSelectedOccurred(currentView, dicesToDelete);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_list, null);
        deleteList = (ListView) currentView.findViewById(R.id.lvDeleteList);
        deleteList.setAdapter(new DeleteListAdapterDices(getActivity(), dices));
        
        deleteList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentDice = dices.get(arg2);
				
				if(dicesToDelete.contains(currentDice)){
					dicesToDelete.remove(currentDice);
					arg1.setBackgroundColor(Color.parseColor("#E5E5E5"));
				}
				else {
					dicesToDelete.add(dices.get(arg2));
					arg1.setBackgroundColor(Color.RED);
				}
			}
		});
                
        builder.setView(currentView);
        return builder.create();
    }
}
