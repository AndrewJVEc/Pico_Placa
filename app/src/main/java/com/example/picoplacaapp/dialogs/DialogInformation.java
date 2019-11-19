package com.example.picoplacaapp.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.picoplacaapp.R;

public class DialogInformation extends DialogFragment {

    private TextView txtTitle;
    private TextView txtContent;
    private Button btnOK;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_information);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        initializeItems(dialog);

        String title = "";
        String content = "";
        if (getArguments() != null) {
            title = getArguments().getString("TITLE_VALUE");
            content = getArguments().getString("CONTENT_VALUE");
        }

        if (title != null && !title.isEmpty() && content != null && !content.isEmpty()) {
            txtTitle.setText(title);
            txtContent.setText(content);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialog;
    }

    private void initializeItems(Dialog dialog) {
        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtContent = dialog.findViewById(R.id.txtContent);
        btnOK = dialog.findViewById(R.id.btnOK);
    }
}
