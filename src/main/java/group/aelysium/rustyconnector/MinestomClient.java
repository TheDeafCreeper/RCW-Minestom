package group.aelysium.rustyconnector;

import group.aelysium.rustyconnector.common.command.Client;
import group.aelysium.rustyconnector.common.errors.Error;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import org.jetbrains.annotations.NotNull;

public class MinestomClient implements Client<ConsoleSender> {
    private final ConsoleSender sender;

    public MinestomClient(@NotNull ConsoleSender sender) {
        this.sender = sender;
    }
    @Override
    public void enforceConsole() throws RuntimeException {
        if(this.sender.isConsole()) return;
        throw new RuntimeException("This command can only be used from the console.");
    }

    @Override
    public void enforcePlayer() throws RuntimeException {
        if(this.sender.isConsole()) throw new RuntimeException("This command can only be used by players.");
    }

    @Override
    public void send(Component message) {
        MinecraftServer.LOGGER.info(message);
    }

    @Override
    public void send(Error error) { MinecraftServer.LOGGER.error(error.toComponent()); }


    @Override
    public ConsoleSender toSender() {
        return this.sender;
    }
}
