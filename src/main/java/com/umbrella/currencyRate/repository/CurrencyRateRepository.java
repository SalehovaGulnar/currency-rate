package com.umbrella.currencyRate.repository;

import com.umbrella.currencyRate.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {

    List<CurrencyRate> findAllByRateDate(Date date);

    void deleteByRateDate(Date date);

    @Query("SELECT c FROM CurrencyRate c WHERE (:date is null or c.rateDate = :date)" +
            "and (:code is null or c.currencyCode = :code)")
    List<CurrencyRate> findCurrencyRate(@Param("date") Date date, @Param("code") String code);
}
