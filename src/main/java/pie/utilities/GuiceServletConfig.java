package pie.utilities;

import pie.servlets.GenerateCodeServlet;
import pie.servlets.LoginServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			
			@Override
			protected void configureServlets() {
				serve("*/login.html").with(LoginServlet.class);
				serve("*/gencode").with(GenerateCodeServlet.class);
			}
		});
	}

}