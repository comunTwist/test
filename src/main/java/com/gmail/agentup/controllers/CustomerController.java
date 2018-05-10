package com.gmail.agentup.controllers;

import com.gmail.agentup.model.Currency;
import com.gmail.agentup.model.Customer;
import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.services.Assistant;
import com.gmail.agentup.services.CurrencyService;
import com.gmail.agentup.services.CustomerService;
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
public class CustomerController {
  @Autowired
  private UserService userService;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private CurrencyService currencyService;

  @RequestMapping("/customers")
  public String index(Model model) {
    List<Customer> customers = customerService.getAllUserCustomers(dbUser().getId());
    List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser().getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("cash", cashMap);
    model.addAttribute("customers", customers);
    model.addAttribute("roles", dbUser().getRole().toString());
    model.addAttribute("login", dbUser().getLogin());

    return "customers";
  }

  @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
  public String add(@RequestParam String name) {
    if (!name.equals("")) {
      Customer customer = new Customer(name);
      dbUser().addCustomer(customer);
      customerService.addCustomer(customer);
    }
    return "redirect:/customers";
  }

  @RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
  public String update(@RequestParam Long customerId,
                       @RequestParam String name) {
    Customer customer = customerService.getCustomerById(customerId);
    if (Assistant.isUserCustomer(dbUser(), customer) && !name.equals("")) {
      customer.setName(name);
      customerService.updateCustomer(customer);
    }

    return "redirect:/customers";
  }

  @RequestMapping(value = "/deleteCustomer", method = RequestMethod.POST)
  public String delete(@RequestParam Long customerId) {
    Customer customer = customerService.getCustomerById(customerId);
    if (Assistant.isUserCustomer(dbUser(), customer) && Assistant.isNoLinksCustomer(customer)) {
      dbUser().getCustomers().remove(customer);
      customerService.deleteCustomer(customer);
    }

    return "redirect:/customers";
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }
}
