package com.grzesica.przemek.artistlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
/**
 * Created by przemek on 26.11.17.
 * Methods for download json file and images.
 */
public class HttpHandler implements IhttpHandler{

    private URL mUrl;
    private Appendable mStrBuilder;
    private OutputStream mOutputStream;

    public HttpHandler(DependencyInjectionBuilder builder){

//    public HttpHandler(Appendable strBuilder, OutputStream byteArrayOutputStream){
        this.mStrBuilder = builder.mStrBuilder;
        this.mOutputStream = builder.mByteArrayOutputStream;
    }

    @Override
    public String jsonServiceCall(String requestUrl) {
        String response = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(requestUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(con.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
        } catch (ProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            Log.e("MYAPP", "exception: " + e);
        } finally {
            con.disconnect();
        }
        return response;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder strBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                mStrBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mStrBuilder.toString();
    }

    public InputStream getHttpConnection(String strUrl)  throws IOException {
        InputStream stream = null;
        try {
            HttpURLConnection httpConnection = httpConn(strUrl);
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }else if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM){
                String newUrl = httpConnection.getHeaderField("Location");
                stream = httpConn(newUrl).getInputStream();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    private HttpURLConnection httpConn(String strUrl) throws Exception{
        URLConnection connection = new URL(strUrl).openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestMethod("GET");
        httpConnection.connect();
        return httpConnection;
    }

    @Override
    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bitmapOptions);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public byte[] getBlob(Bitmap bitmap) {
        if (bitmap!=null) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, mOutputStream);
            ByteArrayOutputStream stream = (ByteArrayOutputStream)mOutputStream;
            return stream.toByteArray();
        }
        return null;
    }
}
