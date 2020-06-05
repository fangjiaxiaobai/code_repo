package hash.node;

/**
 * 服务端节点
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 15:17
 * @since 1.0.0
 */
public class ServerNode implements Node {

    public ServerNode(String ip, String identifier, String name) {
        this.ip = ip;
        this.identifier = identifier;
        this.name = name;
    }

    /**
     * 服务端节点的ip
     */
    private String ip;

    /**
     * 服务端节点的唯一标识：用于计算哈希值
     */
    private String identifier;

    /**
     * 服务端节点的名字
     */
    private String name;

    /**
     * 虚拟节点
     */
    private int virtualNodeId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVirtualNodeId() {
        return virtualNodeId;
    }

    public void setVirtualNodeId(int virtualNodeId) {
        this.virtualNodeId = virtualNodeId;
    }

    @Override
    public String toString() {
        return "ServerNode{" +
                "ip='" + ip + '\'' +
                ", identifier='" + identifier + '\'' +
                ", virtualNodeId='" + virtualNodeId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
