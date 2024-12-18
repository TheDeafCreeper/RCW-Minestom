import group.aelysium.rustyconnector.MinestomRustyConnector;
import group.aelysium.rustyconnector.RustyConnector;
import group.aelysium.rustyconnector.serverCommon.DefaultConfig;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.io.IOException;
import java.util.Objects;

public class Main {

    static ServerProperties serverProperties;

    public static void main(String[] args) {

        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        try {
            serverProperties = ServerProperties.New();
        } catch (IOException error) {
            MinecraftServer.LOGGER.error("Failed to load server properties, shutting down.", error);
            return;
        }

        // Create the instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        // Start the server on either the RC port or the port defined in server properties.
        if (serverProperties.port == -1) minecraftServer.start("0.0.0.0", getPort());
        else minecraftServer.start("0.0.0.0", serverProperties.port);

        if (!Objects.equals(serverProperties.velocitySecret, "")) VelocityProxy.enable(serverProperties.velocitySecret);

        new MinestomRustyConnector(instanceManager.getInstance(instanceContainer.getUniqueId()));
    }

    private static int getPort() {
        try {
            String[] connectionAddress = DefaultConfig.New().address.split(":");
            return Integer.parseInt(connectionAddress[1]);
        } catch (Exception ignore) {
            return 25565;
        }
    }
}
