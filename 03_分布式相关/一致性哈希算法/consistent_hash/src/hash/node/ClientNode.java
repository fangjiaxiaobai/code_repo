package hash.node;

/**
 * 客户端节点
 *
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 15:17
 * @since 1.0.0
 */
public class ClientNode implements Node {

    public ClientNode(String identifier) {
        this.identifier = identifier;
    }

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
