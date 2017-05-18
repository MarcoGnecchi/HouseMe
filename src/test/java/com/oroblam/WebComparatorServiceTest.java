package com.oroblam;

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
    public void shouldReturnFalseWhenTheContentDiffer() throws Exception {
        assertFalse(webComparatorService.compare("foo", "bar"));
    }

    @Test
    public void shouldReturnTrueWhenTheContentIsIdentical(){
        assertTrue(webComparatorService.compare("foobar", "foobar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenOneArgumentIsInvalid(){
        webComparatorService.compare(null, "foo");
    }
}