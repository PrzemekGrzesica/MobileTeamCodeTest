package com.grzesica.przemek.artistlist;

import com.grzesica.przemek.artistlist.Container.DependencyInjectionBuilder;
import com.grzesica.przemek.artistlist.Model.HttpHandler;

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
        DependencyInjectionBuilder depInjBuilder = new DependencyInjectionBuilder();
        HttpHandler httpHandler = depInjBuilder
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();
        String parsedJson = httpHandler.jsonServiceCall(handlerUrl);
        assertEquals(expectedBoolean, nonNull(parsedJson));
    }

}