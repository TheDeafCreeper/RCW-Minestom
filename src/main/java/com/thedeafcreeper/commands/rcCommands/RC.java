package com.thedeafcreeper.commands.rcCommands;

import net.minestom.server.command.builder.Command;

public class RC extends Command {
    public RC() {
        super("rcl");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(group.aelysium.rustyconnector.RC.Lang("rustyconnector-kernelDetails").generate(group.aelysium.rustyconnector.RC.Kernel()));
        });

        addSubcommand(new RCSend());
        addSubcommand(new RCReload());
        addSubcommand(new RCPacket());
        addSubcommand(new RCError());
    }
}
