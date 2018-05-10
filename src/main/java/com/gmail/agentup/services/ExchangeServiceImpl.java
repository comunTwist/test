package com.gmail.agentup.services;

import com.gmail.agentup.model.Exchange;
import com.gmail.agentup.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private ExchangeRepository exchangeRepository;

    @Override
    @Transactional(readOnly = true)
    public Exchange getExchangeById(Long id) {
        return exchangeRepository.getOne(id);
    }
    @Override
    @Transactional(readOnly = true)
    public Exchange getLastDateExchange(Long currencyId){
        return exchangeRepository.findLastCurrencyExchanges(currencyId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Exchange> getAllCurrencyExchanges(Long currencyId) {
        return exchangeRepository.findAllCurrencyExchanges(currencyId);
    }

    @Override
    @Transactional
    public void addExchange(Exchange exchange) {
        exchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public void updateExchange(Exchange exchange) {
        exchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public void deleteExchange(Exchange exchange) {
        exchangeRepository.delete(exchange);
    }
}
