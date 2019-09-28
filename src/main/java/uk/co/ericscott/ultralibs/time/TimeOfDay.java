package uk.co.ericscott.ultralibs.time;

public enum TimeOfDay {

    DUSK(23500),
    MIDDAY(6000),
    NIGHT(18000);

    int ticks;

    TimeOfDay(int ticks){
        this.ticks = ticks;
    }

    public int getTicks()
    {
        return ticks;
    }
}