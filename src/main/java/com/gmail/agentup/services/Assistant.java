package com.gmail.agentup.services;

import com.gmail.agentup.model.*;

import java.util.List;
import java.util.Map;

public class Assistant {

  public static double roundResult(double d) {
    d = d * 100;
    d = (double) Math.round(d);
    return d / 100;
  }

  public static double ifDoubleNull(Double d) {
    if (d == null)
      d = 0.0;
    return d;
  }

  static public TransactionType transactionType(double sumIn, double sumOut) {
    if (sumIn - sumOut > 0) {
      return TransactionType.PROFIT;
    } else if (sumIn - sumOut < 0) {
      return TransactionType.LOSS;
    }
    return TransactionType.ZERO;
  }

  public static Map<String, Double> currencyCash(Map<String, Double> cashMap, List<Currency> currencies) {
    for (Currency currency : currencies) {
      double cash = 0;
      for (Account account : currency.getAccounts()) {
        cash += account.getSum();
      }
      cashMap.put(currency.getName(), cash);
    }
    return cashMap;
  }

  public static boolean isUserTransaction(UserOwner userOwner, Transaction transaction) {
    for (Transaction userTransaction : userOwner.getTransactions()) {
      if (userTransaction == transaction) {
        return true;
      }
    }
    return false;
  }

  public static boolean isUserAccount(UserOwner userOwner, Account account) {
    for (Account userAccount : userOwner.getAccounts()) {
      if (userAccount == account) {
        return true;
      }
    }
    return false;
  }

  public static boolean isUserCurrency(UserOwner userOwner, Currency currency) {
    for (Currency userCurrency : userOwner.getCurrencies()) {
      if (userCurrency == currency) {
        return true;
      }
    }
    return false;
  }

  public static boolean isUserCustomer(UserOwner userOwner, Customer customer) {
    for (Customer userCustomer : userOwner.getCustomers()) {
      if (userCustomer == customer) {
        return true;
      }
    }
    return false;
  }

  public static boolean isUserEntry(UserOwner userOwner, Entry entry) {
    for (Entry userEntry : userOwner.getEntries()) {
      if (userEntry == entry) {
        return true;
      }
    }
    return false;
  }

  public static boolean isNoLinksAccount(Account account) {
    for (Transaction transaction : account.getTransactions()) {
      return false;
    }
    return true;
  }

  public static boolean isNoLinksCurrency(Currency currency) {
    for (Transaction transaction : currency.getTransactions()) {
      return false;
    }
    for (Account account : currency.getAccounts()) {
      return false;
    }
    return true;
  }

  public static boolean isNoLinksCustomer(Customer customer) {
    for (Transaction transaction : customer.getTransactions()) {
      return false;
    }
    return true;
  }

  public static boolean isNoLinksEntry(Entry entry) {
    if (entry.getType() == EntryType.TRANSFER) {
      return false;
    }
    for (Transaction transaction : entry.getTransactions()) {
      return false;
    }
    return true;
  }

  public static boolean isNoLinksExchange(Exchange exchange) {
    for (Transaction transaction : exchange.getTransactions()) {
      return false;
    }
    int count = 0;
    for (Exchange currencyExchange : exchange.getCurrency().getExchanges()) {
      count += 1;
      if (count > 1) {
        return true;
      }
    }
    return false;
  }

}
