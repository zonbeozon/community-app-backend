package com.zonbeozon.communityapp.crpyto.execute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExecuteType {
    ONE_TIME(0), FIVE_SEC(5000), ONE_HOUR(3600000);

    private final int delaySec;
}
