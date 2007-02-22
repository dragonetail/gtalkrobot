package com.gtrobot.commandparser;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

import com.gtrobot.command.AbstractCommand;
import com.gtrobot.command.common.BroadcastMessageCommand;
import com.gtrobot.command.common.InvalidCommand;
import com.gtrobot.processor.InteractiveProcessor;
import com.gtrobot.utils.CommonUtils;
import com.gtrobot.utils.ParameterTable;
import com.gtrobot.utils.UserSession;

public class CommadParser {
	protected static final transient Log log = LogFactory
			.getLog(CommadParser.class);

	private static final String COMMAND_PREFIX_1 = "/";

	private static final String COMMAND_PREFIX_2 = ":";

	/**
	 * Parse the message and return a command object
	 * 
	 * @param message
	 * @return
	 */
	public static AbstractCommand parser(Message message) {
		if (log.isDebugEnabled()) {
			log.debug("Message       from: " + message.getFrom());
			log.debug("                to: " + message.getTo());
			log.debug("          threadId: " + message.getThread());
			log.debug("          packetId: " + message.getPacketID());
			log.debug("              type: " + message.getType().toString());
			Iterator propertyNames = message.getPropertyNames();
			while (propertyNames.hasNext()) {
				String name = (String) propertyNames.next();
				log.debug("          property: " + name + " : "
						+ message.getProperty(name));
			}
			log.debug("             class: " + message.getClass().getName());
			log.debug("             error: " + message.getError());
			log.debug("           subject: " + message.getSubject());
			log.debug("              body: " + message.getBody());
		}

		String from = message.getFrom();
		String body = message.getBody();
		log.info("Message from <" + from + ">: " + body);
		String jid = StringUtils.parseBareAddress(from);
		AbstractCommand command = parse(jid, body);
		if (command != null) {
			command.setOriginMessage(body);
			command.setFrom(from);
		}
		return command;
	}

	/**
	 * Parse the command string and contruct comamnd object
	 * 
	 * @param jid
	 * @param body
	 * @return
	 */
	private static AbstractCommand parse(String jid, String body) {
		if (body == null) {
			log.info("Warn: message's body is NULL!");
			return null;
		}
		// Trim the message body to parse the command prefix
		body = body.trim();
		if (!(body.startsWith(COMMAND_PREFIX_1) || body
				.startsWith(COMMAND_PREFIX_2))) {
			// Check if the user is in a interactive operations
			Long step = InteractiveProcessor.getStep(jid);
			if (step != null) {
				return getPreviousCommand(jid);
			} else {
				// Normal broadcast message
				return new BroadcastMessageCommand(jid, body);
			}
		}

		// Parse the command and parameters
		List commands = CommonUtils.parseCommand(body);
		if (commands == null || commands.size() < 1) {
			return new InvalidCommand(jid, null);
		}

		String commandName = ((String) commands.get(0)).toLowerCase();
		// Repeat the previous command
		if (commandName.equals(COMMAND_PREFIX_1)
				|| commandName.equals(COMMAND_PREFIX_2)) {
			return getPreviousCommand(jid);
		}

		Class commandClass = (Class) ParameterTable.getCommadMappings().get(
				commandName);
		if (commandClass == null) {
			return new InvalidCommand(jid, null);
		}
		try {
			// Construct a new command object according to the command info
			Constructor constructor = commandClass.getConstructor(new Class[] {
					String.class, List.class });
			return (AbstractCommand) constructor.newInstance(new Object[] {
					jid, commands });
		} catch (Exception e) {
			e.printStackTrace();
			return new InvalidCommand(jid, null);
		}
	}

	private static AbstractCommand getPreviousCommand(String jid) {
		AbstractCommand previousCommand = UserSession
				.retrievePreviousCommand(jid);
		if (previousCommand == null) {
			previousCommand = new InvalidCommand(jid, null);
			previousCommand.setErrorMessage(previousCommand
					.getI18NMessage("invalid.command.previous.command.null"));
		} else {
			previousCommand.setErrorMessage(null);
		}
		return previousCommand;
	}
}