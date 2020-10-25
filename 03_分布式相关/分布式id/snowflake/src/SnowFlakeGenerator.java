package com.fxb.algorithm;

/**
 * 雪花算法生成器
 * <p>
 *
 * @author fangjiaxiaobai
 * @date 2020-10-24 23:51
 * @since 1.0
 */
public class SnowFlakeGenerator {

    /**
     * 开始时间戳 (2015-01-01)
     */
    private final static long DEFAULT_TIMESTAMP = 1603556068000L;

    /**
     * 总位数
     */
    private final static long BITS_COUNT = 64L;
    /**
     * 机器id所占的位数,
     * 默认是5位。最多支持31台机器
     */
    private final static long DEFAULT_WORKER_ID_BITS = 5L;

    /**
     * 数据中心id所占的位数
     * 可以理解为机房。默认是5位。
     */
    private final static long DEFAULT_DATA_CENTER_ID_BITS = 5L;

    /**
     * 序列在id中占的位数
     */
    private final static long DEFAULT_SEQUENCE_BITS = 12L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final static long DEFAULT_MAX_WORKER_ID = ~(-1L << DEFAULT_WORKER_ID_BITS);

    /**
     * 支持的数据中心数量
     */
    private final static long DEFAULT_MAX_DATA_CENTER_ID_BITS = ~(-1L << DEFAULT_DATA_CENTER_ID_BITS);

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final static long DEFAULT_SEQUENCE_MASK = ~(-1L << DEFAULT_SEQUENCE_BITS);

    /**
     * 时间戳向左移22位(5+5+12)
     */
    private final static long DEFAULT_TIMESTAMP_LEFT_SHIFT = DEFAULT_SEQUENCE_BITS + DEFAULT_WORKER_ID_BITS + DEFAULT_DATA_CENTER_ID_BITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final static long DEFAULT_DATA_CENTER_ID_SHIFT = DEFAULT_SEQUENCE_BITS + DEFAULT_WORKER_ID_BITS;

    /**
     * workId的位移
     */
    private final static long DEFAULT_WORKER_ID_SHIFT = DEFAULT_SEQUENCE_BITS;

    /**
     * 工作机器ID
     */
    private long workerId;

    /**
     * 数据中心ID
     */
    private long dataCenterId;

    /**
     * 支持的最大数据中心数
     */
    private long maxDataCenterId;

    /**
     * 支持的最大workerid数
     */
    private long maxWorkerId;

    /**
     * 序列号的位数
     */
    private Long sequenceBits;

    /**
     * 序列号的掩码
     * 即，2^sequenceBits
     */
    private Long sequenceMask;

    /**
     * 毫秒内序列
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    private long dataCenterIdShift;


    private long workerIdShift;
    /**
     * 时间戳的位数
     */
    private long timestampLeftShift;

    /**
     * 默认 每个数据中心可以容纳31台机器
     *
     * @param workerId     工作ID
     * @param dataCenterId 数据中心ID
     */
    public SnowFlakeGenerator(long workerId, long dataCenterId) {
        this.maxDataCenterId = DEFAULT_MAX_DATA_CENTER_ID_BITS;
        this.maxWorkerId = DEFAULT_MAX_WORKER_ID;

        if(workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if(dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequenceBits = DEFAULT_SEQUENCE_BITS;

        this.sequenceMask = ~(-1L << DEFAULT_SEQUENCE_MASK);
        this.dataCenterIdShift = DEFAULT_DATA_CENTER_ID_SHIFT;
        this.workerIdShift = DEFAULT_WORKER_ID_SHIFT;
        this.timestampLeftShift = DEFAULT_TIMESTAMP_LEFT_SHIFT;
    }

    /**
     * 根据自己的配置决定容纳的机器数
     *
     * @param workerId         工作机器的id
     * @param dataCenterId     数据中心的id
     * @param workerIdBits     工作机占的位数
     * @param dataCenterIdBits 数据中心占的位数
     */
    public SnowFlakeGenerator(long workerId, long dataCenterId, long workerIdBits, long dataCenterIdBits) {

        long maxBits = BITS_COUNT - 41L - 1;

        if(workerIdBits < 1 || dataCenterIdBits < 1 || workerIdBits > maxBits || dataCenterIdBits > maxBits) {
            throw new IllegalArgumentException(String.format("workerIdBits or dataCenterIdBits can't be greater than %d or less than 0", maxBits));
        }

        this.maxDataCenterId = 2 << dataCenterIdBits;
        this.maxWorkerId = 2 << workerIdBits;

        if(workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if(dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequenceBits = BITS_COUNT - workerIdBits - dataCenterIdBits;

        this.sequenceMask = ~(-1L << sequenceBits);

        this.workerIdShift = sequenceBits;
        this.dataCenterIdShift = sequenceBits + workerIdBits;
        this.timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimeStamp();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if(currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - currentTimestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if(lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if(sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                currentTimestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间戳
        lastTimestamp = currentTimestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((currentTimestamp - DEFAULT_TIMESTAMP) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimeStamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimeStamp();
        }
        return timestamp;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间戳。
     */
    private long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

}
