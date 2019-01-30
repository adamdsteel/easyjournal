package xyz.adamsteel.easyjournal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeleteDialogFragment extends DialogFragment {

    DeleteDialogListener dListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_confirmation);
        builder.setPositiveButton(R.string.delete_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Get the ID of the entry to delete:
                Bundle loadedArgs = getArguments();
                int toDel = (int)loadedArgs.get("toDelete");
                ((DeleteDialogListener)getContext()).onDeleteConfirm(toDel);
            }
        });

        builder.setNegativeButton(R.string.delete_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO:
            }
        });
        return builder.create();
    }

    public interface DeleteDialogListener{
        public void onDeleteConfirm(int dbID);
        public void onDeleteCancel();
    }

    @Override public void onAttach(Context context){
        super.onAttach(context);

        try{
            dListener = (DeleteDialogListener)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().getLocalClassName() + " does not implement DeleteDialogListener");
        }
    }
}
