package hash;

import hash.node.ClientNode;
import hash.node.Node;
import hash.node.ServerNode;

import java.util.List;

/**
 * 一致性hash顶级接口
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 10:49
 * @since 1.0.0
 */
public interface ConsistentHash {

    /**
     * 新增节点
     */
    void add(ServerNode serverNode);

    /**
     * 对节点进行排序
     */
    default void sort() {
    }

    /**
     * 获取相应的节点
     *
     * @param clientNode 根据 客户端节点的Hash值获取到目标服务端节点
     * @return 服务端节点
     */
    ServerNode getFirstNode(ClientNode clientNode);

    /**
     * 传入节点的列表，根据客户端信息，获取第一个服务节点
     *
     * @param clientNode 客户端节点
     * @return 目标服务端节点
     */
    default ServerNode process(ClientNode clientNode) {
        sort();
        return getFirstNode(clientNode);
    }

    List<ServerNode> getAllServerNodes();

    /**
     * 计算hash
     *
     * @param node 节点的信息
     * @return hash值
     */
    default Integer hash(Node node) {
        return defaultStringHash(node.getIdentifier());
    }

    default Integer defaultStringHash(String value) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < value.length(); i++)
            hash = (hash ^ value.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    /**
     * 删除对应节点
     *
     * @param node 节点
     */
    void delete(Node node);

}
