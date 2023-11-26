package com.tuberose.clock.business.util;

import com.tuberose.clock.business.entity.Carriage;
import com.tuberose.clock.business.entity.Seat;
import com.tuberose.clock.business.enums.CarriageTypeEnum;
import com.tuberose.clock.common.util.Snowflake;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SeatGenerator {

    private static final Integer firstClassCarriageRow = 13;
    private static final Integer firstClassCarriageCol = 4;
    private static final List<String> firstClassSeatNumbers = List.of("A", "C", "D", "F");
    private static final Integer secondClassCarriageRow = 20;
    private static final Integer secondClassCarriageCol = 5;
    private static final List<String> secondClassSeatNumbers = List.of("A", "B", "C", "D", "F");


    static public List<Seat> create(Carriage carriage) {
        int rowCount = -1, colCount = -1;
        List<String> seatNumbers = null;
        if (CarriageTypeEnum.FIRST_CLASS_CARRIAGE.getCode().equals(carriage.getType())) {
            rowCount = firstClassCarriageRow;
            colCount = firstClassCarriageCol;
            seatNumbers = firstClassSeatNumbers;
        } else if (CarriageTypeEnum.SECOND_CLASS_CARRIAGE.getCode().equals(carriage.getType())) {
            rowCount = secondClassCarriageRow;
            colCount = secondClassCarriageCol;
            seatNumbers = secondClassSeatNumbers;
        } else {
            log.error("not implement carriage type");
        }

        List<Seat> seats = new ArrayList<>(rowCount * colCount);
        for (int row = 1; row <= rowCount; row++) {
            for (int col = 1; col <= colCount; col++) {
                Seat seat = new Seat(Snowflake.nextId(), carriage.getTrainCode(), carriage.getIndex(),
                        carriage.getType(), String.valueOf(row), seatNumbers.get(col - 1),
                        (row - 1) * colCount + col);
                seats.add(seat);
            }
        }

        return seats;
    }
}
