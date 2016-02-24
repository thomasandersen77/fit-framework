package com.github.fit.undertow;

import java.io.Serializable;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

@WebListener
public class ApplicationInitialisationListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ApplicationInitialisationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        BeanManager beanManager = lookUpBeanManager();
        if (beanManager != null) {
            beanManager.fireEvent(new SomeDummyEvent());
            LOG.info("beanManager fired SomeDummyEvent.");
        } else {
            LOG.error("beanManager is null.  Cannot fire startup event.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public BeanManager lookUpBeanManager() {
        try {
            // See reference below about how I came up with this
            InitialContext iniCtx = new InitialContext();
            BeanManager result = (BeanManager) iniCtx.lookup("java:comp/env/BeanManager");
            return result;
        } catch (NamingException e) {
            LOG.error("Could not construct BeanManager.", e);
            return null;
        }
    }

    public static class SomeDummyEvent implements Serializable {
    }
}