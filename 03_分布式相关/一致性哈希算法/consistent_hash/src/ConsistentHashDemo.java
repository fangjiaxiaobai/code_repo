import hash.ConsistentHash;
import hash.impl.SortListConsistentHash;
import hash.impl.SortedMapConsistentHash;
import hash.impl.TraverseArrayConsistentHash;
import hash.node.ClientNode;
import hash.node.ServerNode;

import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 10:13
 * @since 1.0.0
 */
public class ConsistentHashDemo {

    public static void main(String[] args) {
        testSortListConsistentHash();
        testSortedMapConsistentHash();
        testTraverseArrayConsistentHash();
    }

    /**
     * 测试，使用遍历的方式来实现一致性hash算法
     */
    private static void testTraverseArrayConsistentHash() {
        ConsistentHash sortedMapConsistentHash = new TraverseArrayConsistentHash();
        testConsistentHashCommonPart(sortedMapConsistentHash);
    }

    /**
     * 测试，使用SortMap实现的一致性哈希算法
     */
    private static void testSortedMapConsistentHash() {
        ConsistentHash sortedMapConsistentHash = new SortedMapConsistentHash(3);
        testConsistentHashCommonPart(sortedMapConsistentHash);

    }

    /**
     * 测试，使用数组和排序方式实现的 一致性哈希算法
     */
    private static void testSortListConsistentHash() {
        SortListConsistentHash sortListConsistentHashMap = new SortListConsistentHash();
        testConsistentHashCommonPart(sortListConsistentHashMap);
    }


    /**
     * 测试 consistentHash 算法，公共代码部分
     *
     * @param consistentHash 一致性哈希实例
     */
    private static void testConsistentHashCommonPart(ConsistentHash consistentHash) {
        Stream.of(
                new ServerNode("192.168.0.1", UUID.randomUUID().toString(), "domain1.com"),
                new ServerNode("192.168.0.2", UUID.randomUUID().toString(), "domain2.com"),
                new ServerNode("192.168.0.3", UUID.randomUUID().toString(), "domain3.com"),
                new ServerNode("192.168.0.4", UUID.randomUUID().toString(), "domain4.com"),
                new ServerNode("192.168.0.5", UUID.randomUUID().toString(), "domain5.com"),
                new ServerNode("192.168.0.6", UUID.randomUUID().toString(), "domain6.com")
        ).forEach(consistentHash::add);
        String s1 = UUID.randomUUID().toString();
        ServerNode serverNode1 = consistentHash.process(new ClientNode(s1));
        out.println(serverNode1);
        ServerNode serverNode1_1 = consistentHash.process(new ClientNode(s1));
        out.println(serverNode1_1);
        ServerNode serverNode1_2 = consistentHash.process(new ClientNode(UUID.randomUUID().toString()));
        out.println(serverNode1_2);
    }
}
