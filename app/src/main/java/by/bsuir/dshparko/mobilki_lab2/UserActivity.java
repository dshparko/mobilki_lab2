package by.bsuir.dshparko.mobilki_lab2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserActivity extends AppCompatActivity {


    TextView name, email;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            setContentView(R.layout.activity_user);
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            name = findViewById(R.id.userName);
            userPhoto = findViewById(R.id.userPhoto);
            Glide.with(getApplicationContext())
                  .load(personPhoto)
                    .into(userPhoto);

            System.out.println(personPhoto);
            email = findViewById(R.id.post);
            name.setText(personName);
            email.setText(personEmail);
        } else {
            Intent intent1 = new Intent(this, SignInActivity.class);
            startActivity(intent1);
        }
    }

    public void showInformationAboutCreator(View view) {

        String info = "lab2\nCreator: Darya Shparko ";

        final AlertDialog aboutDialog = new AlertDialog.Builder(
                UserActivity.this).setMessage(info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();

        aboutDialog.show();

        ((TextView) aboutDialog.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());

    }
}
