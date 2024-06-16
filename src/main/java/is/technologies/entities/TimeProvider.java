package is.technologies.entities;

import is.technologies.exceptions.AccountException;

import java.time.LocalDateTime;

public class TimeProvider {
    private LocalDateTime cbTime;
    private static TimeProvider instance;

    public LocalDateTime getCbTime() {
        return cbTime;
    }

    private TimeProvider() {
        cbTime = LocalDateTime.now();
    }

    public static TimeProvider getInstance() {
        if (instance == null) {
            instance = new TimeProvider();
        }
        return instance;
    }

    public void fastForwardDay() throws AccountException {
        cbTime = cbTime.plusDays(1);
        CentralBank.getInstance().update();
    }

    public void fastForwardMonth() throws AccountException {
        int daysInCurrentMonth = cbTime.toLocalDate().lengthOfMonth();
        for(int i = 0; i < daysInCurrentMonth; ++i)
            fastForwardDay();
    }

    public void fastForwardYear() throws AccountException {
        for(int i = 0; i < 12; ++i)
            fastForwardMonth();
    }
}
