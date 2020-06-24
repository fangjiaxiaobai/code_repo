package bitmap;


/**
 * java语言实现Bitmap
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-22 15:46
 * @since 1.0.0
 */
public class BitMap extends AbstractBitMap {

    private final int[] bits;

    private final static long MAX_SIZE = 1024 * 1024 * 512 * 3;

    private final static long default_size = 1024 * 1024 * 8;

    private volatile int length;

    private static final int[] BIT_VALUE = {
            0x80000000, 0x40000000, 0x20000000, 0x10000000,
            0x08000000, 0x04000000, 0x02000000, 0x01000000,
            0x00800000, 0x00400000, 0x00200000, 0x00100000,
            0x00080000, 0x00040000, 0x00020000, 0x00010000,
            0x00008000, 0x00004000, 0x00002000, 0x00001000,
            0x00000800, 0x00000400, 0x00000200, 0x00000100,
            0x00000080, 0x00000040, 0x00000020, 0x00000010,
            0x00000008, 0x00000004, 0x00000002, 0x00000001
    };

    public BitMap(int length) {
        if (length > MAX_SIZE) {
            throw new IllegalArgumentException("the parameter 'length' is to large");
        }
        this.length = length;
        // length >> 5
        // 相当于 length/32, 如果length%32==0，那么整体数组长度就是length/32.
        // 否则：就是length/32+1
        int currentLength = (length >> FIVE) + ((length & 31) > 0 ? 1 : 0);
        this.bits = new int[currentLength];
    }


    @Override
    public int setBit(int bit, boolean flag) {
        int index = bit >> FIVE;
        int offset = bit & 31;
        if (flag) {
            this.bits[index] = this.bits[index] | BIT_VALUE[offset];
        } else {
            this.bits[index] = this.bits[index] & ~BIT_VALUE[offset];
        }
        return 0;
    }

    @Override
    public int getBit(int bit) {
        int index = bit >> FIVE;
        int offset = bit & 31;
        return (bits[index] & BIT_VALUE[offset]) >> (31 - offset);
    }

    @Override
    public String get() {
        StringBuilder stringBuffer = new StringBuilder("0x");
        for (int bit : bits) {
            String s = Integer.toHexString(bit);
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    @Override
    public String get(int offset) {
        // 如果offset大于bit的长度，直接返回0即可
        if (offset >= bits.length) {
            return "0x00";
        }
        StringBuilder stringBuilder = new StringBuilder("0x");
        for (int i = offset; i < bits.length; i++) {
            stringBuilder.append(Integer.toHexString(bits[i]));
        }
        return stringBuilder.toString();
    }

    @Override
    public String or(AbstractBitMap bitMap) {
        return null;
    }

    @Override
    public String and(AbstractBitMap bitMap) {
        return null;
    }

    @Override
    public String xor(AbstractBitMap bitMap) {
        return null;
    }

    @Override
    public String bitCount(int offset, int end) {
        return null;
    }

    @Override
    public int incrby(int offset, IncrbyType type, int step, OverFlow overFlow) {
        int res = 0;
        switch (overFlow) {
            case SAT:
                res = satIncrby(offset, type, step);
                break;
            case FAIL:
                break;
            default:

        }
        return res;
    }

    /**
     * @param offset bit位值
     * @param type   值的bit位数
     * @param step   增长的大小
     * @return 增长后的值
     */
    private int satIncrby(int offset, IncrbyType type, int step) {
        // 首先要判断是否需要跨字节的。
        int startIndex = offset >> FIVE; // 确定bitmap数组中的位置
        int startOffset = offset & 31;
        // 如果跨字节时候，type指定的结束位的字节位置
        // 如果不夸字节时候，endIndex = startIndex
        int endIndex = (offset + step) >> FIVE;
        int value = type.getValue(); //  u4?u5?u6?
        int intervalMaximum = 1 << value; // 当前区间内最大值。

        int bitOffset = 32 - startOffset - value;
        if (step >= intervalMaximum) {
            this.bits[startIndex] = this.bits[startIndex] | (intervalMaximum - 1) << bitOffset;
            return (intervalMaximum - 1);
        }
        if (startIndex == endIndex) {
            // 不需要跨字节
            int cardinality = (1 << (32 - startOffset)) - 1; // 计算基数
            int i = bits[startIndex] & cardinality; // 计算出当前字节中组成的新数
            int intervalNumber = i >> (32 - startOffset - value);
            int res = intervalNumber + step;
            if (res >= intervalMaximum) {
                this.bits[startIndex] = this.bits[startIndex] | (intervalMaximum - 1) << bitOffset;
                return (intervalMaximum - 1);
            } else {
                this.bits[startIndex] = this.bits[startIndex] + ((step & (intervalMaximum - 1)) << bitOffset);
                return res;
            }
        } else {
            // 夸字节操作
            int cardinality = (1 << (32 - startOffset)) - 1; // 计算基数
            int leftNumber = bits[startIndex] & cardinality; // 计算出前一个字节中组成的新数

            // 计算第二个字节中的数。
            int rightOffset = value - (32 - startOffset); // 表示后一个字节中的位数。
            int aa = 32 - rightOffset; // 后一个字节中，不需要的位数。
            int rightNumber = this.bits[endIndex] & ~(1 << aa - 1);
            rightNumber = rightNumber >>> aa; // 第二个字节中的数。

            int newNumber = (leftNumber << (rightOffset)) + rightNumber; // 指定区间内的数

            int res = newNumber + step; // 递加之后的数
            if (res >= intervalMaximum) {
                // 超出计算范围
                this.bits[startIndex] = this.bits[startIndex] | cardinality;
                this.bits[endIndex] = this.bits[endIndex] | (-(1 << aa));
                return intervalMaximum - 1;
            } else {

                // 计算后一个字节中的数。
                int rightCounter = res & ((1 << rightOffset) - 1);
                /// 使前n位不变，后32-n位全变成1
                rightCounter = (rightCounter << (32 - rightOffset)) | ((1 << (32 - rightOffset)) - 1);
                /// 一个整型数，将其二进制的前n位变为1，后32-n位不变。
                int temp = this.bits[endIndex] | (-(1 << (32 - rightOffset)));
                this.bits[endIndex] = temp & rightCounter;

                int right = (res >> rightOffset);

                /// 第一个字节中，后n位置为0，前32-n位不变。
                int startTemp = this.bits[startIndex] & -(1 << (32 - rightOffset));
                this.bits[startIndex] = startTemp | right;
                return res;
            }

        }
    }

}
