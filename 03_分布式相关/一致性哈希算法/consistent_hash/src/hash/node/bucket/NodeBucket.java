package hash.node.bucket;

import hash.node.ServerNode;

/**
 * 节点bucket，
 * <p>
 * 存储节点信息和计算节点的哈希值
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 14:48
 * @since 1.0.0
 */
public interface NodeBucket {

    ServerNode getNode();

    Integer getHash();


}
