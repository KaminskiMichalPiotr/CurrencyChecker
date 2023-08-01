package com.example.currencyvalue.repositories;

import com.example.currencyvalue.entities.CurrencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRequestRepository extends JpaRepository<CurrencyRequest, Long> {


}
