package com.friska.javaaes.key;

import javax.annotation.Nonnull;

public record Word(@Nonnull byte[] bytes) {

    public Word{
        assert(bytes.length == 4);
    }

}
