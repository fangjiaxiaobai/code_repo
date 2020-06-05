package hash.impl;

import hash.ConsistentHash;
import hash.algorithm.HashHandle;
import hash.node.ClientNode;
import hash.node.Node;
import hash.node.ServerNode;
import hash.node.bucket.DefaultNodeBucket;
import hash.node.bucket.NodeBucket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 排序+遍历 实现
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 14:30
 * @since 1.0.0
 */
public class SortListConsistentHash implements ConsistentHash {

    /**
     * hash环容器
     */
    private List<NodeBucket> hashCircle = null;

    /**
     * 哈希算法
     * 默认使用 {@link ConsistentHash#hash }算法实现
     */
    private final HashHandle<Node> hashHandle;

    public SortListConsistentHash() {
        this.hashHandle = this::hash;
        virtualNumber = 1;
    }

    public SortListConsistentHash(HashHandle<Node> hashHandle) {
        this.hashHandle = hashHandle;
        virtualNumber = 1;
    }

    public SortListConsistentHash(HashHandle<Node> hashHandle, Integer virtualNumber) {
        this.hashHandle = hashHandle;
        this.virtualNumber = virtualNumber;
    }

    public SortListConsistentHash(Integer virtualNumber) {
        this.hashHandle = this::hash;
        this.virtualNumber = virtualNumber;
    }

    /**
     * 虚拟节点的数目,默认为1
     */
    private final Integer virtualNumber;

    /**
     * 新增服务节点
     *
     * @param value 服务节点
     */
    @Override
    public void add(ServerNode value) {
        if (hashCircle == null) {
            hashCircle = new ArrayList<>();
        }
        for (int i = 0; i < virtualNumber; i++) {
            value.setVirtualNodeId(i);
            hashCircle.add(DefaultNodeBucket.of(value, (node) -> hashHandle.hash(value)));
        }
    }

    /**
     * 排序
     */
    @Override
    public void sort() {
        hashCircle.sort(Comparator.comparingInt(NodeBucket::getHash));
    }

    /**
     * 找不到符合条件的第一个节点
     *
     * @param clientNode 根据 客户端节点的Hash值获取到目标服务端节点
     * @return
     */
    @Override
    public ServerNode getFirstNode(ClientNode clientNode) {

        Integer hash = hash(clientNode);

        Optional<NodeBucket> first = hashCircle.stream().filter(item -> item.getHash() > hash).findFirst();

        return first.map(NodeBucket::getNode).orElse(hashCircle.get(0).getNode());

    }

    /**
     * 获取所有服务节点
     *
     * @return 所有服务端节点
     */
    @Override
    public List<ServerNode> getAllServerNodes() {
        return this.hashCircle.stream().map(NodeBucket::getNode).collect(Collectors.toList());
    }

    /**
     * 哈希算法
     *
     * @param node 节点的信息,可能是客户端，也可能是服务端
     * @return 哈希值
     */
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
        if (node instanceof ServerNode) {
            this.hashCircle.remove(DefaultNodeBucket.of((ServerNode) node, hashHandle));
        }
    }

}