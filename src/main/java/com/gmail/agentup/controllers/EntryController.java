package com.gmail.agentup.controllers;

import com.gmail.agentup.model.Currency;
import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.EntryType;
import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.services.Assistant;
import com.gmail.agentup.services.CurrencyService;
import com.gmail.agentup.services.EntryService;
import com.gmail.agentup.services.UserService;

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
public class EntryController {
  @Autowired
  private UserService userService;
  @Autowired
  private EntryService entryService;
  @Autowired
  private CurrencyService currencyService;

  @RequestMapping("/entries")
  public String index(Model model) {
    List<Entry> entries = entryService.getAllUserEntries(dbUser().getId());
    List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser().getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("cash", cashMap);
    model.addAttribute("entries", entries);
    model.addAttribute("roles", dbUser().getRole().toString());
    model.addAttribute("login", dbUser().getLogin());

    return "entries";
  }

  @RequestMapping(value = "/addEntry", method = RequestMethod.POST)
  public String add(@RequestParam String name) {
    if (!name.equals("")) {
      Entry entry = new Entry(name, EntryType.GENERAL);
      dbUser().addEntry(entry);
      entryService.addEntry(entry);
    }

    return "redirect:/entries";
  }

  @RequestMapping(value = "/updateEntry", method = RequestMethod.POST)
  public String update(@RequestParam Long entryId,
                       @RequestParam String name) {
    Entry entry = entryService.getEntryById(entryId);
    if (Assistant.isUserEntry(dbUser(), entry) && !name.equals("")) {
      entry.setName(name);
      entryService.updateEntry(entry);
    }

    return "redirect:/entries";
  }

  @RequestMapping(value = "/deleteEntry", method = RequestMethod.POST)
  public String delete(@RequestParam Long entryId) {
    Entry entry = entryService.getEntryById(entryId);
    if (Assistant.isUserEntry(dbUser(), entry) && Assistant.isNoLinksEntry(entry)) {
      dbUser().getEntries().remove(entry);
      entryService.deleteEntry(entry);
    }

    return "redirect:/entries";
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }
}
