package com.thedeafcreeper;

import group.aelysium.declarative_yaml.DeclarativeYAML;
import group.aelysium.declarative_yaml.annotations.Comment;
import group.aelysium.declarative_yaml.annotations.Config;
import group.aelysium.declarative_yaml.annotations.Node;

import java.io.IOException;

@Config("server-properties.yml")
public class ServerProperties {

    @Comment({
            "#",
            "# The port for Minestom to accept connections on.",
            "# Leave as -1 to use the port provided in the RC config.",
            "#"
            })
    @Node(0)
    public int port = -1;

    @Node(1)
    public String velocitySecret = "";

    public static ServerProperties New() throws IOException {
        return DeclarativeYAML.load(ServerProperties.class);
    }
}
