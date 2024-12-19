package com.thedeafcreeper.commands.rcCommands;

import group.aelysium.rustyconnector.RC;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class RCSend extends Command {
    public RCSend() {
        super("send");

        ArgumentString usernameArg = ArgumentType.String("username");
        ArgumentString targetArg = ArgumentType.String("family-or-server");
        ArgumentString flagsArg = ArgumentType.String("flags");

        flagsArg.isOptional();
        flagsArg.allowSpace();

        addSyntax((sender, context) -> {
            final String username = context.get(usernameArg);
            final String familyOrServer = context.get(targetArg);
            final String flags = context.get(flagsArg);

            if (username == null || familyOrServer == null) {
                sender.sendMessage(RC.Lang("rustyconnector-sendUsage").generate());
                return;
            }

            sender.sendMessage("Sending " + username + " to " + familyOrServer);
        }, usernameArg, targetArg, flagsArg);

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(RC.Lang("rustyconnector-sendUsage").generate());
        });
    }
}
