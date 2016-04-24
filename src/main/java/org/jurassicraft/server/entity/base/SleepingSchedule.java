package org.jurassicraft.server.entity.base;

public enum SleepingSchedule
{
    DIURNAL(fromTimeToTicks(6, 00), fromTimeToTicks(22, 00)),
    NOCTURNAL(fromTimeToTicks(18, 00), fromTimeToTicks(6, 00)),
    CREPUSCULAR(fromTimeToTicks(12, 30), fromTimeToTicks(4, 30));

    private int wakeUpTime;
    private int sleepTime;

    SleepingSchedule(int wakeUpTime, int sleepTime)
    {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
    }

    public static int fromTimeToTicks(int hour, int minute)
    {
        int ticksPerMinute = 1000 / 60;
        return ((hour - 6) * 1000) + (minute * ticksPerMinute);
    }

    public int getWakeUpTime()
    {
        return wakeUpTime;
    }

    public int getSleepTime()
    {
        return sleepTime;
    }

    public boolean sleepNextDay()
    {
        return wakeUpTime > sleepTime;
    }

    public int getAwakeTime()
    {
        int newSleepTime = sleepTime;

        if (sleepNextDay())
        {
            newSleepTime += 24000;
        }

        return newSleepTime - wakeUpTime;
    }
}
