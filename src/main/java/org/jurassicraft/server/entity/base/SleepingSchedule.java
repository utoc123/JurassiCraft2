package org.jurassicraft.server.entity.base;

public enum SleepingSchedule {
    DIURNAL(fromTimeToTicks(6, 0), fromTimeToTicks(22, 0)),
    NOCTURNAL(fromTimeToTicks(18, 0), fromTimeToTicks(6, 0)),
    CREPUSCULAR(fromTimeToTicks(12, 30), fromTimeToTicks(4, 30));

    private int wakeUpTime;
    private int sleepTime;

    SleepingSchedule(int wakeUpTime, int sleepTime) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
    }

    public static int fromTimeToTicks(int hour, int minute) {
        int ticksPerMinute = 1000 / 60;
        return ((hour - 6) * 1000) + (minute * ticksPerMinute);
    }

    public int getWakeUpTime() {
        return this.wakeUpTime;
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public boolean sleepNextDay() {
        return this.wakeUpTime > this.sleepTime;
    }

    public int getAwakeTime() {
        int newSleepTime = this.sleepTime;

        if (this.sleepNextDay()) {
            newSleepTime += 24000;
        }

        return newSleepTime - this.wakeUpTime;
    }
}
