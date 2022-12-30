package org.junit.tools.generator.utils.testmodel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DummyClassWithDates {

    private Date date;
    private LocalDate localDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime localDateTime;

    public DummyClassWithDates() {
	// default constructor
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public LocalDate getLocalDate() {
	return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
	this.localDate = localDate;
    }

    public LocalDateTime getLocalDateTime() {
	return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
	this.localDateTime = localDateTime;
    }

}
