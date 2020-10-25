package hash.impl;

import hash.ConsistentHash;
import hash.algorithm.HashHandle;
import hash.node.ClientNode;
import hash.node.Node;
import hash.node.ServerNode;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * SortedMap实现的一致性哈希算法
 * <p>
 * 这种算法在节点数 大于 10000的场景下，性能优秀。
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-04 12:49
 * @since 1.0.0
 */
public class SortedMapConsistentHash implements ConsistentHash {

    private final SortedMap<Integer, ServerNode> hashCircle = new TreeMap<>();

    private final HashHandle<Node> hashHandle;

    /**
     * 虚拟节点的数目,默认为1
     */
    private final Integer virtualNumber;

    public SortedMapConsistentHash() {
        virtualNumber = 1;
        hashHandle = this::hash;
    }

    public SortedMapConsistentHash(int virtualNumber, HashHandle<Node> hashHandle) {
        this.virtualNumber = 1;
        this.hashHandle = hashHandle;
    }

    public SortedMapConsistentHash(int virtualNumber) {
        this.virtualNumber = virtualNumber;
        this.hashHandle = this::hash;
    }

    @Override
    public void add(ServerNode serverNode) {
        // 实现虚拟节点
        for (int i = 0; i < virtualNumber; i++) {
            serverNode.setVirtualNodeId(i);
            hashCircle.put(this.hashHandle.hash(serverNode), serverNode);
        }
    }

    @Override
    public Integer hash(Node node) {
        if (node instanceof ServerNode) {
            return defaultStringHash(node.getIdentifier() + "#" + ((ServerNode) node).getVirtualNodeId());
        } else {
            return defaultStringHash(node.getIdentifier());
        }
    }

    @Override
    public void delete(Node node) {
        this.hashCircle.remove(hashHandle.hash(node));
    }

    @Override
    public ServerNode getFirstNode(ClientNode clientNode) {
        int hash = hash(clientNode);
        // 获取大于客户端哈希值的子序列
        SortedMap<Integer, ServerNode> subMap = hashCircle.tailMap(hash);

        if (subMap.isEmpty()) {
            // 子序列为空，获取哈希环的第一个节点
            Integer key = hashCircle.firstKey();
            return hashCircle.get(key);
        } else {
            // 子序列不为空，获取子序列的第一个节点
            Integer key = subMap.firstKey();
            return subMap.get(key);
        }
    }

    @Override
    public List<ServerNode> getAllServerNodes() {
        return new ArrayList<>(this.hashCircle.values());
    }

    @Override
    public ServerNode process(ClientNode clientNode) {
        return getFirstNode(clientNode);
    }
}
