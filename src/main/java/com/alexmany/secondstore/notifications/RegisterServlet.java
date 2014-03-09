package com.alexmany.secondstore.notifications;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alexmany.secondstore.dao.RegistresDao;
import com.alexmany.secondstore.model.NotificationRegister;


/**
 * Servlet that registers a device, whose registration id is identified by
 * {@link #PARAMETER_REG_ID}.
 *
 * <p>
 * The client app should call this servlet everytime it receives a
 * {@code com.google.android.c2dm.intent.REGISTRATION C2DM} intent without an
 * error or {@code unregistered} extra.
 */
@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {

  private static final String PARAMETER_REG_ID = "regId";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException {
    String regId = getParameter(req, PARAMETER_REG_ID);
    
    ApplicationContext ac = new ClassPathXmlApplicationContext("/WEB-INF/appStore-context-servlet.xml");
    RegistresDao registreDao = (RegistresDao) ac.getBean("registerDao");
    NotificationRegister notifRegister = new NotificationRegister(regId);
    registreDao.save(notifRegister);

    setSuccess(resp);
  }

}
