package com.example.angga.coba2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class cobaActivity  extends Activity implements OnClickListener {

    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button popupButton, insidePopupButton;
    TextView popupText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);
        init();
        popupInit();


    }

    public void init() {
        popupButton = (Button) findViewById(R.id.popupbutton);
        popupText = new TextView(this); insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        insidePopupButton.setText("OK");
        popupText.setText("This is Popup Window.press OK to dismiss it.");
        popupText.setPadding(0, 0, 0, 20);
        layoutOfPopup.setBackgroundResource(R.color.black);
        layoutOfPopup.setOrientation(1);
        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(insidePopupButton);
    }
    public void popupInit() {
        popupButton.setOnClickListener(this);
        insidePopupButton.setOnClickListener(this);
        popupMessage = new PopupWindow(layoutOfPopup, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup); }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.popupbutton) {
            popupMessage.showAsDropDown(popupButton, 0, 0);
        }
        else { popupMessage.dismiss(); }
    }


}
