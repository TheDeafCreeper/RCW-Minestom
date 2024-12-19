package group.aelysium.rustyconnector.common.command;

import group.aelysium.ara.Particle;
import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.RustyConnector;
import group.aelysium.rustyconnector.common.crypt.NanoID;
import group.aelysium.rustyconnector.common.errors.Error;
import group.aelysium.rustyconnector.common.magic_link.packet.Packet;
import group.aelysium.rustyconnector.common.plugins.PluginHolder;
import group.aelysium.rustyconnector.common.lang.CommonLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static net.kyori.adventure.text.Component.text;

@Command("rc")
@Permission("rustyconnector.commands.rc")
public class CommonCommands {
    @Command("plugin")
    @Command("plugins")
    public void nglbwcmuvchdjaon(Client<?> client) {
        try {
            client.send(RC.Lang("rustyconnector-pluginList").generate(RC.Kernel().plugins().keySet()));
        } catch (Exception e) {
            RC.Error(Error.from(e).urgent(true));
        }
    }
    @Command("plugin <pluginTree>")
    @Command("plugins <pluginTree>")
    public void nglbwcmuschdjaon(Client<?> client, String pluginTree) {
        try {
            Particle.Flux<?> flux = fetchPlugin(client, pluginTree);
            if(!flux.exists()) {
                client.send(
                    Error.withHint(
                                "While attempting to fetch the plugin "+pluginTree+" a plugin in the chain was unavailable.",
                                "This issue typically arises when a plugin is being reloaded. In which case wait a bit before attempting to access it."
                        )
                        .causedBy("Attempting to fetch the plugin "+pluginTree)
                );
                return;
            }

            client.send(RC.Lang("rustyconnector-details").generate(flux));
        } catch (Exception e) {
            RC.Error(Error.from(e).urgent(true));
        }
    }

    @Command("plugin <pluginTree> reload")
    @Command("plugins <pluginTree> reload")
    public void nglbwzmspchdjaon(Client<?> client, String pluginTree) {
        try {
            Particle.Flux<?> flux = fetchPlugin(client, pluginTree);
            if(flux == null) return;
            client.send(RC.Lang("rustyconnector-waiting").generate());
            flux.reignite().get();
            client.send(RC.Lang("rustyconnector-finished").generate());
        } catch (Exception e) {
            RC.Error(Error.from(e).urgent(true));
        }
    }
    @Command("plugin <pluginTree> stop")
    @Command("plugins <pluginTree> stop")
    public void nglbwzmzpsodjaon(Client<?> client, String pluginTree) {
        Particle.Flux<?> flux = fetchPlugin(client, pluginTree);
        if(flux == null) return;
        if(!flux.exists()) {
            client.send(RC.Lang("rustyconnector-pluginAlreadyStopped").generate());
            return;
        }
        client.send(RC.Lang("rustyconnector-waiting").generate());
        flux.close();
        try {
            client.send(RC.Lang("rustyconnector-finished").generate());
        } catch (NoSuchElementException e) {
            client.send(Component.text("Successfully stopped that plugin!"));
        }
    }
    @Command("plugin <pluginTree> start")
    @Command("plugins <pluginTree> start")
    public void asfdmgfsgsodjaon(Client<?> client, String pluginTree) {
        try {
            Particle.Flux<?> flux = fetchPlugin(client, pluginTree);
            if(flux == null) return;
            if(flux.exists()) {
                client.send(RC.Lang("rustyconnector-pluginAlreadyStarted").generate());
                return;
            }
            client.send(RC.Lang("rustyconnector-waiting").generate());
            flux.observe();
            client.send(RC.Lang("rustyconnector-finished").generate());
        } catch (Exception e) {
            RC.Error(Error.from(e).urgent(true));
        }
    }

    private static @Nullable Particle.Flux<? extends Particle> fetchPlugin(Client<?> client, String pluginTree) {
        String[] nodes = pluginTree.split("\\.");
        AtomicReference<Particle.Flux<? extends Particle>> current = new AtomicReference<>(RustyConnector.Kernel());

        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            boolean isLast = i == (nodes.length - 1);
            if(!current.get().exists()) {
                client.send(Error.withHint(
                                "While attempting to fetch the plugin "+pluginTree+" a plugin in the chain was unavailable.",
                                "This issue typically arises when a plugin is being reloaded. In which case wait a bit before attempting to access it."
                        )
                        .causedBy("Attempting to fetch the plugin "+pluginTree)
                );
                return null;
            }

            String name = current.get().metadata("name");
            if(name == null) throw new IllegalArgumentException("Fluxes provided to `rustyconnector-details` must contain `name`, `description`, and `details` metadata.");

            Particle plugin = null;
            try {
                plugin = current.get().observe(3, TimeUnit.SECONDS);
            } catch(Exception ignore) {}

            if(!(plugin instanceof PluginHolder pluginHolder)) {
                client.send(Error.from(
                                node+" doesn't exist on "+name+". "+name+" actually doesn't have any children plugins.")
                        .causedBy("Attempting to fetch the plugin "+pluginTree)
                );
                return null;
            }

            Particle.Flux<? extends Particle> newCurrent = pluginHolder.plugins().get(node);
            if(newCurrent == null) {
                client.send(Error.withSolution(
                            node+" doesn't exist on "+name+".",
                            "Available plugins are: "+String.join(", "+pluginHolder.plugins().keySet())
                    )
                    .causedBy("Attempting to fetch the plugin "+pluginTree)
                );
                return null;
            }
            if(!newCurrent.exists() && !isLast) {
                client.send(Error.withHint(
                                "Despite existing and being correct; "+node+" is not currently available. It's probably rebooting.",
                                "This issue typically occurs when a plugin is restarting. You can try again after a little bit, or try reloading the plugin directly and see if that works."
                        )
                        .causedBy("Attempting to fetch the plugin "+pluginTree)
                );
                return null;
            }

            current.set(newCurrent);
        }

        return current.get();
    }

    @Command("send")
    public void acmednrmiufxxviz(Client<?> client) {
        client.send(RC.Lang("rustyconnector-sendUsage").generate());
    }
    @Command("send <playerTarget>")
    public void acmednrmiusgxviz(Client<?> client, String playerTarget) {
        acmednrmiufxxviz(client);
    }
}