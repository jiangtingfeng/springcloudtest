package org.test.springcloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsistentHashLoadBalance {
    /*
    * 使用TreeMap实现hash环
    * */
    private TreeMap<Long, String> virtualNodes = new TreeMap<>();
    private LinkedList<String> nodes;
    //每个真实节点对应的虚拟节点数
    private final int replicCnt;

    /*
    * 初始化节点nodes 以及测试需要的虚拟节点数
    * */
    public ConsistentHashLoadBalance(LinkedList<String> nodes, int replicCnt){
        this.nodes = nodes;
        this.replicCnt = replicCnt;
        initalization();
    }

    /**
     * 初始化哈希环
     * 循环计算每个node名称的哈希值，将其放入treeMap
     *
     * 初始化哈希环  循环计算每个节点的哈希值  然后将其放入treeMap中
     */
    private void initalization(){
        for (String nodeName: nodes) {
            for (int i = 0; i < replicCnt/4; i++) {
                /*
                * 获取节点
                * */
                String virtualNodeName = getNodeNameByIndex(nodeName, i);
                for (int j = 0; j < 4; j++) {
                    /*
                    * 通过hash(virtualNodeName(节点名),加尾缀)
                    * map.put(key,value);
                    * */
                    virtualNodes.put(hash(virtualNodeName, j), nodeName);
                }
            }
        }
    }

    private String getNodeNameByIndex(String nodeName, int index){
        /*System.out.println(new StringBuffer(nodeName)
                .append("&&")
                .append(index)
                .toString());*/
        return new StringBuffer(nodeName)
                .append("&&")
                .append(index)
                .toString();
    }

    /**
     * 根据资源key选择返回相应的节点名称
     * @param key
     * @return 节点名称
     */
    public String selectNode(String key){
        Long hashOfKey = hash(key, 0);
        if (! virtualNodes.containsKey(hashOfKey)) {
            Map.Entry<Long, String> entry = virtualNodes.ceilingEntry(hashOfKey);
            if (entry != null)
                return entry.getValue();
            else
                return nodes.getFirst();
        }else
            return virtualNodes.get(hashOfKey);
    }

    private Long hash(String nodeName, int number) {
        /*
         *通过MD5进行计算hash值
         * */
        byte[] digest = md5(nodeName);
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public byte[] md5(String str) {
        try {
            /*
            * 对象初始化
            * */
            MessageDigest md = MessageDigest.getInstance("MD5");
            /*
            * 重置
            * */
            md.reset();
            /*
            * 设置编码为UTF-8
            * */
            md.update(str.getBytes("UTF-8"));
            /*
            * 返回md.digest()
            * */
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNode(String node){
        nodes.add(node);
        String virtualNodeName = getNodeNameByIndex(node, 0);
        for (int i = 0; i < replicCnt/4; i++) {
            for (int j = 0; j < 4; j++) {
                virtualNodes.put(hash(virtualNodeName, j), node);
            }
        }
    }

    public void removeNode(String node){
        nodes.remove(node);
        String virtualNodeName = getNodeNameByIndex(node, 0);
        for (int i = 0; i < replicCnt/4; i++) {
            for (int j = 0; j < 4; j++) {
                virtualNodes.remove(hash(virtualNodeName, j), node);
            }
        }
    }

    private void printTreeNode(){
        if (virtualNodes != null && ! virtualNodes.isEmpty()){
            virtualNodes.forEach((hashKey, node) ->
                    System.out.println(
                            new StringBuffer(node)
                                    .append(" ==> ")
                                    .append(hashKey)
                    )
            );
        }else
            System.out.println("Cycle is Empty");
    }
    @Test
    public static void main(String[] args){
        LinkedList<String> nodes = new LinkedList<>();
        nodes.add("192.168.2.1:8080");
        nodes.add("192.168.2.2:8080");
        nodes.add("192.168.2.3:8080");
        nodes.add("192.168.2.4:8080");
        ConsistentHashLoadBalance consistentHash = new ConsistentHashLoadBalance(nodes, 160);
        consistentHash.printTreeNode();
    }
}
