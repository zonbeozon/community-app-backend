package com.zonbeozon.currency.test;

import com.zonbeozon.currency.fetch.CMCMetadataResponse;

import java.util.List;
import java.util.Map;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;

public class CMCMetadataDummy {
    // BTC Metadata
    public static final CMCMetadataResponse.CMCMetadata BTC_CMC_METADATA = new CMCMetadataResponse.CMCMetadata(
            BTC_EN_NAME,
            BTC_SYMBOL,
            BTC_LOGO,
            BTC_EN_DESCRIPTION,
            new CMCMetadataResponse.CMCUrls(List.of(BTC_WEBSITE))
    );
    // ETH Metadata
    public static final CMCMetadataResponse.CMCMetadata ETH_CMC_METADATA = new CMCMetadataResponse.CMCMetadata(
            ETH_EN_NAME,
            ETH_SYMBOL,
            ETH_LOGO,
            ETH_EN_DESCRIPTION,
            new CMCMetadataResponse.CMCUrls(List.of(ETH_WEBSITE))
    );

    public static final CMCMetadataResponse CMC_METADATA_RESPONSE = new CMCMetadataResponse(
            Map.of(
                    BTC_SYMBOL, BTC_CMC_METADATA,
                    ETH_SYMBOL, ETH_CMC_METADATA
            )
    );

}
