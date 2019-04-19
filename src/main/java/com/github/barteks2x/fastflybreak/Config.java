package com.github.barteks2x.fastflybreak;

import net.fabricmc.loader.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Path CONFIG = FabricLoader.INSTANCE.getConfigDirectory().toPath().resolve("fastflyblockbreaking.properties");
    public static boolean alwaysFastBreaking = false;

    static {
        try {
            loadState();
            saveState();
        } catch (Exception | LinkageError t) {
            t.printStackTrace();
        }
    }

    public static void loadState() {
        final Properties properties = new Properties();
        try (final InputStream input = Files.newInputStream(CONFIG)) {
            properties.load(input);
        } catch (final NoSuchFileException ignored) {
            // first run, state has never been set at runtime
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
        final String property = properties.getProperty("alwaysFastBreaking");
        alwaysFastBreaking = "true".equalsIgnoreCase(property);
    }

    private static void saveState() {
        final Properties properties = new Properties();
        properties.setProperty("alwaysFastBreaking", String.valueOf(alwaysFastBreaking));
        try (final OutputStream output = Files.newOutputStream(CONFIG)) {
            properties.store(output, null);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
