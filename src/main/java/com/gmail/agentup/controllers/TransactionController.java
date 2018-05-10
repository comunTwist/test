package com.gmail.agentup.controllers;

import com.gmail.agentup.model.*;
import com.gmail.agentup.model.Currency;
import com.gmail.agentup.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class TransactionController {
  @Autowired
  private TransactionService transactionService;
  @Autowired
  private UserService userService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private EntryService entryService;
  @Autowired
  private CurrencyService currencyService;
  @Autowired
  private ExchangeService exchangeService;

  @RequestMapping("/")
  public String index(@RequestParam(required = false) Long accountId,
                      @RequestParam(required = false) Long customerId,
                      @RequestParam(required = false) Long entryId,
                      @RequestParam(required = false) Long currencyId,
                      Model model) {
    Long id = dbUser().getId();
    List<Account> accounts = accountService.getAllUserAccounts(id);
    List<Customer> customers = customerService.getAllUserCustomers(id);
    List<Entry> entries = entryService.getAllUserEntries(id);
    List<Currency> currencies = currencyService.getAllUserCurrencies(id);
    Map<String, Double> cashMap = new TreeMap<>();
    cashMap = Assistant.currencyCash(cashMap, currencies);
    List<Transaction> transactions = transactionService.getUserTransactionsByFilter(dbUser().getId(), customerId, accountId, entryId);
    ;
    currencyId = isCurrencyIdNull(currencyId);
    double accountsSum = Assistant.roundResult(totalAccountsSum(accountId, currencyId));

    model.addAttribute("accountsSum", accountsSum);
    model.addAttribute("cash", cashMap);
    model.addAttribute("transactions", transactions);
    model.addAttribute("accountId", accountId);
    model.addAttribute("customerId", customerId);
    model.addAttribute("entryId", entryId);
    model.addAttribute("currencyId", currencyId);
    model.addAttribute("login", dbUser().getLogin());
    model.addAttribute("roles", dbUser().getRole().toString());
    model.addAttribute("accounts", accounts);
    model.addAttribute("customers", customers);
    model.addAttribute("entries", entries);
    model.addAttribute("currencies", currencies);
    model.addAttribute("defaultCustomer", dbUser().getDefaultCustomer());
    model.addAttribute("defaultAccount", dbUser().getDefaultAccount());

    return "index";
  }

  @RequestMapping(value = "/addTransaction", method = RequestMethod.POST)
  public String add(@RequestParam Long accountId,
                    @RequestParam Long customerId,
                    @RequestParam Long entryId,
                    @RequestParam Long currencyId,
                    @RequestParam(required = false) Double sumIn,
                    @RequestParam(required = false) Double sumOut,
                    @RequestParam(required = false) String comment) {
    Account account = accountService.getAccountById(accountId);
    Customer customer = customerService.getCustomerById(customerId);
    Entry entry = entryService.getEntryById(entryId);
    Currency currency = currencyService.getCurrencyById(currencyId);
    Exchange exchangeAccount = exchangeService.getLastDateExchange(account.getCurrency().getId());
    Exchange exchange = exchangeService.getLastDateExchange(currencyId);

    if (Assistant.isUserAccount(dbUser(), account)
      && Assistant.isUserCustomer(dbUser(), customer)
      && Assistant.isUserEntry(dbUser(), entry)
      && Assistant.isUserCurrency(dbUser(), currency)) {
      sumIn = Assistant.ifDoubleNull(sumIn);
      sumOut = Assistant.ifDoubleNull(sumOut);

      Transaction transaction = new Transaction(sumIn, sumOut, new Date(), comment, Assistant.transactionType(sumIn, sumOut));

      account = updateAccountSum(account, exchange, exchangeAccount, sumIn, sumOut);
      transaction = addTransaction(transaction, account, exchange, currency, customer, entry);
      transactionService.addTransaction(transaction,account);
    }

    return "redirect:/";

  }

  @RequestMapping(value = "/addTransfer", method = RequestMethod.POST)
  public String add(@RequestParam Long accountOutId,
                    @RequestParam Long accountInId,
                    @RequestParam Long customerId,
                    @RequestParam Long currencyId,
                    @RequestParam(required = false) Double sum) {

    Account accountIn = accountService.getAccountById(accountInId);
    Account accountOut = accountService.getAccountById(accountOutId);
    Customer customer = customerService.getCustomerById(customerId);
    Entry entry = entryService.getEntryByType(EntryType.TRANSFER, dbUser());
    Currency currency = currencyService.getCurrencyById(currencyId);
    Exchange exchange = exchangeService.getLastDateExchange(currencyId);
    Exchange exchangeAccountIn = exchangeService.getLastDateExchange(accountIn.getCurrency().getId());
    Exchange exchangeAccountOut = exchangeService.getLastDateExchange(accountOut.getCurrency().getId());
    if (Assistant.isUserAccount(dbUser(), accountIn)
      && Assistant.isUserAccount(dbUser(), accountOut)
      && Assistant.isUserCustomer(dbUser(), customer)
      && Assistant.isUserEntry(dbUser(), entry)
      && Assistant.isUserCurrency(dbUser(), currency)) {
      sum = Assistant.ifDoubleNull(sum);
      Date date = new Date();

      Transaction transactionIn = new Transaction(0.0, sum, date, "transfer to " + accountIn.getName(), TransactionType.TRANSFER);
      Transaction transactionOut = new Transaction(sum, 0.0, date, "transfer from " + accountOut.getName(), TransactionType.TRANSFER);
      transactionIn.setTransaction(transactionOut);
      transactionOut.setTransaction(transactionIn);

      accountIn = updateAccountSum(accountIn, exchange, exchangeAccountIn, sum, 0.0);
      accountOut = updateAccountSum(accountOut, exchange, exchangeAccountOut, 0.0, sum);

      transactionIn = addTransaction(transactionIn, accountOut, exchange, currency, customer, entry);
      transactionOut = addTransaction(transactionOut, accountIn, exchange, currency, customer, entry);

    }

    return "redirect:/";
  }

  @RequestMapping(value = "/updateTransaction", method = RequestMethod.POST)
  public String update(@RequestParam Long transactionId,
                       @RequestParam Long accountId,
                       @RequestParam Long customerId,
                       @RequestParam Long entryId,
                       @RequestParam Long currencyId,
                       @RequestParam(required = false) Long accountLinkId,
                       @RequestParam(required = false) Double sumIn,
                       @RequestParam(required = false) Double sumOut,
                       @RequestParam(required = false) String comment) {
    sumIn = Assistant.ifDoubleNull(sumIn);
    sumOut = Assistant.ifDoubleNull(sumOut);
    Transaction transaction = transactionService.getTransactionById(transactionId);
    Account account = accountService.getAccountById(accountId);
    Entry entry = entryService.getEntryById(entryId);
    Customer customer = customerService.getCustomerById(customerId);
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserTransaction(dbUser(), transaction)
      && Assistant.isUserAccount(dbUser(), account)
      && Assistant.isUserEntry(dbUser(), entry)
      && Assistant.isUserCustomer(dbUser(), customer)
      && Assistant.isUserCurrency(dbUser(), currency)) {
      Exchange exchange = exchangeService.getLastDateExchange(currencyId);
      Exchange accountExchange = exchangeService.getLastDateExchange(account.getCurrency().getId());
      Account oldAccount = accountService.getAccountById(transaction.getAccount().getId());
      Exchange oldAccountExchange = exchangeService.getLastDateExchange(oldAccount.getCurrency().getId());
      if (transaction.getType() == TransactionType.TRANSFER) {
        Account accountLink = accountService.getAccountById(accountLinkId);
        if (!Assistant.isUserAccount(dbUser(), accountLink)) {
          return "redirect:/";
        }
        Exchange accountLinkExchange = exchangeService.getLastDateExchange(accountLink.getCurrency().getId());
        Transaction transactionLink = transactionService.getTransactionById(transaction.getTransaction().getId());
        Account oldAccountLink = accountService.getAccountById(transactionLink.getAccount().getId());
        Exchange oldAccountLinkExchange = exchangeService.getLastDateExchange(oldAccountLink.getCurrency().getId());
        updateAccountSum(oldAccountLink, transactionLink.getExchange(), oldAccountLinkExchange, transactionLink.getSumOut(), transactionLink.getSumIn());
        updateAccountSum(accountLink, exchange, accountLinkExchange, sumOut, sumIn);
        updateTransaction(transactionLink, sumOut, sumIn, transactionLink.getComment(), accountLink, customer, currency, exchange, transactionLink.getEntry());
      }
      updateAccountSum(oldAccount, transaction.getExchange(), oldAccountExchange, transaction.getSumOut(), transaction.getSumIn());
      updateAccountSum(account, exchange, accountExchange, sumIn, sumOut);
      updateTransaction(transaction, sumIn, sumOut, comment, account, customer, currency, exchange, entry);
    }
    return "redirect:/";
  }

  @RequestMapping(value = "/deleteTransaction", method = RequestMethod.POST)
  public String delete(@RequestParam Long transactionId) {
    Transaction transaction = transactionService.getTransactionById(transactionId);
    if (Assistant.isUserTransaction(dbUser(), transaction)) {
      Account account = accountService.getAccountById(transaction.getAccount().getId());
      Exchange exchangeAccount = exchangeService.getLastDateExchange(account.getCurrency().getId());
      updateAccountSum(account, transaction.getExchange(), exchangeAccount, transaction.getSumOut(), transaction.getSumIn());

      if (transaction.getType() == TransactionType.TRANSFER) {
        Transaction transactionLink = transactionService.getTransactionById(transaction.getTransaction().getId());
        Account accountLink = accountService.getAccountById(transactionLink.getAccount().getId());
        Exchange exchangeAccountLink = exchangeService.getLastDateExchange(accountLink.getCurrency().getId());
        updateAccountSum(accountLink, transactionLink.getExchange(), exchangeAccountLink, transaction.getSumIn(), transaction.getSumOut());
        dbUser().getTransactions().remove(transactionLink);
      }
      dbUser().getTransactions().remove(transaction);
      transactionService.deleteTransaction(transaction);
    }
    return "redirect:/";
  }

  private UserOwner dbUser() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userService.getUserByLogin(user.getUsername());
  }

  private Account updateAccountSum(Account account, Exchange exchange, Exchange exchangeAccount, Double plus, Double minus) {
    account.setSum(Assistant.roundResult(account.getSum()
      + exchange.getRate()
      / exchangeAccount.getRate()
      * (plus - minus)));
    return account;
  }

  private Transaction addTransaction(Transaction transaction,
                                     Account account,
                                     Exchange exchange,
                                     Currency currency,
                                     Customer customer,
                                     Entry entry) {
    account.addTransaction(transaction);
    currency.addTransaction(transaction);
    exchange.addTransaction(transaction);
    customer.addTransaction(transaction);
    entry.addTransaction(transaction);
    dbUser().addTransaction(transaction);
    return transaction;
  }

  private void updateTransaction(Transaction transaction,
                                 Double plus,
                                 Double minus,
                                 String comment,
                                 Account account,
                                 Customer customer,
                                 Currency currency,
                                 Exchange exchange,
                                 Entry entry) {
    transaction.setSumIn(plus);
    transaction.setSumOut(minus);
    transaction.setComment(comment);
    transaction.setAccount(account);
    transaction.setCustomer(customer);
    transaction.setCurrency(currency);
    transaction.setExchange(exchange);
    transaction.setEntry(entry);
    transactionService.updateTransaction(transaction);
  }

  private double totalAccountsSum(Long accountId, Long currencyId) {
    double sum = 0.0;
    Currency currency = currencyService.getCurrencyById(currencyId);
    if (Assistant.isUserCurrency(dbUser(), currency)) {
      Exchange exchange = exchangeService.getLastDateExchange(currencyId);
      if (accountId != null) {
        Account account = accountService.getAccountById(accountId);
        if (Assistant.isUserAccount(dbUser(), account)) {
          Exchange exchangeAccount = exchangeService.getLastDateExchange(account.getCurrency().getId());
          sum = account.getSum() * exchangeAccount.getRate() / exchange.getRate();
          return Assistant.roundResult(sum);
        }
      }
      List<Account> accounts = accountService.getAllUserAccounts(dbUser().getId());
      Exchange exchangeAccount;
      for (Account account : accounts) {
        exchangeAccount = exchangeService.getLastDateExchange(account.getCurrency().getId());
        sum += account.getSum() * exchangeAccount.getRate() / exchange.getRate();
      }
    }
    return Assistant.roundResult(sum);
  }

  private long isCurrencyIdNull(Long currencyId) {
    if (currencyId == null) {
      return dbUser().getDefaultAccount().getCurrency().getId();
    }
    return currencyId;
  }
}
