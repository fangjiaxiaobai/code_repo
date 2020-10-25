import hash.ConsistentHash;
import hash.impl.SortListConsistentHash;
import hash.impl.SortedMapConsistentHash;
import hash.impl.TraverseArrayConsistentHash;
import hash.node.ClientNode;
import hash.node.ServerNode;

import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * 测试： 不同实现方案的效率
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 10:13
 * @since 1.0.0
 */
public class PressureConsistentHashDemo {

    private static final int virtualNodeNumber = 10000;

    public static void main(String[] args) {
        String s1 = "0fc4424f-7e4f-4bea-93a3-d34b45668158";
        testSortListConsistentHash(s1);
        testSortedMapConsistentHash(s1);
        testTraverseArrayConsistentHash(s1);
    }

    /**
     * 测试，使用遍历的方式来实现一致性hash算法
     */
    private static void testTraverseArrayConsistentHash(String s1) {
        ConsistentHash sortedMapConsistentHash = new TraverseArrayConsistentHash(virtualNodeNumber * 6, virtualNodeNumber);
        testConsistentHashCommonPart(sortedMapConsistentHash, s1);
    }

    /**
     * 测试，使用SortMap实现的一致性哈希算法
     */
    private static void testSortedMapConsistentHash(String s1) {
        ConsistentHash sortedMapConsistentHash = new SortedMapConsistentHash(virtualNodeNumber);
        testConsistentHashCommonPart(sortedMapConsistentHash, s1);

    }

    /**
     * 测试，使用数组和排序方式实现的 一致性哈希算法
     */
    private static void testSortListConsistentHash(String s1) {
        SortListConsistentHash sortListConsistentHashMap = new SortListConsistentHash(virtualNodeNumber);
        testConsistentHashCommonPart(sortListConsistentHashMap, s1);
    }


    /**
     * 测试 consistentHash 算法，公共代码部分
     *
     * @param consistentHash 一致性哈希实例
     */
    private static void testConsistentHashCommonPart(ConsistentHash consistentHash, String s1) {
        Stream.of(
                new ServerNode("192.168.0.1", "192.168.0.1", "domain1.com"),
                new ServerNode("192.168.0.2", "192.168.0.2", "domain2.com"),
                new ServerNode("192.168.0.3", "192.168.0.3", "domain3.com"),
                new ServerNode("192.168.0.4", "192.168.0.4", "domain4.com"),
                new ServerNode("192.168.0.5", "192.168.0.5", "domain5.com"),
                new ServerNode("192.168.0.6", "192.168.0.6", "domain6.com")
        ).forEach(consistentHash::add);

        long l = System.currentTimeMillis();
        ServerNode serverNode1 = consistentHash.process(new ClientNode(s1));
        out.println("耗时: " + (System.currentTimeMillis() - l));
    }
}
