package com.gmail.agentup.services;

import com.gmail.agentup.model.Currency;
import com.gmail.agentup.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    @Transactional(readOnly = true)
    public Currency getCurrencyById(Long id) {
        return currencyRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAllUserCurrencies(Long userId) {
        return currencyRepository.findAllUserCurrencies(userId);
    }

    @Override
    @Transactional
    public void addCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void updateCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void deleteCurrency(Currency currency) {
        currencyRepository.delete(currency);
    }
}
