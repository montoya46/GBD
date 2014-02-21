package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;

import cat.montoya.gbd.adapters.DiceAdapter;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.utils.ImageSelectorUtils;
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
	private List<Dice.DiceType> dices;
	private OnDiceSelectedListener listener;
	private View currentView;
	private GridView gvDice;
	private Dice.DiceType currentDice;
	private ImageView ibCurrentDice;
	
	public interface OnDiceSelectedListener {
		void onDiceSelectedOccurred(View v, Dice.DiceType dice);
	}
	
	public void setOnShapeSelectedListener(OnDiceSelectedListener l) {
        listener = l;
	}
	
	public Dice.DiceType GetDefaultDice(){
		return dices.get(0);
	}
	
    public DialogDicePicker() {
    	dices = new ArrayList<Dice.DiceType>();
    	dices.add(Dice.DiceType.STANDARD);
    	dices.add(Dice.DiceType.TETRAAEDRO);
    	dices.add(Dice.DiceType.HEXAEDRO);
    	dices.add(Dice.DiceType.OCTAEDRO);
    }
    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	setRetainInstance(true);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.TituloSeleccionaForma);
        
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                listener.onDiceSelectedOccurred(currentView, currentDice);
            }
    	});
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        });

        currentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_dice_picker, null);
        currentDice = dices.get(0);
        
        gvDice = (GridView) currentView.findViewById(R.id.gvDialogDices);
        gvDice.setAdapter(new DiceAdapter(getActivity(), dices));
        
        gvDice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentDice = dices.get(arg2);
				ibCurrentDice.setImageResource(ImageSelectorUtils.SelectImg(currentDice));
			}
		});
        
        ibCurrentDice = (ImageView) currentView.findViewById(R.id.ibCurrentDice);
        ibCurrentDice.setImageResource(ImageSelectorUtils.SelectImg(currentDice));
        
        builder.setView(currentView);
        return builder.create();
    }
}
