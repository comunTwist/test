package com.gmail.agentup.services;

import com.gmail.agentup.model.Currency;

import java.util.List;

public interface CurrencyService {
    Currency getCurrencyById(Long id);

    List<Currency> getAllUserCurrencies(Long userId);

    void addCurrency(Currency currency);

    void updateCurrency(Currency currency);

    void deleteCurrency(Currency currency);
}
