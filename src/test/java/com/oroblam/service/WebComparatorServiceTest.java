package com.oroblam.service;

import com.oroblam.service.WebComparatorService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebComparatorServiceTest {

    private WebComparatorService webComparatorService;

    @Before
    public void setUp(){
        webComparatorService = new WebComparatorService();
    }

    @Test
    public void shouldReturnTrueWhenTheContentDiffer() throws Exception {
        assertTrue(webComparatorService.isUpdated("foo", "bar"));
    }

    @Test
    public void shouldReturnFalseWhenTheContentIsIdentical(){
        assertFalse(webComparatorService.isUpdated("foobar", "foobar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenOneArgumentIsInvalid(){
        webComparatorService.isUpdated(null, "foo");
    }
}