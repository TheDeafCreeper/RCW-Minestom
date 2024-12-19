package group.aelysium.rustyconnector;

import com.thedeafcreeper.commands.rcCommands.RC;
import group.aelysium.declarative_yaml.DeclarativeYAML;
import group.aelysium.rustyconnector.common.config.GitOpsConfig;
import group.aelysium.rustyconnector.common.config.PrivateKeyConfig;
import group.aelysium.rustyconnector.common.errors.Error;
import group.aelysium.rustyconnector.common.lang.LangLibrary;
import group.aelysium.rustyconnector.serverCommon.DefaultConfig;
import group.aelysium.rustyconnector.serverCommon.ServerLang;
import group.aelysium.rustyconnector.server.ServerKernel;
import group.aelysium.rustyconnector.server.magic_link.WebSocketMagicLink;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

public final class MinestomRustyConnector {
    Instance server;

    public MinestomRustyConnector(Instance server) {
        this.server = server;

        ComponentLogger console = MinecraftServer.LOGGER;
        console.info("Initializing RustyConnector...");

        try {
            if(PrivateKeyConfig.Load().isEmpty()) {
                console.info(Component.join(
                        JoinConfiguration.newlines(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.text("Looks like I'm still waiting on a private.key from the proxy!", NamedTextColor.BLUE),
                        Component.text("You'll need to copy ", NamedTextColor.BLUE).append(Component.text("plugins/rustyconnector/metadata/aes.private", NamedTextColor.YELLOW)).append(Component.text(" and paste it into this server in that same folder!", NamedTextColor.BLUE)),
                        Component.text("Both the proxy and I need to have the same aes.private!", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.empty(),
                        Component.empty()
                ));
                return;
            }

            {
                GitOpsConfig config = GitOpsConfig.New();
                if(config != null) DeclarativeYAML.registerRepository("rustyconnector", config.config());
            }

            ServerKernel.Tinder tinder = DefaultConfig.New().data(
                    new MinestomServerAdapter(this.server)
            );
            RustyConnector.registerAndIgnite(tinder.flux());
            RustyConnector.Kernel(flux->{
                flux.onStart(kernel -> {
                    try {
                        kernel.fetchPlugin("LangLibrary").onStart(l -> ((LangLibrary) l).registerLangNodes(ServerLang.class));
                    } catch (Exception e) {
                        group.aelysium.rustyconnector.RC.Error(Error.from(e));
                    }
                    try {
                        kernel.fetchPlugin("MagicLink").onStart(l -> ((WebSocketMagicLink) l).connect());
                    } catch (Exception e) {
                        group.aelysium.rustyconnector.RC.Error(Error.from(e));
                    }
                });
            });

            MinecraftServer.getCommandManager().register(new RC());

            group.aelysium.rustyconnector.RC.Lang("rustyconnector-wordmark").send(group.aelysium.rustyconnector.RC.Kernel().version());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}