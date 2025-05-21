package com.zonbeozon.exchange;

import com.zonbeozon.market.entity.MarketType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Exchange {
    UPBIT(
            "upbit",
            "업비트",
            "https://s2.coinmarketcap.com/static/img/exchanges/64x64/351.png",
            """
                    Upbit is the largest cryptocurrency exchange in South Korea in terms of trading volume and customer base,
                    with over 180 crypto coins/tokens listed and over 300 trading pairs readily available.
                    Upbit's ecosystem comprises of Upbit Exchange, NFT Marketplace/Drops platform, and its Staking services.
                    """,
            """
                    업비트는 거래량과 고객 수 기준으로 한국에서 가장 큰 암호화폐 거래소이며,
                    180개 이상의 암호화폐가 상장되어 있고,
                    300개 이상의 거래쌍이 제공됩니다.
                    업비트 생태계는 업비트 거래소, NFT 마켓플레이스/드롭스 플랫폼, 그리고 스테이킹 서비스로 구성되어 있습니다.
                    """,
            MarketType.KRW
    ),
    BITHUMB(
            "bithumb",
            "빗썸",
            "https://s2.coinmarketcap.com/static/img/exchanges/64x64/200.png",
            """
                    Bithumb is one of South Korea's largest crypto exchanges,
                    with $205 million in daily trading volume at the time of writing and with 170+ listed cryptocurrencies.
                    """,
            """
                    빗썸은 한국에서 가장 큰 암호화폐 거래소 중 하나로,
                    작성 시점 기준으로 하루 거래량이 2억 5백만 달러에 달하며,
                    170개 이상의 암호화폐가 상장되어 있습니다.
                    """,
            MarketType.KRW
            ),
    BINANCE(
            "binance",
            "바이낸스",
            "https://s2.coinmarketcap.com/static/img/exchanges/64x64/270.png",
            """
                        Binance is the world’s largest crypto exchange by trading volume,
                        with $76 billion daily trading volume on Binance exchange as of August 2022, and 90 million customers worldwide.
                        The platform has established itself as a trusted member of the crypto space,
                        where users can buy, sell and store their digital assets, as well as access over 350 cryptocurrencies listed and thousands of trading pairs.
                        The Binance ecosystem now comprises of Binance Exchange, Labs, Launchpad, Info, Academy, Research, Trust Wallet, Charity, NFT and more.
                    """,
            """
                    바이낸스는 전 세계 거래량 기준으로 가장 큰 암호화폐 거래소이며,
                    2022년 8월 기준 하루 거래량이 760억 달러에 달하고,
                    전 세계적으로 9천만 명 이상의 사용자를 보유하고 있습니다.
                    
                    이 플랫폼은 신뢰받는 암호화폐 생태계 구성원으로 자리매김하였으며,
                    사용자들은 이곳에서 디지털 자산을 매매하거나 보관할 수 있고,
                    350개 이상의 암호화폐와 수천 개의 거래 페어에 접근할 수 있습니다.
                    
                    바이낸스 생태계는 이제 바이낸스 거래소, 랩스(Labs), 런치패드(Launchpad),
                    인포(Info), 아카데미, 리서치, 트러스트 월렛(Trust Wallet),
                    자선재단(Charity), NFT 플랫폼 등을 포함합니다.
                    """,
            MarketType.USDT
    );

    private final String enName;
    private final String krName;
    private final String logo;
    private final String enDescription;
    private final String krDescription;
    private final MarketType priorityMarketType;

    public static Exchange parse(String exchangeName) {
        return Exchange.valueOf(exchangeName.toUpperCase());
    }
}
