package com.thedeafcreeper.commands.rcCommands;

import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.common.crypt.NanoID;
import group.aelysium.rustyconnector.common.errors.Error;
import group.aelysium.rustyconnector.common.lang.CommonLang;
import group.aelysium.rustyconnector.common.magic_link.packet.Packet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class RCError extends Command {
    public RCError() {
        super("error", "errors");

        ArgumentString errorArg = new ArgumentString("error-id");
        errorArg.isOptional();

        addSyntax((sender, context) -> {
            final String passedArg = context.get(errorArg);

            UUID uuid;

            try {
                uuid = UUID.fromString(passedArg);
            } catch (Exception err) {
                sender.sendMessage(Component.text("Please provide a valid UUID.", NamedTextColor.RED));
                return;
            }

            try {
                Error error = RC.Errors().fetch(uuid)
                        .orElseThrow(()->new NoSuchElementException("No Error entry exists with the uuid ["+uuid+"]"));
                if(error.throwable() == null) sender.sendMessage(text("The error ["+uuid+"] doesn't have a throwable to inspect.", NamedTextColor.BLUE));
                RC.Adapter().log(RC.Lang("rustyconnector-exception").generate(error.throwable()));
            } catch (Exception e) {
                RC.Error(Error.from(e).urgent(true));
            }
        }, errorArg);

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(
                    Component.join(
                            CommonLang.newlines(),
                            Component.empty(),
                            RC.Lang().asciiAlphabet().generate("Errors").color(NamedTextColor.BLUE),
                            Component.empty(),
                            (
                                    RC.Errors().fetchAll().isEmpty() ?
                                            text("There are no errors to show.", NamedTextColor.DARK_GRAY)
                                            :
                                            Component.join(
                                                    CommonLang.newlines(),
                                                    RC.Errors().fetchAll().stream().map(e->Component.join(
                                                            CommonLang.newlines(),
                                                            text("------------------------------------------------------", NamedTextColor.DARK_GRAY),
                                                            e.toComponent()
                                                    )).toList()
                                            )
                            ),
                            Component.empty()
                    )
            );
        });
    }
}
