package com.qre.tg.query.api.service.impl;

import com.qre.tg.query.api.service.TripFareService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

@Service
@Transactional
public class TripFareServiceFactoryImpl {

    private final HolidayTripFareServiceImpl holidayTripFareService;

    private final NormalDayTripFareServiceImpl normalDayTripFareService;

    private HashSet<Date> holidays;

    public TripFareServiceFactoryImpl(HolidayTripFareServiceImpl holidayTripFareService,
                                      NormalDayTripFareServiceImpl normalDayTripFareService) {
        this.holidayTripFareService = holidayTripFareService;
        this.normalDayTripFareService = normalDayTripFareService;

        holidays = new HashSet<>();
        // Add Singapore holidays for the year 2024
        holidays.add(createDate(2024, Calendar.JANUARY, 1)); // New Year's Day
        holidays.add(createDate(2024, Calendar.JANUARY, 24)); // Chinese New Year
        holidays.add(createDate(2024, Calendar.JANUARY, 25)); // Chinese New Year (2nd day)
        holidays.add(createDate(2024, Calendar.APRIL, 5)); // Good Friday
        holidays.add(createDate(2024, Calendar.MAY, 1)); // Labour Day
        holidays.add(createDate(2024, Calendar.JUNE, 12)); // Hari Raya Haji
        holidays.add(createDate(2024, Calendar.AUGUST, 9)); // National Day
        holidays.add(createDate(2024, Calendar.OCTOBER, 23)); // Deepavali
        holidays.add(createDate(2024, Calendar.DECEMBER, 25)); // Christmas Day
        // You can add more holidays as needed
    }

    public TripFareService createTripFare(boolean isHoliday) {
        if (isHoliday) {
            return holidayTripFareService;
        } else {
            return normalDayTripFareService;
        }
    }

    public boolean isHoliday(Date dateToCheck) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCheck);
        int year = cal.get(Calendar.YEAR);
        // Create a date without time (set time to midnight)
        Date dateWithoutTime = createDate(year, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return holidays.contains(dateWithoutTime);
    }

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
