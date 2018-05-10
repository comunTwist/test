package com.gmail.agentup.controllers;

import com.gmail.agentup.model.*;
import com.gmail.agentup.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private CurrencyService currencyService;

  @RequestMapping("/settings")
  public String settings(Model model) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserOwner dbUser = userService.getUserByLogin(user.getUsername());

    List<Customer> customers = customerService.getAllUserCustomers(dbUser.getId());
    List<Account> accounts = accountService.getAllUserAccounts(dbUser.getId());
    List<Currency> currencies = currencyService.getAllUserCurrencies(dbUser.getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("cash", cashMap);
    model.addAttribute("login", dbUser.getLogin());
    model.addAttribute("roles", user.getAuthorities());
    model.addAttribute("defaultCustomer", dbUser.getDefaultCustomer());
    model.addAttribute("defaultAccount", dbUser.getDefaultAccount());
    model.addAttribute("customers", customers);
    model.addAttribute("accounts", accounts);
    model.addAttribute("currencies", currencies);

    return "settings";
  }

  @RequestMapping("/register")
  public String register() {
    return "register";
  }

  @RequestMapping(value = "/newuser", method = RequestMethod.POST)
  public String add(@RequestParam String login,
                    @RequestParam String password,
                    Model model) {
    if (userService.existsByLogin(login)) {
      model.addAttribute("exists", true);
      return "register";
    }

    ShaPasswordEncoder encoder = new ShaPasswordEncoder();
    String passHash = encoder.encodePassword(password, null);

    UserOwner dbUser = new UserOwner(login, passHash, UserRole.USER);
    defaultUser(dbUser);

    return "redirect:/login?registered";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(@RequestParam Long customerId,
                       @RequestParam Long accountId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserOwner dbUser = userService.getUserByLogin(user.getUsername());
    dbUser.setDefaultCustomer(customerService.getCustomerById(customerId));
    dbUser.setDefaultAccount(accountService.getAccountById(accountId));
    userService.updateUser(dbUser);

    return "redirect:/settings";
  }

  @RequestMapping("/admin")
  public String admin(@RequestParam(value = "check", required = false) String[] check,
                      @RequestParam(required = false) String checkRole,
                      @RequestParam(required = false) String userLogin,
                      Model model) {
    if (check != null && check.length > 0) {
      for (String login : check) {
        UserOwner dbUser = userService.getUserByLogin(login);
        userService.deleteUser(dbUser);
      }
    }
    if (checkRole != null && userLogin != null) {
        UserOwner dbUser = userService.getUserByLogin(userLogin);
        dbUser.setRole(UserRole.valueOf(checkRole));
        userService.updateUser(dbUser);
    }
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserOwner admin = userService.getUserByLogin(user.getUsername());
    List<UserOwner> users = userService.getAllUsers();
    List<Currency> currencies = currencyService.getAllUserCurrencies(admin.getId());
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);

    model.addAttribute("cash", cashMap);
    model.addAttribute("login", admin.getLogin());
    model.addAttribute("roles", user.getAuthorities());
    model.addAttribute("allRoles", UserRole.values());
    model.addAttribute("users", users);
    return "/admin";
  }

  @RequestMapping("/unauthorized")
  public String unauthorized(Model model) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    model.addAttribute("login", user.getUsername());
    return "unauthorized";
  }

  private void defaultUser(UserOwner userOwner) {
    Currency currency = new Currency("UAH", "Hrivna");
    Exchange exchange = new Exchange(1, new Date());
    Account account = new Account("Master account", 0);
    Customer customer = new Customer("Main customer");
    Entry entry = new Entry("Market", EntryType.GENERAL);
    Entry transfer = new Entry("Transfer", EntryType.TRANSFER);

    currency.addExchange(exchange);
    currency.addAccount(account);
    userOwner.addCurrency(currency);
    userOwner.addAccount(account);//Избыточная связь?
    userOwner.addCustomer(customer);
    userOwner.addEntry(entry);
    userOwner.addEntry(transfer);
    userOwner.setDefaultCustomer(customer);
    userOwner.setDefaultAccount(account);
    userService.addUser(userOwner);
  }
}
