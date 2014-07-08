package upv.welcomeincoming.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import util.Preferencias;


public class Dialog_Login extends DialogFragment {

    private Dialog_Login I = this;

    public Dialog_Login() {
        // Empty constructor required for DialogFragment
    }

    public interface EditDialogLoginListener {
        void EditDialogLoginListener(int button);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle(getString(R.string.login));
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

                        String stringUser = ((EditText) view.findViewById(R.id.editTextUser)).getText().toString().trim();
                        String stringPass = ((EditText) view.findViewById(R.id.editTextPass)).getText().toString().trim();

                        if (!stringUser.equals("") && !stringPass.equals("")) {

                            Preferencias.setPIN(I.getActivity(), stringPass);
                            Preferencias.setDNI(I.getActivity(), stringUser);

                            EditDialogLoginListener activity = (EditDialogLoginListener) getActivity();
                            activity.EditDialogLoginListener(DialogInterface.BUTTON_POSITIVE);
                        } else {
                            EditDialogLoginListener activity = (EditDialogLoginListener) getActivity();
                            activity.EditDialogLoginListener(DialogInterface.BUTTON_NEUTRAL);
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
