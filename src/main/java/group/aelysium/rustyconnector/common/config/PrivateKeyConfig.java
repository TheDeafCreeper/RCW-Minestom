package group.aelysium.rustyconnector.common.config;

import group.aelysium.declarative_yaml.*;
import group.aelysium.declarative_yaml.annotations.AllContents;
import group.aelysium.declarative_yaml.annotations.Config;
import group.aelysium.declarative_yaml.annotations.Git;
import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.common.crypt.AES;
import group.aelysium.rustyconnector.common.errors.Error;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Config("rustyconnector/metadata/aes.private")
@Git(value = "rustyconnector", required = false)
public class PrivateKeyConfig {
    @AllContents()
    private byte[] key;

    public AES cryptor() {
        return AES.from(Base64.getDecoder().decode(this.key));
    }

    public static PrivateKeyConfig New() throws IOException {
        // This logic only cares about generating the config if it doesn't exist.
        File file = new File("rustyconnector/metadata/aes.private");
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists()) parent.mkdirs();

                try(FileWriter writer = new FileWriter(file)) {
                    writer.write(new String(Base64.getEncoder().encode(AES.createKey()), StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            RC.Error(Error.from(e));
        }
        return DeclarativeYAML.load(PrivateKeyConfig.class);
    }
    public static Optional<PrivateKeyConfig> Load() throws IOException {
        File file = new File("rustyconnector/metadata/aes.private");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                return Optional.empty();
            }
        } catch (Exception e) {
            RC.Error(Error.from(e));
        }

        return Optional.of(DeclarativeYAML.load(PrivateKeyConfig.class));
    }
}
