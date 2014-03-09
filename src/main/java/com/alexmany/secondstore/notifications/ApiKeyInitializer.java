package com.alexmany.secondstore.notifications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context initializer that loads the API key from a
 * {@value #PATH} file located in the classpath (typically under
 * {@code WEB-INF/classes}).
 */
public class ApiKeyInitializer implements ServletContextListener {

  static final String ATTRIBUTE_ACCESS_KEY = "apiKey";


  private final Logger logger = Logger.getLogger(getClass().getName());

  public void contextInitialized(ServletContextEvent event) {

    event.getServletContext().setAttribute(ATTRIBUTE_ACCESS_KEY, "AIzaSyCbWtbW6pj0Bp7_jVxGTVYMTvTOlgGIkR0");
  }

  public void contextDestroyed(ServletContextEvent event) {
  }

}
