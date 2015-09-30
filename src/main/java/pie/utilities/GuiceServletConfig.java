package pie.utilities;

import pie.filters.AuthFilter;
import pie.filters.ResponseFilter;
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
				
				// Filters
				filter("/servlets/*").through(ResponseFilter.class);
				filter("/servlets/secured/*").through(AuthFilter.class);
				
				// Servlets
				serve("*/servlets/login").with(LoginServlet.class);
				serve("*/servlets/secured/gencode").with(GenerateCodeServlet.class);
			}
		});
	}

}
