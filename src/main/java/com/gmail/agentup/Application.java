package com.gmail.agentup;

import com.gmail.agentup.model.*;
import com.gmail.agentup.services.AccountService;
import com.gmail.agentup.services.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(final UserService userService, final AccountService accountService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
              if (!userService.existsByLogin("admin")) {

                UserOwner admin = new UserOwner("admin", "6a326fef6ff200f6f2b48d9d5ccea18d95ca0285", UserRole.SUPER);

                Exchange exchange = new Exchange(1, new Date());
                Currency currency = new Currency("UAH", "hrivna");
                Account account = new Account("Main", 1000);
                Customer customer = new Customer("Keeper");
                Entry entry = new Entry("Market", EntryType.GENERAL);
                Entry transfer = new Entry("Transfer", EntryType.TRANSFER);

                Transaction transaction = new Transaction(100, 0, new Date(), "First transaction", TransactionType.PROFIT);
                exchange.addTransaction(transaction);
                currency.addTransaction(transaction);
                account.addTransaction(transaction);
                customer.addTransaction(transaction);
                entry.addTransaction(transaction);

                currency.addExchange(exchange);
                currency.addAccount(account);
                admin.setDefaultCustomer(customer);
                admin.setDefaultAccount(account);
                admin.addCurrency(currency);
                admin.addAccount(account);//Избыточная связь?
                admin.addCustomer(customer);
                admin.addEntry(entry);
                admin.addEntry(transfer);
                admin.addTransaction(transaction);

                userService.addUser(admin);
              }
            }
        };
    }
}
