package bitmap;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-22 16:34
 * @since 1.0.0
 */
public abstract class AbstractBitMap {

    int ZERO = 0;

    int ONE = 1;

    /**
     * n >> 5  == n / 32
     * <p>
     * 一个整形数正好占4个字节，32个bit位
     */
    public static final int FIVE = 5;

    /**
     * 设置bit
     *
     * @param bit   第bit个位置(从0开始)
     * @param value 设置第bit个位置上的值
     * @return 设置的值。如果设置成功返回1，设置失败返回0。从0设置为0，从1设置为1，返回0.从1设置为0，从0设置为1，返回1
     */
    public int setBit(int bit, int value) {
        boolean flag = (value == ONE);
        return this.setBit(bit, flag);
    }

    /**
     * 设置bit的具体实现
     *
     * @param bit  第bit位
     * @param flag 第bit位上要设置的值是否为1
     * @return {@see #setBit}
     */
    public abstract int setBit(int bit, boolean flag);

    /**
     * 获取第bit个位置上的值
     *
     * @param bit 第bit个位
     * @return 获取第bit个位置上的值
     */
    public abstract int getBit(int bit);

    /**
     * 获取值
     *
     * @return 值
     */
    public abstract String get();

    /**
     * 从offset上获取值
     *
     * @param offset 便宜
     * @return 值
     */
    public abstract String get(int offset);

    /**
     * 取或运算
     *
     * @param bitMap bitmap
     * @return or运算之后的值
     */
    public abstract String or(AbstractBitMap bitMap);

    /**
     * 取与预算
     *
     * @param bitMap bitmap
     * @return and运算之后的值
     */
    public abstract String and(AbstractBitMap bitMap);

    /**
     * 亦或运算
     *
     * @param bitMap bitmap
     * @return 亦或运算之后的值
     */
    public abstract String xor(AbstractBitMap bitMap);

    /**
     * 计算 从offset到end之间有多少位1
     *
     * @param offset 起始位置
     * @param end    终止位置
     * @return 位上是1的个数
     */
    public abstract String bitCount(int offset, int end);

    /**
     * 指定的位置上增长step
     *
     * @param offset 偏移量
     * @param type   增长限度
     * @param step   涨幅
     * @return 增长以后的值(十进制)
     */
    public abstract int incrby(int offset, IncrbyType type, int step, OverFlow overFlow);

}
