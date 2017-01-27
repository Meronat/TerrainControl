package com.khorn.terraincontrol.sponge;

import org.slf4j.MarkerFactory;

import com.khorn.terraincontrol.logging.LogMarker;
import com.khorn.terraincontrol.logging.Logger;

public class SpongeLogger extends Logger {

    private org.slf4j.Logger logger;

    SpongeLogger(org.slf4j.Logger logger) {

        this.logger = logger;

    }

    @Override
    public void log(LogMarker level, String message, Object... params) {

        if (minimumLevel.compareTo(level) < 0) {
            // Only log messages that we want to see...
            return;
        }

        switch (level) {

            case FATAL:
                // slf4j does not have a fatal logger, so add a marker to error.
                this.logger.error(MarkerFactory.getMarker("FATAL"), message, params);
                break;
            case ERROR:
                this.logger.error(message, params);
                break;
            case WARN:
                this.logger.warn(message, params);
                break;
            case INFO:
                this.logger.info(message, params);
                break;
            case DEBUG:
                this.logger.debug(message, params);
                break;
            case TRACE:
                this.logger.trace(message, params);
                break;
            default:
                // Unknown log level, should never happen
                this.logger.info(message, params); // Still log the message
                throw new RuntimeException("Unknown log marker: " + level);

        }

    }

}
