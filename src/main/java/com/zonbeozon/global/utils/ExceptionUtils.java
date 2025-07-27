package com.zonbeozon.global.utils;

import java.util.Optional;

public class ExceptionUtils {
    public static Optional<Throwable> getCause(Throwable throwable, Class<? extends Throwable> causeType) {
        Throwable chain = throwable;
        while(chain != null) {
            if(causeType.isInstance(chain)) return Optional.of(causeType.cast(chain));
            chain = chain.getCause();
        }
        return Optional.empty();
    }

    public static boolean hasCause(Throwable throwable, Class<? extends Throwable> causeType) {
        Optional<Throwable> optCause = getCause(throwable, causeType);
        return optCause.isPresent();
    }
}
