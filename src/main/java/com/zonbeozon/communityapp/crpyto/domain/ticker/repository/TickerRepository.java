package com.zonbeozon.communityapp.crpyto.domain.ticker.repository;

import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TickerRepository extends JpaRepository<Ticker, Long> {
}
