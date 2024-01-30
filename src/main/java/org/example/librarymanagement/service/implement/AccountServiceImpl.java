package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.repository.AccountRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountServiceImpl {

    private final AccountRepository accountRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

}
