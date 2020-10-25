package bitmap.test;

import bitmap.BitMap;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-22 17:55
 * @since 1.0.0
 */
public class TestDemo {

    public static void main(String[] args) {
        BitMap bitMap = new BitMap(128);

        bitMap.setBit(1, 1);
        bitMap.setBit(3, 1);
        bitMap.setBit(5, 1);
        bitMap.setBit(7, 1);
        bitMap.setBit(9, 1);
        bitMap.setBit(33, 1);
        bitMap.setBit(50, 1);
        bitMap.setBit(127, 1);

        System.out.println();
        for (int i = 0; i < 128; i++) {
            System.out.printf("%d ", bitMap.getBit(i));
        }

        System.out.println("\n--------------------------");
        System.out.println(bitMap.get());
        System.out.println(bitMap.get(1));

        System.out.println();
        bitMap.setBit(5, 0);
        bitMap.setBit(7, 0);
        bitMap.setBit(9, 0);

        System.out.println();
        for (int i = 0; i < 32; i++) {
            System.out.printf("%d\t", bitMap.getBit(i));
        }
        System.out.println();


    }

}
