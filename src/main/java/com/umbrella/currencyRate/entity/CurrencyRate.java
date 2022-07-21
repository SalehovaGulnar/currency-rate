package com.umbrella.currencyRate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_rate_seq")
    @SequenceGenerator(name = "currency_rate_seq", sequenceName = "currency_rate_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "GMT+4")
    private Date rateDate;

    private String currencyCode;

    private String currencyName;

    private double rateValue;
}
