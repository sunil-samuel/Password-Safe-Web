/**
 * ProcessCommandLineParameters.java (Sep 17, 2014 - 11:30:10 PM)
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

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.util.StringUtils;

/**
 * Given a command line parameter, process the arguments and create getters that
 * can be used to retrieve the user parameters. This code also allows the
 * functionality to restart the application to start with debug options. <br>
 * <br>
 * Usage:<br>
 * <code>
 * ProcessCommandLineParameters cli = new ProcessCommandLineParameters(
				"password-safe", args);
	if (cli.isError()) {
			System.err.println(cli.getError());
			cli.printUsage();
			System.exit(1);

		}
		if (cli.isHelp()) {
			cli.printUsage();
			System.exit(0);
		}
 * </code>
 * 
 * @author Sunil G. Samuel (sgs@sunilsamuel.com)
 *
 */
public class ProcessCommandLineParameters {

	private String[] arguments;
	private Options options = new Options();
	private StringBuilder error = new StringBuilder();
	private CommandLine cmdLine = null;
	private boolean isError = false;
	private String name;
	private String commandName;

	private String dbDir = null;
	private String dbPasswd = null;
	private Integer port = null;

	public ProcessCommandLineParameters() {
		setCommandName();
	}

	public ProcessCommandLineParameters(String name, String[] arguments) {
		this.name = name;
		setCommandName();
		setArguments(arguments);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Use the ManagementFactory and system properties to reconstruct the
	 * command that was used to run this application. Then add -Xdebug ...
	 * options to start the application using debug options so that remote
	 * debugging can happen.
	 * 
	 * @return The command used to run the applicaion with additiona debug
	 *         options
	 */
	public String getCommandParametersForDebug() {
		StringBuilder commands = new StringBuilder();

		String javaCommand = System.getProperty("sun.java.command");
		javaCommand = javaCommand.replaceAll("--debug", "").replaceAll("-d\\s+|-d$", "").trim();

		return commands.append(System.getProperty("java.home")).append("/bin/java").append(" ")
				.append(isDebug() ? "-Xdebug -agentlib:jdwp=transport=dt_socket,address=" + getDebugPort()
						+ ",server=y,suspend=n " : "")
				.append(StringUtils.arrayToDelimitedString(
						ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(), " "))
				.append("-jar ").append(javaCommand).toString();
	}

	/**
	 * Shutdown the current applicaion and re-run the application with
	 * additional parameters.
	 */
	public void runApp() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					String cmd = getCommandParametersForDebug();
					Process p = Runtime.getRuntime().exec(cmd);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
					}
					in.close();
					p.waitFor();
				} catch (IOException e) {
					System.err.println("Could not run application : " + e);
				} catch (InterruptedException ie) {
					System.err.println("Application is interrupted : " + ie);
				}
			}
		});
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
		createOptions();
		process();
		validateGroups();
	}

	public void process() {
		CommandLineParser parser = new DefaultParser();
		try {
			cmdLine = parser.parse(options, arguments);
		} catch (ParseException e) {
			addError(e.getLocalizedMessage());
		}
	}

	public void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setDescPadding(5);
		formatter.setLeftPadding(10);
		formatter.printHelp(name, options);
	}

	private void addError(String err) {
		error.append(err);
		isError = true;
	}

	/**
	 * Making all of these options false. Then validate that the groups of
	 * options are met using other code.
	 */
	private void createOptions() {

		Option help = Option.builder("h").longOpt("help").required(false).desc("Show this help message").build();

		Option interactive = Option.builder("i").longOpt("interactive").required(false)
				.desc("Start application in interactive mode").build();

		Option debug = Option.builder("d").longOpt("debug").required(false).desc("Start the application in debug mode")
				.build();

		Option debugPort = Option.builder("e").longOpt("debugPort").hasArg(true).required(false).type(Number.class)
				.desc("Start the debug mode using this port (requires debug)").build();

		Option db = Option.builder("b").longOpt("dbDir").required(false).hasArg(true).desc("Location of the database")
				.build();

		Option dbPasswd = Option.builder("w").longOpt("dbPasswd").required(false).hasArg(true)
				.desc("Password for the database").build();

		Option port = Option.builder("p").longOpt("port").required(false).hasArg(true).type(Number.class)
				.desc("Start the application on this port (default 8080)").build();

		options.addOption(interactive).addOption(help).addOption(debug).addOption(debugPort).addOption(db)
				.addOption(dbPasswd).addOption(port);
	}

	public boolean isError() {
		return isError;
	}

	public String getError() {
		return error.toString();
	}

	public String getName() {
		return name;
	}

	public boolean isHelp() {
		return cmdLine.hasOption("help");
	}

	public boolean isDebug() {
		return cmdLine.hasOption("debug");
	}

	public boolean isInteractive() {
		if (cmdLine == null) {
			System.out.println("cmdLine is null");
		}
		return cmdLine.hasOption("interactive");
	}

	public int getDebugPort() {
		if (cmdLine.hasOption("debugPort")) {
			try {
				return ((Number) cmdLine.getParsedOptionValue("debugPort")).intValue();
			} catch (ParseException e) {
				return 9999;
			}
		}
		return 9999;
	}

	public String getDbDir() {
		if (dbDir != null) {
			return dbDir;
		}
		if (cmdLine.hasOption("dbDir")) {
			try {
				return (String) cmdLine.getParsedOptionValue("dbDir");
			} catch (ParseException e) {
				return null;
				// e.printStackTrace();
			}
		}
		return null;
	}

	public void setDbDir(String dbDir) {
		this.dbDir = dbDir;
	}

	public String getDbPasswd() {
		if (dbPasswd != null) {
			return dbPasswd;
		}
		if (cmdLine.hasOption("dbPasswd")) {
			try {
				return (String) cmdLine.getParsedOptionValue("dbPasswd");
			} catch (ParseException e) {
				// return null;
				// e.printStackTrace();
			}
		}
		Console console = System.console();
		char password[] = null;
		while (password == null) {
			password = console.readPassword("Please enter a password for database: ");
			if (password.length <= 1) {
				password = null;
			}
		}
		return new String(password);
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public int getPort() {
		if (port != null) {
			return port;
		}
		if (cmdLine.hasOption("port")) {
			try {
				return ((Number) cmdLine.getParsedOptionValue("port")).intValue();
			} catch (ParseException e) {
				return 8080;
			}
		}
		return 8080;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private void setCommandName() {
		this.commandName = System.getProperty("sun.java.command");
		int index = commandName.indexOf(" ");
		if (index >= 0) {
			this.commandName = commandName.substring(0, index);
		}
	}

	private void validateGroups() {
		/**
		 * If not 'h' && not 'i', then 'b' && 'w'
		 */
		if (!(isHelp() || isInteractive())) {
			if (getDbDir() == null) {
				addError("DbDir must be provided.");
			}
		}
	}

	public String getCommandName() {
		return commandName;
	}
}
