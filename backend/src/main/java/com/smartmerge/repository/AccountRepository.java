package com.smartmerge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartmerge.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
