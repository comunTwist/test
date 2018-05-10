package com.gmail.agentup.repository;

import com.gmail.agentup.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query("SELECT c FROM Currency c WHERE c.userOwner.id = :id")
    List<Currency> findAllUserCurrencies(@Param("id") Long id);
}
