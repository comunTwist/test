package com.gmail.agentup.controllers;


import com.gmail.agentup.model.Account;
import com.gmail.agentup.model.Currency;
import com.gmail.agentup.model.Exchange;
import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class AccountController {
  @Autowired
  private UserService userService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private CurrencyService currencyService;
  @Autowired
  private ExchangeService exchangeService;

  @RequestMapping("/accounts")
  public String index(Model model) {
    List<Account> accounts = accountService.getAllUserAccounts(dbUser().getId());
    List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser().getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("accounts", accounts);
    model.addAttribute("currencies", currencies);
    model.addAttribute("roles", dbUser().getRole().toString());
    model.addAttribute("login", dbUser().getLogin());
    model.addAttribute("cash", cashMap);

    return "accounts";
  }

  @RequestMapping(value = "/addAccount", method = RequestMethod.POST)
  public String add(@RequestParam Long currencyId,
                    @RequestParam String name,
                    @RequestParam(required = false) Double sum) {
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency) && !name.equals("")) {
      sum = Assistant.ifDoubleNull(sum);
      Account account = new Account(name, sum);
      currency.addAccount(account);
      dbUser().addAccount(account);
      accountService.addAccount(account);
    }

    return "redirect:/accounts";
  }

  @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
  public String update(@RequestParam Long accountId,
                       @RequestParam Long currencyId,
                       @RequestParam String name,
                       @RequestParam Double restSum) {
    Account account = accountService.getAccountById(accountId);
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserAccount(dbUser(), account) && Assistant.isUserCurrency(dbUser(), currency) && !name.equals("")) {
      Exchange exchange = exchangeService.getLastDateExchange(account.getCurrency().getId());
      Exchange newExchange = exchangeService.getLastDateExchange(currencyId);
      Double sum = restSum;
      if (sum == null) {
        sum = account.getSum();
      }
      account.setSum(Assistant.roundResult(sum * (exchange.getRate() / newExchange.getRate())));
      account.setName(name);
      account.setCurrency(currencyService.getCurrencyById(currencyId));
      accountService.updateAccount(account);
    }

    return "redirect:/accounts";
  }

  @RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
  public String delete(@RequestParam Long accountId) {
    Account account = accountService.getAccountById(accountId);
    if (Assistant.isUserAccount(dbUser(), account) && Assistant.isNoLinksAccount(account)) {
      dbUser().getAccounts().remove(account);
      accountService.deleteAccount(account);
    }
    return "redirect:/accounts";
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }
}
