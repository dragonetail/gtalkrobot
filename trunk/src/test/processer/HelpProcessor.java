package test.processer;

import org.jivesoftware.smack.XMPPException;

import test.command.AbstractCommand;
import test.command.HelpCommand;
import test.exception.CommandMatchedException;

public class HelpProcessor extends AbstractProcessor {

	protected void beforeProcess(AbstractCommand abstractCommand)
			throws CommandMatchedException {
		if (!(abstractCommand instanceof HelpCommand)) {
			throw new CommandMatchedException(abstractCommand, this);
		}
		super.beforeProcess(abstractCommand);
	}

	protected void internalProcess(AbstractCommand abstractCommand)
			throws XMPPException {
		// HelpCommand command = (HelpCommand) abstractCommand;

		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("Welcome to gtalkbot!\n").append("\n").append(
				"Commands:").append("  [ /help ] show this message");

		sendBackMessage(abstractCommand, messageBuffer.toString());
	}

}
