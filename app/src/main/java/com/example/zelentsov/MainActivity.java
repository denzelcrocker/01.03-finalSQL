package com.example.zelentsov;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    TextView id;
    TextView name;
    TextView count;
    ImageView image;
    String Img = "";
    Connection connection;
    private ImageView imageButton;
    String ConnectionResult = "";
    TextView idEdit;
    TextView nameEdit;
    TextView countEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void backClick(View view) {
        setContentView(R.layout.activity_main);

    }
    public void editClick(View view) {
        setContentView(R.layout.activity_edit);

    }
    public void addItem(View view) {
        setContentView(R.layout.activity_new);
    }
    public void GetList(View v) {
        setContentView(R.layout.activity_check);
        ListView lstview = (ListView) findViewById(R.id.listview1);
        List<Map<String, Object>> MyDataList = null;
        ImageView imageView = findViewById(R.id.list_item_image);
        ListItem MyData = new ListItem();
        MyDataList = MyData.getlist();

        String[] Fromw = {"id", "Name", "Count","Image"};
        int[] Tow = {R.id.id, R.id.Name, R.id.Count, R.id.list_item_image};
        SimpleAdapter ad = new SimpleAdapter(MainActivity.this, MyDataList, R.layout.listayouttemplate, Fromw, Tow);
        lstview.setAdapter(ad);
        ad.setViewBinder((view, data, textRepresentation) -> {
            if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                ImageView iv = (ImageView) view;
                Bitmap bm = (Bitmap) data;
                iv.setImageBitmap(bm);
                return true;
            }
            return false;
        });
        lstview.setOnItemClickListener((parent, view, position, id) -> {
            String idString, name, count;
            TextView idTv = view.findViewById(R.id.id);
            idString = idTv.getText().toString();
            TextView nameTv = view.findViewById(R.id.Name);
            name = nameTv.getText().toString();
            TextView countTv = view.findViewById(R.id.Count);
            count = countTv.getText().toString();
            ImageView avatar = view.findViewById(R.id.list_item_image);
            Bitmap avatarBm = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
            setContentView(R.layout.activity_edit);

            TextView editName = findViewById(R.id.idEdit1);
            editName.setText(idString);
            TextView editJob = findViewById(R.id.nameEdit1);
            editJob.setText(name);
            TextView editEmail = findViewById(R.id.countEdit1);
            editEmail.setText(count);
            ImageView ivAvatar = findViewById(R.id.image1);
            ivAvatar.setImageBitmap(avatarBm);
        });
    }
    public void deleteClick(View view) {
        EditText deleteId = findViewById(R.id.delete);
        try {
            String ConnectionResult = "";
            ConnectionSQL con = new ConnectionSQL();
            Connection connect = con.CONN();

            if (connect != null) {
                String query = "DELETE FROM Humans WHERE Id = " + deleteId.getText() + ";";

                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                ConnectionResult = "Success";

                connect.close();
            } else {
                ConnectionResult = "Failed";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
    public Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null && !encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(MainActivity.this.getResources(),
                R.drawable.img);
    }
    public void onClickChooseImage(View view)
    {
        getImage(view);
    }

    public void getImage(View v)
    {
        try {
            Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFileIntent.setType("*/*");
            chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");

            startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
        }
        catch (Exception ex){
            Toast.makeText(v.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==MY_RESULT_CODE_FILECHOOSER && data!= null && data.getData()!= null)
        {
            if(resultCode==RESULT_OK)
            {
                Log.d("MyLog","Image URI : "+data.getData());
                imageButton=findViewById(R.id.image1);
                imageButton.setImageURI(data.getData());
                Bitmap bitmap = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                encodeImage(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        public void SetTextFromSql(View v) {
        idEdit = findViewById(R.id.idEdit);
        nameEdit = findViewById(R.id.nameEdit);
        countEdit = findViewById(R.id.countEdit);
        String id = idEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String count = countEdit.getText().toString();
        try {
            ConnectionSQL conectionHellper = new ConnectionSQL();
            connection = conectionHellper.CONN();
            if (idEdit.getText().length()==0 || nameEdit.getText().length()==0 || countEdit.getText().length()==0 )
            {
                Toast.makeText(this,"Не заполнены обязательные поля", Toast.LENGTH_LONG).show();
                return;
            }
            if (connection != null) {

                String query = "INSERT INTO Humans (Id, Name, Count, Image) values ('" + id + "','" + name + "','" + count + "','"+ Img +"')";
                Statement statement = connection.createStatement();
                statement.execute(query);
                Toast.makeText(this,"Успешно добавлено", Toast.LENGTH_LONG).show();
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}