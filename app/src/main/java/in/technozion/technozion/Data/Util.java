package in.technozion.technozion.Data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;


public class Util {

    public static String getStringFromURL(String s,HashMap<String,String> hashMap){
        try {
            URL url=new URL(s);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
           // JSONObject jsonParam = new JSONObject();
           // jsonParam.put("eventid", "12");
            int len = hashMap.size();
            Set<String> set = hashMap.keySet();
            java.util.Iterator ir = set.iterator();
            String data = "";

            for(int i=0;i<len;i++) {
                String key = (String) ir.next();
                String value = hashMap.get(key);
                Log.d("testing_data"+i,key+" "+value);
                if(i==0)
                data = ""+key+"="+URLEncoder.encode(value,"UTF-8");
                else
               data +="&"+key+"="+URLEncoder.encode(value,"UTF-8");
            }

            Log.d("checking_send_data",data);
           // String values = "jsonValue="+ URLEncoder.encode(jsonParam.toString(),"UTF-8");
           // String userid;
            //String param = "9346472";
            //String param= "userid=" + URLEncoder.encode("9346472","UTF-8");
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(data);
            out.close();



            int HttpResult =httpURLConnection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK) {


                InputStream is = httpURLConnection.getInputStream();
//            InputStream is=urlConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(is));


                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {

                    builder.append(line);
                }
                Log.d("checking_val",builder.toString());
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static String getStringFromURL(String s) {
        try {
            URL url=new URL(s);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            InputStream is=httpURLConnection.getInputStream();

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));


            String line;
            StringBuilder builder=new StringBuilder();
            while((line=reader.readLine())!=null){
                builder.append(line);
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
