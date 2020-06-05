package hash.impl;

import hash.ConsistentHash;
import hash.algorithm.HashHandle;
import hash.node.ClientNode;
import hash.node.Node;
import hash.node.ServerNode;
import hash.node.bucket.DefaultNodeBucket;
import hash.node.bucket.NodeBucket;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 遍历的方式来实现
 * <p>
 * 在服务端节点的数据量 小于 10000的情况下，性能优秀。
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-05 11:18
 * @since 1.0.0
 */
public class TraverseArrayConsistentHash implements ConsistentHash {

    /**
     * 使用数组实现的哈希环
     */
    private NodeBucket[] hashCircle = new NodeBucket[]{};

    private final HashHandle<Node> hashHandle;

    private static final int defaultSize = 10;

    private static final int MaxSize = Integer.MAX_VALUE;

    private int length = 0;

    private int size = 0;


    /**
     * 虚拟节点的数目,默认为1
     */
    private final Integer virtualNumber;

    public TraverseArrayConsistentHash(Integer initialCapacity) {
        if (initialCapacity > 0) {
            this.size = initialCapacity;
            this.hashCircle = new NodeBucket[initialCapacity];
        } else {
            throw new IllegalArgumentException("initialCapacity must > 0 ");
        }

        this.hashHandle = this::hash;
        virtualNumber = 1;
    }

    public TraverseArrayConsistentHash(Integer initialCapacity, Integer virtualNumber) {
        if (initialCapacity > 0) {
            this.size = initialCapacity;
            this.hashCircle = new NodeBucket[initialCapacity];
        } else {
            throw new IllegalArgumentException("initialCapacity must > 0 ");
        }

        this.hashHandle = this::hash;
        this.virtualNumber = virtualNumber;
    }

    public TraverseArrayConsistentHash(Integer initialCapacity, Integer virtualNumber, HashHandle<Node> hashHandle) {
        if (initialCapacity > 0) {
            this.size = initialCapacity;
            this.hashCircle = new NodeBucket[initialCapacity];
        } else {
            throw new IllegalArgumentException("initialCapacity must > 0 ");
        }

        this.virtualNumber = virtualNumber;
        this.hashHandle = hashHandle;
    }

    public TraverseArrayConsistentHash() {
        this.hashHandle = this::hash;
        this.virtualNumber = 1;
    }

    @Override
    public void add(ServerNode serverNode) {
        // 检查大小是否需要扩容
        checkSize();
        for (int i = 0; i < virtualNumber; i++) {
            serverNode.setVirtualNodeId(i);
            this.hashCircle[length++] = DefaultNodeBucket.of(serverNode, hashHandle);
        }
    }


    /**
     * 检查数组大小
     */
    private void checkSize() {
        if (size == 0) {
            this.size = defaultSize;
            this.hashCircle = new NodeBucket[size];
        }
        if (size >= MaxSize) {
            throw new IllegalArgumentException();
        }
        if (length + 1 > size) {
            this.size = size << 1;
            this.hashCircle = Arrays.copyOf(this.hashCircle, size, NodeBucket[].class);
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
        for (int index = 0; index < size; index++) {
            if (node.equals(hashCircle[index].getNode())) {
                fastRemove(index);
                break;
            }
        }
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(hashCircle, index+1, hashCircle, index,
                    numMoved);
        hashCircle[--size] = null;
    }


    @Override
    public ServerNode getFirstNode(ClientNode clientNode) {
        Integer hash = this.hashHandle.hash(clientNode);
        ServerNode findNode = null;
        int min = Integer.MAX_VALUE;
        // 查找符合条件的服务端节点
        for (int i = 0; i < length; i++) {
            NodeBucket nodeBucket = this.hashCircle[i];
            int difference;
            if ((difference = Math.abs(nodeBucket.getHash() - hash)) < min) {
                min = difference;
                findNode = nodeBucket.getNode();
            }
        }
        return findNode;
    }

    @Override
    public List<ServerNode> getAllServerNodes() {
        return Stream.of(this.hashCircle).map(NodeBucket::getNode).collect(Collectors.toList());
    }

}