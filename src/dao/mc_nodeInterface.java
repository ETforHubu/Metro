package dao;

import entity.mc_node;

import java.util.List;

/**
 * Created by Mr.Zhang on 2020/11/2.
 */
public interface mc_nodeInterface {
    mc_node add(mc_node b);
    boolean delete(String b);
    boolean update(mc_node b);
    boolean updateParent(mc_node b);
    boolean updateExcute(mc_node b);
    mc_node selectById(String b);
    mc_node selectByName(mc_node b);
    List<mc_node> selectChild(String b);
}
