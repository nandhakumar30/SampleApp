package com.rd.nn.sampleapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nandha on 24-02-2016.
 */
public class NewActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String UPLOAD_URL = "http://kongas.kaptastech.in/cowsforsellbyuser2.php";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
    File imagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

//        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
//
//        String imsiSIM1 = telephonyInfo.getImsiSIM1();
//        String imsiSIM2 = telephonyInfo.getImsiSIM2();
//
//        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
//        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
//
//        boolean isDualSIM = telephonyInfo.isDualSIM();
//
        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getSimSerialNumber();
        String getSimNumber = telemamanger.getLine1Number();
        System.out.print("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            imagename = new File(getRealPathFromURI(filePath));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI){
        String result;
        Cursor cursor = getContentResolver().query(contentURI,null,null,null,null);
        if(cursor==null){
            result=contentURI.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result=cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            uploadImage();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        class uploadData extends AsyncTask<String,String ,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NewActivity.this, "Uploading ", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String value) {
                super.onPostExecute(value);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                String serverResponseMessage="";

                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                try {
                    if (imagename.isFile()) {
                        Log.d("CheckFile", "Its a file");
                        try {
                            FileInputStream fileInputStream = new FileInputStream(imagename);
                            URL url = new URL(UPLOAD_URL);
                            String sourceFileUri = imagename.toString();

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("userId", "1622");
                            conn.setRequestProperty("colorId", "2");
                            conn.setRequestProperty("gender", "Female");
                            conn.setRequestProperty("address", "10/133, Tirupur Road, Kangayam");
                            conn.setRequestProperty("distId", "16");
                            conn.setRequestProperty("town", "Kangayam");
                            conn.setRequestProperty("teeth", "2");
                            conn.setRequestProperty("price", "42000");
                            conn.setRequestProperty("contactperson", "Arun");
                            conn.setRequestProperty("mobileno", "9677066066");
                            conn.setRequestProperty("pincode", "638701");
                            conn.setRequestProperty("bullimage[]", sourceFileUri);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"userId\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("1622" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"colorId\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("2" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"gender\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("Female" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("10/133, Tirupur Road, Kangayam" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"distId\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("6" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"town\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("Kangayam" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"teeth\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("2" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"price\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("42000" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"contactperson\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("Arun" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"mobileno\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("9677066066" + lineEnd);dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"pincode\"" + lineEnd);
                            dos.writeBytes(lineEnd);
                            dos.writeBytes("638701" + lineEnd);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"bullimage[]\";filename=\"" + sourceFileUri + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            String s = Integer.toString(dos.size());
                            Log.d("DOSFILESIZE", s);

                            // create a buffer of maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0)
                            {

                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0,bufferSize);

                            }

                            // send multipart form data necesssary after file
                            // data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            // Responses from the server (code and message)
                            int serverResponseCode = conn.getResponseCode();
                            serverResponseMessage = conn.getResponseMessage();
                            System.out.print(serverResponseCode +" "+ serverResponseMessage);

                            if (serverResponseCode == 200)
                            {
                                Log.d("Server Response",serverResponseMessage);

                                // messageText.setText(serverResponseMessage);
                                //Toast.makeText(this, "File Upload Complete.",Toast.LENGTH_SHORT).show();

                                //recursiveDelete(mDirectory1);

                            }

                            // close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Log.d("No image", "Source is not an iamge file");
                    }
                }catch (Exception ex) {
                    // dialog.dismiss();

                    ex.printStackTrace();
                }


                return serverResponseMessage;
            }
        }

        uploadData ui = new uploadData();
        ui.execute();
    }
}
