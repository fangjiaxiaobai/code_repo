package hash.node.bucket;

import hash.algorithm.HashHandle;
import hash.node.Node;
import hash.node.ServerNode;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 14:48
 * @since 1.0.0
 */
public class DefaultNodeBucket implements NodeBucket {

    ServerNode serverNode;

    Integer hashCode;

    public DefaultNodeBucket(ServerNode serverNode) {
        this.serverNode = serverNode;
    }

    public DefaultNodeBucket(ServerNode serverNode, Integer hashCode) {
        this.serverNode = serverNode;
        this.hashCode = hashCode;
    }

    @Override
    public ServerNode getNode() {
        return this.serverNode;
    }

    @Override
    public Integer getHash() {
        return hashCode;
    }

    public static DefaultNodeBucket of(ServerNode serverNode, HashHandle<Node> hashHandle) {
        return new DefaultNodeBucket(serverNode, hashHandle.hash(serverNode));
    }


    @Override
    public String toString() {
        return "DefaultNodeBucket{" +
                "serverNode=" + serverNode +
                ", hashCode=" + hashCode +
                "}\n";
    }
}
