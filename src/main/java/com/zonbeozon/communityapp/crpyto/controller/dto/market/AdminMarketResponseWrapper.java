package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import java.util.List;

public record AdminMarketResponseWrapper(
        List<AdminMarketResponse> markets,
        Integer size
) {
}
