package com.grzesica.przemek.artistlist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static java.util.Objects.nonNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by przemek on 11.12.17.
 * Parametrized test is green when Android API is excluded from HttpHandler.java
 */

@RunWith(Parameterized.class)

public class HttpHandlerParametrizedTestAsInstrumented {

    private String handlerUrl;
    private boolean expectedBoolean;

    public HttpHandlerParametrizedTestAsInstrumented(String url, boolean bool){
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
        String parsedJson = new HttpHandler().jsonServiceCall(handlerUrl);
        assertEquals(expectedBoolean, nonNull(parsedJson));
    }

}