package bitmap.test;

import bitmap.AbstractBitMap;
import bitmap.BitMap;
import bitmap.IncrbyType;
import bitmap.OverFlow;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-23 14:14
 * @since 1.0.0
 */
public class TestDemoIncrby {

    public static void main(String[] args) {
        /// 测试不跨字节
//        satTest1();
        // 测试跨字节
        satTest2();
    }

    /**
     * 跨字节计算
     */
    private static void satTest2() {
        int length = 64;
        AbstractBitMap bitMap = new BitMap(length);
//        bitMap.setBit(31,1);
//        bitMap.setBit(34,1);
        for (int i = 0; i < 20; i++) {
            if(i==8){
                System.out.println("-------------------------------");
            }
            int incrby = bitMap.incrby(31, IncrbyType.i4, 4, OverFlow.SAT);
            System.out.println(incrby);
            print(bitMap, length);
        }

    }


    private static void satTest1() {
        int length = 32;
        AbstractBitMap bitMap = new BitMap(length);
//        bitMap.setBit(1, 1);

        for (int i = 0; i < 10; i++) {
            int incrby = bitMap.incrby(3, IncrbyType.i4, 2, OverFlow.SAT);
            System.out.println(incrby);
            print(bitMap, length);
        }

    }


    /**
     * 打印所有位
     *
     * @param bitMap bitmap
     * @param count  bitmap的长度
     */
    private static void print(AbstractBitMap bitMap, int count) {
        for (int i = 0; i < count; i++) {
            System.out.printf("%d ", bitMap.getBit(i));
        }
        System.out.println();
    }


}
