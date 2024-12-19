package com.thedeafcreeper.commands.rcCommands;

import group.aelysium.ara.Particle;
import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.RustyConnector;
import group.aelysium.rustyconnector.common.crypt.NanoID;
import group.aelysium.rustyconnector.common.errors.Error;
import group.aelysium.rustyconnector.common.magic_link.packet.Packet;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class RCPacket extends Command {
    public RCPacket() {
        super("packet", "packets");

        addSubcommand(new RCPacketClear());

        ArgumentString packetArg = new ArgumentString("packet-id");
        packetArg.isOptional();

        addSyntax((sender, context) -> {
            final String id = context.get(packetArg);

            try {
                sender.sendMessage(RC.Lang("rustyconnector-packetDetails").generate(
                        RC.MagicLink().packetCache().find(NanoID.fromString(id)).orElseThrow(
                                ()->new NoSuchElementException("Unable to find packet with id "+id)
                        )
                ));
            } catch (Exception e) {
                RC.Error(Error.from(e).urgent(true));
            }
        }, packetArg);

        setDefaultExecutor((sender, context) -> {
            try {
                List<Packet> messages = RC.MagicLink().packetCache().packets();
                sender.sendMessage(RC.Lang("rustyconnector-packets").generate(messages));
            } catch (Exception e) {
                RC.Error(Error.from(e).urgent(true));
            }
        });
    }
}
