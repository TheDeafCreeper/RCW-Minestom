package com.thedeafcreeper.commands.rcCommands;

import group.aelysium.ara.Particle;
import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.RustyConnector;
import group.aelysium.rustyconnector.common.errors.Error;
import net.minestom.server.command.builder.Command;

public class RCPacketClear extends Command {
    public RCPacketClear() {
        super("clear");

        setDefaultExecutor((sender, context) -> {
            try {
                sender.sendMessage(RC.Lang("rustyconnector-waiting").generate());
                RC.MagicLink().packetCache().empty();
                sender.sendMessage(RC.Lang("rustyconnector-finished").generate());
            } catch (Exception e) {
                RC.Error(Error.from(e).urgent(true));
            }
        });
    }
}
