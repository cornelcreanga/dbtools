package com.ccreanga;

import java.util.Random;
import java.util.UUID;

public class UUIDGen
{
    private static final long START_EPOCH = -12219292800000L;
    private static final long clockSeqAndNode = makeClockSeqAndNode();

    private UUIDGen()
    {
        // make sure someone didn't whack the clockSeqAndNode by changing the order of instantiation.
        if (clockSeqAndNode == 0) throw new RuntimeException("singleton instantiation is misplaced.");
    }

    public static UUID getTimeUUIDFromMicros(long whenInMicros)
    {
        long whenInMillis = whenInMicros / 1000;
        long nanos = (whenInMicros - (whenInMillis * 1000)) * 10;
        return getTimeUUID(whenInMillis, nanos);
    }


    public static UUID getTimeUUID(long when, long nanos)
    {
        return new UUID(createTime(fromUnixTimestamp(when, nanos)), clockSeqAndNode);
    }


    private static long fromUnixTimestamp(long timestamp, long nanos)
    {
        return ((timestamp - START_EPOCH) * 10000) + nanos;
    }

    private static long makeClockSeqAndNode()
    {
        long clock = new Random(System.currentTimeMillis()).nextLong();

        long lsb = 0;
        lsb |= 0x8000000000000000L;                 // variant (2 bits)
        lsb |= (clock & 0x0000000000003FFFL) << 48; // clock sequence (14 bits)
        return lsb;
    }

    private static long createTime(long nanosSince)
    {
        long msb = 0L;
        msb |= (0x00000000ffffffffL & nanosSince) << 32;
        msb |= (0x0000ffff00000000L & nanosSince) >>> 16;
        msb |= (0xffff000000000000L & nanosSince) >>> 48;
        msb |= 0x0000000000001000L; // sets the version to 1.
        return msb;
    }



}

