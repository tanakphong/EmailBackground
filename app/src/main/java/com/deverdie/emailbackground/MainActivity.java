package com.deverdie.emailbackground;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.deverdie.emailbackground.java.GMailSender;
import com.deverdie.emailbackground.java.GMailSender2;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class MainActivity extends AppCompatActivity {

    private Button send;

    final String username = "neung.deverdie@gmail.com";
    final String password = "D3v@gmail";
    private String token;

    private class SendEmailTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            sendEmail3();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
//                    sendEmail3();
                    new SendEmailTask().execute();
//                    AccountManager am = AccountManager.get(MainActivity.this);
//                    Account[] me = am.getAccounts(); //You need to get a google account on the device, it changes if you have more than one
//                    am.getAuthToken(me[0], "oauth2:https://mail.google.com/", null, this, new OnTokenAcquired(), null);
//


                    GMailSender2 gMailSender2 = new GMailSender2(MainActivity.this);
                    gMailSender2.sendMail("Testing Subject","Dear Mail Crawler,","neung.deverdie@gmail.com",gMailSender2.getToken(),"t.phongsing@gmail.com");
//                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Offine", Toast.LENGTH_LONG).show();
                }




            }
        });
    }

    private void sendEmail3() {
        GMailSender2 gMailSender2 = new GMailSender2(MainActivity.this);
        gMailSender2.sendMail("Testing Subject","Dear Mail Crawler,","neung.deverdie@gmail.com",gMailSender2.getToken(),"t.phongsing@gmail.com");
    }

    private void sendEmail2() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("neung.deverdie@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("t.phongsing@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();

            /*messageBodyPart = new MimeBodyPart();
            String file = "path of file to be attached";
            String fileName = "attachmentName";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);*/

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail() {
        try {

            GMailSender sender = new GMailSender(

                    "neung.deverdie@gmail.com",

                    "D3v@gmail");


//                    sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");

            sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",

                    "neung.deverdie@gmail.com",

                    "t.phongsing@gmail.com");


            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result){
            try{
                Bundle bundle = result.getResult();
                token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            } catch (Exception e){
                Log.d("test", e.getMessage());
            }
        }
    }
}
