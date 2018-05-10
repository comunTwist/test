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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class CurrencyController {
  @Autowired
  private UserService userService;
  @Autowired
  private CurrencyService currencyService;
  @Autowired
  private ExchangeService exchangeService;

  @RequestMapping("/currencies")
  public String index(Model model) {
    List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser().getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("cash", cashMap);
    model.addAttribute("currencies", currencies);
    model.addAttribute("roles", dbUser().getRole().toString());
    model.addAttribute("login", dbUser().getLogin());

    return "currencies";
  }

  @RequestMapping(value = "/addCurrency", method = RequestMethod.POST)
  public String add(@RequestParam String name,
                    @RequestParam(required = false) String fullName,
                    @RequestParam(required = false) Double rate) {
    if (!name.equals("")) {
      if (fullName.equals("")) {
        fullName = name;
      }
      if (rate == null) {
        rate = 1.0;
      }
      Currency currency = new Currency(name, fullName);
      Exchange exchange = new Exchange(rate, new Date());
      currency.addExchange(exchange);
      dbUser().addCurrency(currency);
      exchangeService.addExchange(exchange);
      currencyService.addCurrency(currency);
    }
    return "redirect:/currencies";
  }

  @RequestMapping(value = "/updateCurrency", method = RequestMethod.POST)
  public String update(@RequestParam Long currencyId,
                       @RequestParam String name,
                       @RequestParam String fullName) {
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency) && !name.equals("")) {
      if (fullName.equals("")) {
        fullName = name;
      }
      currency.setName(name);
      currency.setFullName(fullName);
      currencyService.updateCurrency(currency);
    }

    return "redirect:/currencies";
  }

  @RequestMapping(value = "/deleteCurrency", method = RequestMethod.POST)
  public String delete(@RequestParam Long currencyId) {
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency) && Assistant.isNoLinksCurrency(currency)) {
      dbUser().getCurrencies().remove(currency);
      currencyService.deleteCurrency(currency);
    }

    return "redirect:/currencies";
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }
}
