package com.example.eric.easiermonashlibrary;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
/**
 * Created by Eric on 10/6/17.
 */

public class LoginActivityTest
{
    @org.testng.annotations.Test
    public void testEmailValid() throws Exception
    {
        LoginActivity loginActivity = new LoginActivity();
        Class findClass = loginActivity.getClass();
        Method method = findClass.getMethod("isEmailValid",String.class);
        boolean a = (boolean)method.invoke(loginActivity,"123@123.com");
        assertEquals(false, a);
    }

    @org.testng.annotations.Test
    public void testPasswordValid() throws Exception
    {
        LoginActivity loginActivity = new LoginActivity();
        Class findClass = loginActivity.getClass();
        Method method = findClass.getMethod("isPasswordValid",String.class);
        boolean b = (boolean)method.invoke(loginActivity,"789");
        assertEquals(false, b);
    }
}
