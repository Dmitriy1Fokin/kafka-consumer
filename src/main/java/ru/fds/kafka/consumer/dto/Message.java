package ru.fds.kafka.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String text;
    private BigDecimal bigDecimal;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private Double aDouble;
    private Integer integer;
    private TypeEnum typeEnum;
}
