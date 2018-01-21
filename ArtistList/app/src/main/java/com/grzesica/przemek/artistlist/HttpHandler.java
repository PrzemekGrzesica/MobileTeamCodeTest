package com.grzesica.przemek.artistlist;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
//import javax.imageio.ImageIO;

/**
 * Created by przemek on 26.11.17.
 * Methods for download json and jpg files.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String jsonServiceCall(String requestUrl) {
        String response = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(con.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
        } catch (ProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }

        return response;
    }

    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder strBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line).append('\n');
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
        return strBuilder.toString();
    }

    public void jpgServiceCall(String requestUrl) {
        /*try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // read the response
            BufferedImage img =  ImageIO.read(con.getInputStream());

            // retrieve image
//			    BufferedImage bi = getMyImage();
            File outputfile = new File("szaved.jpg");
            ImageIO.write(img, "jpg", outputfile);

//			response = convertStreamToString(in);
        } catch (MalformedURLException e) {
             Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
             Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
             Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
             Log.e(TAG, "Exception: " + e.getMessage());
        }*/

    }
}
