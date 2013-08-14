package cat.montoya.gbd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

public class DialogColorPicker extends DialogFragment {
	private EditText mEditText;

    public DialogColorPicker() {
        // Empty constructor required for DialogFragment
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    	
//        View view = inflater.inflate(R.layout.dialog_color_picker, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        getDialog().setTitle("Hello");
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
//        return new AlertDialog.Builder(getActivity())
//                .setPositiveButton("OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            
//                        }
//                    }
//                )
//                .setNegativeButton("Cancel",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            
//                        }
//                    }
//                )
//                .create();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
            }
    	});
        
        builder.setNegativeButton("Cancel",
        	    new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
            }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        builder.setView(view);

        return builder.create();
    }
}
