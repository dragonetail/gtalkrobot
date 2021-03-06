package com.gtrobot.command.common;

import java.util.List;

import com.gtrobot.command.ErrorType;
import com.gtrobot.command.ProcessableCommand;

/**
 * 更改用户的NickName。
 * 
 * @author Joey
 * 
 */
public class ChangeNickNameCommand extends ProcessableCommand {
    private String newNickName;

    @Override
    public void parseArgv(final List argv) {
        if (argv.size() != 2) {
            this.setError(ErrorType.wrongParameter);
            return;
        }
        this.newNickName = ((String) argv.get(1)).trim();

        super.parseArgv(argv);
    }

    public String getNewNickName() {
        return this.newNickName;
    }
}
