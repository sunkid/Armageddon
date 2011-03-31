package com.iminurnetz.bukkit.plugin;

import org.bukkit.plugin.AuthorNagException;

public class UnsupportedServerVersionException extends AuthorNagException {
    private static final long serialVersionUID = 1L;

    public UnsupportedServerVersionException(String message) {
        super(message);
    }

}
