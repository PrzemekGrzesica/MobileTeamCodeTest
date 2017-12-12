package com.grzesica.przemek.artistlist;

import android.os.health.SystemHealthManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.junit.Assert.*;

/**
 * Created by przemek on 11.12.17.
 */

@RunWith(Parameterized.class)

public class HttpHandlerParameterizedTest {

    private String handlerUrl;
    private boolean expectedBoolean;

    public HttpHandlerParameterizedTest(String url, boolean bool){
        this.handlerUrl = url;
        this.expectedBoolean = bool;
    }

    @Parameterized.Parameters(name= "{index}: Test with url={0}, result boolean={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"http://i.img.co/data/data.json", true},
                {"http://onet.pl", true},
                {null, false},
                {"", false}
        });
    }


    @Test
    public void jsonServiceCallTest() throws Exception {
        String url = "http://i.img.co/data/data.json";
        String parsedJson = new HttpHandler().jsonServiceCall(handlerUrl);
        assertEquals(expectedBoolean, nonNull(parsedJson));
    }

    /*@Test
    public void jpgServiceCallTest() throws Exception {
    }*/


}