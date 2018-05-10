package com.gmail.agentup.controllers;

import com.gmail.agentup.model.Currency;
import com.gmail.agentup.model.Exchange;
import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.services.Assistant;
import com.gmail.agentup.services.CurrencyService;
import com.gmail.agentup.services.ExchangeService;

import com.gmail.agentup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class ExchangeController {
  @Autowired
  private UserService userService;
  @Autowired
  private ExchangeService exchangeService;
  @Autowired
  private CurrencyService currencyService;

  @RequestMapping("/exchanges/{id}")
  public String index(@PathVariable(value = "id") Long currencyId, Model model) {
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency)) {
      List<Exchange> exchanges = exchangeService.getAllCurrencyExchanges(currencyId);
      List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser().getId());
      Map<String, Double> cashMap = new TreeMap<>();
      cashMap = Assistant.currencyCash(cashMap, currencies);

      model.addAttribute("cash", cashMap);
      model.addAttribute("currency", currency);
      model.addAttribute("exchanges", exchanges);
      model.addAttribute("roles", dbUser().getRole().toString());
      model.addAttribute("login", dbUser().getLogin());
    }
    return "exchanges";
  }

  @RequestMapping(value = "/addExchange", method = RequestMethod.POST)
  public String add(@RequestParam Long currencyId,
                    @RequestParam(required = false) Double rate) {
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency)) {
      if (rate == null) {
        rate = 1.0;
      }
      Exchange exchange = new Exchange(rate, new Date());
      currency.addExchange(exchange);
      exchangeService.addExchange(exchange);
    }

    return "redirect:/exchanges/" + currency.getId();
  }

  @RequestMapping(value = "/updateExchange", method = RequestMethod.POST)
  public String update(@RequestParam Long exchangeId,
                       @RequestParam Double rate) {
    Exchange exchange = exchangeService.getExchangeById(exchangeId);
    Currency currency = currencyService.getCurrencyById(exchange.getCurrency().getId());
    if (Assistant.isUserCurrency(dbUser(), currency)) {
      exchange.setRate(rate);
      exchangeService.updateExchange(exchange);
    }

    return "redirect:/exchanges/" + exchange.getCurrency().getId();
  }

  @RequestMapping(value = "/deleteExchange", method = RequestMethod.POST)
  public String delete(@RequestParam Long exchangeId) {
    Exchange exchange = exchangeService.getExchangeById(exchangeId);
    Currency currency = currencyService.getCurrencyById(exchange.getCurrency().getId());
    if (Assistant.isUserCurrency(dbUser(), currency) && Assistant.isNoLinksExchange(exchange)) {
      currency.getExchanges().remove(exchange);
      exchangeService.deleteExchange(exchange);
    }

    return "redirect:/exchanges/" + exchange.getCurrency().getId();
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }
}
