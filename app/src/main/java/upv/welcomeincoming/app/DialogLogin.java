package upv.welcomeincoming.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import util.Preferences;


public class DialogLogin extends DialogFragment{

    private DialogLogin I = this;

    public DialogLogin() {
        // Empty constructor required for DialogFragment
    }

    public interface EditDialogLoginListener {
        void EditDialogLoginListener(int button);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle("Login...");
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.fragment_calendar_login, null);

        builder.setView(view)
            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    Log.d(((Object) this).getClass().getName(), "Ok");

                    String stringUser = ((EditText) view.findViewById(R.id.editTextUser)).getText().toString().trim();
                    String stringPass = ((EditText) view.findViewById(R.id.editTextPass)).getText().toString().trim();

                    if (!stringUser.equals("") && !stringPass.equals("")) {

                        Preferences.setPass(I.getActivity(), stringPass);
                        Preferences.setUser(I.getActivity(), stringUser);

                        Log.d(((Object) this).getClass().getName(), "User: " + stringUser);
                        Log.d(((Object) this).getClass().getName(), "Pass: " + stringPass);

                        EditDialogLoginListener activity = (EditDialogLoginListener) getActivity();
                        activity.EditDialogLoginListener(DialogInterface.BUTTON_POSITIVE);
                    }
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    EditDialogLoginListener activity = (EditDialogLoginListener) getActivity();
                    activity.EditDialogLoginListener(DialogInterface.BUTTON_NEGATIVE);

                }
            });

        return builder.create();
    }
}
