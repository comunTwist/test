package com.gmail.agentup.services;

import com.gmail.agentup.model.Exchange;

import java.util.List;

public interface ExchangeService {
    Exchange getExchangeById(Long id);

    Exchange getLastDateExchange(Long currencyId);

    List<Exchange> getAllCurrencyExchanges(Long currencyId);

    void addExchange(Exchange exchange);

    void updateExchange(Exchange exchange);

    void deleteExchange(Exchange exchange);
}
