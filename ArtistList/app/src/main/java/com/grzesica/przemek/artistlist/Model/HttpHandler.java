package com.grzesica.przemek.artistlist.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;
import com.grzesica.przemek.artistlist.Container.IHttpHandlerDIBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLConnection;

/**
 * Created by przemek on 26.11.17.
 * Methods for download json file and images.
 */
public class HttpHandler implements IHttpHandler {

    private Appendable mStrBuilder;
    private OutputStream mOutputStream;
    private IExtendedUrl mExtendedUrl;
    private IExtendedBufferReader mExtendedBufferReader;

    public HttpHandler(IHttpHandlerDIBuilder builder) {
        this.mStrBuilder = ((HttpHandlerDIBuilder)builder).mStrBuilder;
        this.mOutputStream = ((HttpHandlerDIBuilder)builder).mByteArrayOutputStream;
        this.mExtendedUrl = ((HttpHandlerDIBuilder)builder).mExtendedUrl;
        this.mExtendedBufferReader = ((HttpHandlerDIBuilder)builder).mExtendedBufferedReader;
    }

    @Override
    public String jsonServiceCall(String requestUrl) {
        String response = null;
        try {
            response = convertStreamToString(getInputStream(requestUrl));
        } catch (MalformedURLException e) {
        } catch (ProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            Log.e("MYAPP", "exception: " + e);
        } finally {
//            mConnection.disconnect();
        }
        return response;
    }

    public String convertStreamToString(InputStream inputStream) {

        BufferedReader reader = mExtendedBufferReader.setInputStream(inputStream);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                mStrBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mStrBuilder.toString();
    }

    public InputStream getInputStream(String strUrl) throws IOException {
        InputStream stream = null;
        try {
            HttpURLConnection httpConnection = getHttpUrlConn(strUrl);
            //Checking and solving redirection.
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            } else if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                String newUrl = httpConnection.getHeaderField("Location");
                stream = getHttpUrlConn(newUrl).getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    private HttpURLConnection getHttpUrlConn(String strUrl) throws Exception {
        URLConnection connection = mExtendedUrl.setUrl(strUrl).openConnection();
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
            stream = getInputStream(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bitmapOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public byte[] getBlob(Bitmap bitmap) {
        ((ByteArrayOutputStream)mOutputStream).reset();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, mOutputStream);
            return ((ByteArrayOutputStream) mOutputStream).toByteArray();
        }
        return null;
    }
}
