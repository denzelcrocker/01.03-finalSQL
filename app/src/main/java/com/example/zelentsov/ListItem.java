package com.example.zelentsov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuucess = false;

    public List<Map<String, Object>> getlist() {
        List<Map<String, Object>> data;
        data = new ArrayList<>();
        try {
            ConnectionSQL connectionHelper = new ConnectionSQL();
            connect = ConnectionSQL.CONN();
            if (connect != null) {
                String qu = " select * from Humans";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(qu);
                while (resultSet.next()) {
                    Map<String, Object> dtname = new HashMap<>();
                    dtname.put("id", resultSet.getString("Id"));
                    dtname.put("Name", resultSet.getString("Name"));
                    dtname.put("Count", resultSet.getString("Count"));

                    String decodeImage = resultSet.getString("Image");

                    byte[] decodedString = Base64.decode(decodeImage,Base64.DEFAULT);
                    Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

                    dtname.put("Image", base64Bitmap);
                    data.add(dtname);
                }
                ConnectionResult = "Success";
                isSuucess = true;
                connect.close();
            } else {
                ConnectionResult = "Failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}

