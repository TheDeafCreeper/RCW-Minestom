package com.thedeafcreeper.commands.rcCommands;

import com.thedeafcreeper.Main;
import group.aelysium.ara.Particle;
import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.RustyConnector;
import group.aelysium.rustyconnector.common.errors.Error;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class RCReload extends Command {

    // TODO: This needs to be Async

    public RCReload() {
        super("reload");

        setDefaultExecutor((sender, context) -> {
            try {
                sender.sendMessage(RC.Lang("rustyconnector-waiting").generate());
                Particle.Flux<?> particle = RustyConnector.Kernel();
                particle.reignite();
                particle.observe();
                sender.sendMessage(RC.Lang("rustyconnector-finished").generate());
            } catch (Exception e) {
                RC.Error(Error.from(e).urgent(true));
            }
        });
    }
}
