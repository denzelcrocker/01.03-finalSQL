package com.example.zelentsov;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class Edit extends AppCompatActivity {
    Connection connection;
    String ConnectionResult = "";
    private ImageView imageButton;
    TextView idEdit1;
    TextView nameEdit1;
    TextView countEdit1;
    ImageView imageView;
    String Img="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imageButton=findViewById(R.id.image1);
        imageView=findViewById(R.id.image1);
    }
    public void onClickChooseImage(View view)
    {
        getImage();

    }
    private Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null && !encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(Edit.this.getResources(),
                R.drawable.ic_launcher_background);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!= null && data.getData()!= null)
        {
            if(resultCode==RESULT_OK)
            {
                Log.d("MyLog","Image URI : "+data.getData());
                imageButton.setImageURI(data.getData());
                Bitmap bitmap = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                encodeImage(bitmap);

            }
        }
    }

    private void getImage()
    {
        Intent intentChooser= new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }

    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Img= Base64.getEncoder().encodeToString(bytes);
            return Img;
        }
        return "";
    }
    private void configureBackButton() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void editS(View v) {
        idEdit1 = findViewById(R.id.idEdit1);
        nameEdit1 = findViewById(R.id.nameEdit1);
        countEdit1 = findViewById(R.id.countEdit1);
        String idEdit2 = idEdit1.getText().toString();
        String  nameEdit2 = nameEdit1.getText().toString();
        String countEdit2 = countEdit1.getText().toString();
        try {
            ConnectionSQL conectionHellper = new ConnectionSQL();
            connection = conectionHellper.CONN();
            if (connection != null) {
                String query12 = "update Humans set Name ='" + nameEdit2 + "',Count ='" + countEdit2 + "', Image = '"+ Img +"' where Id = "+idEdit2+"";
                Statement statement12 = connection.createStatement();
                statement12.execute(query12);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Данные успешно изменены, обновите их", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}