package com.example.accidentdetection.ui.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accidentdetection.DrawerActivity;
import com.example.accidentdetection.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class TimerDialogFragment extends DialogFragment {


    private static final int SMS_PERMISSION_REQUEST_CODE = 123;
    private TextView countdownText;
    private Button continueButton;
    private Button cancelButton;

    private String message;

    private CountDownTimer countDownTimer;

    public interface TimerDialogListener {
        void onContinue();

        void onCancel();

        void onFinish();
    }



    public TimerDialogFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer_dialog, container, false);
    }





    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_timer_dialog, null);

        countdownText = view.findViewById(R.id.countdown_text);
        continueButton = view.findViewById(R.id.continue_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        builder.setView(view);

        // Set the countdown timer for 15 seconds
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                countdownText.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                    // Permission not granted, request it
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
//                } else {
//                    // Permission already granted, send SMS
//                    openGoogleMapsNavigation();
//                    sendSMS("ACCIDENT!!!!! "+"click on the following link to find the route for accident. "+message);
//
//
//                }

                TimerDialogListener listener = (TimerDialogListener) requireActivity();
                listener.onFinish();


                dismiss();
            }
        };

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialogListener listener = (TimerDialogListener) requireActivity();
                listener.onContinue();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialogListener listener = (TimerDialogListener) requireActivity();
                listener.onCancel();
                dismiss();
            }
        });

        // Disable cancel on touch outside
        setCancelable(false);

        return builder.create();
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        TimerDialogListener listener = (TimerDialogListener) requireActivity();
        listener.onCancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    private void sendSMS(String _message){
        SmsManager smsManager = SmsManager.getDefault();
        String number = "7978886146";
        String message = "this is message sent after 15 second";
        smsManager.sendTextMessage(number, null , _message , null, null );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                sendSMS("ACCIDENT!!!!! "+"click on the following link to find the route for accident. "+message);


            } else {
                // Permission denied
                Toast.makeText(getContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGoogleMapsNavigation() {
        double currentLatitude = 22.25; // Replace with the actual current latitude of the device
        double currentLongitude = 84.85; // Replace with the actual current longitude of the device


        String mapsUrl = "https://www.google.com/maps/dir/?api=1&";
        String destinationLatitude = "22.25068";
        String destinationLongitude = "84.90182";
        String destination = "destination=" + destinationLatitude + "," + destinationLongitude;
        String origin = "origin=" + currentLatitude + "," + currentLongitude;

        Uri uri = Uri.parse(mapsUrl + destination + "&" + origin);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
            Log.d("kamlans", "openGoogleMapsNavigation: " + uri.toString());
            Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();

            message = uri.toString();
        } else {
            // No browser app available, handle error
        }
    }

}