/**
 * JettyInitialize.java (Sep 17, 2014 - 11:30:10 PM)
 *
 * Sunil Samuel CONFIDENTIAL
 *
 *  [2017] Sunil Samuel
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */
package com.sunilsamuel.passwordsafe.config.jetty;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * The main application that will start jetty with the correct configuration
 * parameters.
 * 
 * @author Sunil G. Samuel (sgs@sunilsamuel.com)
 *
 */
public class JettyInitialize {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ProcessCommandLineParameters cli;

	public static void main(String[] args) throws Exception {
		new JettyInitialize().processApplication(args);
	}

	private void processApplication(String[] args) throws Exception {
		cli = new ProcessCommandLineParameters("password-safe", args);
		/**
		 * If there is an error, then print out the error message and terminate
		 * the application.
		 */
		if (cli.isError()) {
			printInteractiveFirst();
			System.err.println(cli.getError());
			cli.printUsage();
			System.exit(1);
		}
		/**
		 * If help is requested, then print the usage and terminate the
		 * application.
		 */
		if (cli.isHelp()) {
			printInteractiveFirst();
			cli.printUsage();
			System.exit(0);
		}
		/**
		 * If the user requested interactive, then ask for all of the
		 * information we need to run the application.
		 */
		if (cli.isInteractive()) {
			interactive();
		}
		if (cli.isDebug()) {
			System.out.println(
					"Since you wanted to run in 'debug' mode, I will sleep for 5 seconds");
			System.out.println(
					"so that you can start up your debug client.  The debug port is '"
							+ cli.getDebugPort() + "'.");
			Thread.sleep(5000);
			System.out.println("Running in debug mode.");

			cli.runApp();
		} else {
			startJetty();
		}
		return;
	}

	private void printInteractiveFirst() {
		wrap("If you are running for the first time.  Then run as interactive first.");
		System.out.println("\tjava -jar " + cli.getCommandName() + " -i");
		System.out.println(
				"\tjava -jar " + cli.getCommandName() + " --interactive\n");
		wrap("But then, you can always run as interactive since it's easier.");
		System.out.println("\n");
	}

	private void interactive() {
		wrap("OK.  You asked for interactive, so I will ask you some questions "
				+ "that will help me start properly.");
		getDbDir();
		getPort();
		getDbPasswd();
	}

	private void startJetty() throws Exception {
		// Set JSP to use Standard JavaC always
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
		System.setProperty("derby-home-dir", cli.getDbDir());
		System.setProperty("derby-passwd", cli.getDbPasswd());
		logger.debug("Starting server at port {}", cli.getPort());
		Server server = new Server(cli.getPort());
		server.setHandler(getServletContextHandler(getContext()));
		server.start();
		InetAddress address = InetAddress.getLocalHost();
		System.out.println("Server starting (try these URLs):");
		System.out.println(
				"\thttp://" + address.getHostAddress() + ":" + cli.getPort());
		System.out.println(
				"\thttp://" + address.getHostName() + ":" + cli.getPort());
		System.out.println("\thttp://localhost:" + cli.getPort());
		server.join();
	}

	private ClassLoader getUrlClassLoader() {
		ClassLoader jspClassLoader = new URLClassLoader(new URL[0],
				this.getClass().getClassLoader());
		return jspClassLoader;
	}

	private void wrap(String str) {
		System.out.println(WordUtils.wrap(str, 80));
	}

	/**
	 * Create JSP Servlet (must be named "jsp")
	 */

	private ServletContextHandler getServletContextHandler(
			WebApplicationContext context)
			throws IOException, URISyntaxException {
		WebAppContext contextHandler = new WebAppContext();
		contextHandler.setContextPath("/");

		contextHandler.addEventListener(new ContextLoaderListener(context));
		contextHandler.setResourceBase(
				new ClassPathResource("/webapp").getURI().toString());

		contextHandler.setAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/.*jsp-api-[^/]*\\.jar$|.*/.*jsp-[^/]*\\.jar$|.*/.*taglibs[^/]*\\.jar$");

		contextHandler.setClassLoader(getUrlClassLoader());

		return contextHandler;
	}

	private static WebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		return context;
	}

	private void getDbDir() {
		wrap("\nEnter the database directory.  This is the path (relative or fullpath) "
				+ "of where the database would be saved.  If this is the first time you "
				+ "are running this application, then give me a directory path and I will "
				+ "create the database directory.  This will allow you to take this directory "
				+ "anywhere you go and use it on other machines.  If this is not the first time "
				+ "you are running this application, or you want to use an existing database, "
				+ " then please provide the directory to the existing database.");
		System.out.print("Database Directory:");
		cli.setDbDir(System.console().readLine());
	}

	private void getPort() {
		wrap("\nEnter the port number.  This is the port where the web application "
				+ "will run.  The default port is 8080 and may be the right port to use since it "
				+ "does not require administrative permission.");
		System.out.print("Enter Port:");
		cli.setPort(Integer.valueOf(System.console().readLine()));
	}

	private void getDbPasswd() {
		wrap("\nEnter the password for the database.  If this is the first time "
				+ "you are running this application, then enter a strong password "
				+ "for the database.  Please remember this password since it will be "
				+ "needed if you choose to migrate this database elsewhere or restart the "
				+ "application.  If you do not remember this password, then you will not be "
				+ "able to open this database and all your information will be lost!!!!\n\n"
				+ "Note, that this password is different from the password you will use "
				+ "to log onto the web-based application.");
		Boolean equal = false;
		String password = null;
		while (!equal) {
			System.out.print("Database Password:");
			password = String.copyValueOf(System.console().readPassword());
			System.out.print("Re-enter Database Password:");
			String rePassword = String
					.copyValueOf(System.console().readPassword());
			equal = (password.equals(rePassword));
			if (!equal) {
				System.out.println("The two passwords do not match.");
			}
		}
		cli.setDbPasswd(password);
	}
}
