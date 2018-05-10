package com.gmail.agentup.repository;

import com.gmail.agentup.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    @Query("SELECT e FROM Exchange e WHERE e.currency.id = :id")
    List<Exchange> findAllCurrencyExchanges(@Param("id") Long id);

    @Query(value = "SELECT * FROM Exchanges WHERE currency_id = :id ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Exchange findLastCurrencyExchanges(@Param("id") Long id);
}
