package dao;

import db.DBConnection;
import entity.mc_node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class mc_nodeDAO implements mc_nodeInterface{

    @Override
    public mc_node add(mc_node b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "insert into mc_node values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,b.getId());
            ps.setString(2,b.getParentId());
            ps.setString(3,b.getTitle());
            ps.setString(4,b.getCode());
            ps.setString(5,b.getValue());
            ps.setString(6,b.getNote());
            ps.setString(7,b.getOther());
            ps.setString(8,b.getSort());
            ps.setDouble(9,b.getQuantity());
            ps.setString(10,b.getUnit());
            ps.setDouble(11,b.getConstructionCost());
            ps.setDouble(12,b.getInstallCost());
            ps.setDouble(13,b.getDeviceCost());
            ps.setDouble(14,b.getOtherCost());
            ps.setDouble(15,b.getQuota());
            ps.setString(16,b.getsNumber());
            i = ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public List<mc_node> selectChild(String b) {
        List<mc_node> listMc_node=new ArrayList<>();
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            ResultSet rs = null;
            String sql = "select * from mc_node where ParentID = ? order by SNUMBER asc";
            ps = conn.prepareStatement(sql);
            ps.setString(1,b);
            rs = ps.executeQuery();
            while (rs.next()){
                String id= rs.getString(1);
                String parentId =rs.getString(2);
                String title = rs.getString(3);
                String unit = rs.getString(10);
                String sNumber = rs.getString(16);
                double quantity = rs.getDouble(9);
                double constructionCost = rs.getDouble(11);
                double installCost = rs.getDouble(12);
                double deviceCost = rs.getDouble(13);
                double otherCost = rs.getDouble(14);
                mc_node mc_node1 = new mc_node();
                mc_node1.setId(id);
                mc_node1.setParentId(parentId);
                mc_node1.setTitle(title);
                mc_node1.setUnit(unit);
                mc_node1.setsNumber(sNumber);
                mc_node1.setQuantity(quantity);
                mc_node1.setConstructionCost(constructionCost);
                mc_node1.setInstallCost(installCost);
                mc_node1.setDeviceCost(deviceCost);
                mc_node1.setOtherCost(otherCost);
                listMc_node.add(mc_node1);
            }
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listMc_node;
    }

    @Override
    public mc_node selectById(String b) {
        mc_node mc_node1 = new mc_node();
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            ResultSet rs = null;
            String sql = "select * from mc_node where id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,b);
            rs = ps.executeQuery();
            if(rs.next()){
                String id= rs.getString(1);
                String parentId =rs.getString(2);
                String title = rs.getString(3);
                String code = rs.getString(4);
                String value = rs.getString(5);
                String note = rs.getString(6);
                String other = rs.getString(7);
                String sort = rs.getString(8);
                double quantity = rs.getDouble(9);
                String unit = rs.getString(10);
                double constructionCost = rs.getDouble(11);
                double installCost = rs.getDouble(12);
                double deviceCost = rs.getDouble(13);
                double otherCost = rs.getDouble(14);
                double quota = rs.getDouble(15);
                String sNumber= rs.getString(16);

                mc_node1.setId(id);
                mc_node1.setParentId(parentId);
                mc_node1.setTitle(title);
                mc_node1.setCode(code);
                mc_node1.setValue(value);
                mc_node1.setNote(note);
                mc_node1.setOther(other);
                mc_node1.setSort(sort);
                mc_node1.setQuantity(quantity);
                mc_node1.setUnit(unit);
                mc_node1.setConstructionCost(constructionCost);
                mc_node1.setInstallCost(installCost);
                mc_node1.setDeviceCost(deviceCost);
                mc_node1.setOtherCost(otherCost);
                mc_node1.setQuota(quota);
                mc_node1.setsNumber(sNumber);
            }
            ps.close();
            conn.close();
            return mc_node1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean delete(String b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "delete from mc_node where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,b);
            i = ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(mc_node b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "update mc_node set TITLE = ?,UNIT = ?,CONSTRUCTIONCOST = ?,INSTALLCOST = ?,DEVICECOST = ?,OTHERCOST = ?,SNUMBER =?,QUANTITY=? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(9,b.getId());
            ps.setString(1,b.getTitle());
            ps.setString(2,b.getUnit());
            ps.setDouble(3,b.getConstructionCost());
            ps.setDouble(4,b.getInstallCost());
            ps.setDouble(5,b.getDeviceCost());
            ps.setDouble(6,b.getOtherCost());
            ps.setString(7,b.getsNumber());
            ps.setDouble(8,b.getQuantity());
            i = ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateParent(mc_node b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "update mc_node set CONSTRUCTIONCOST = ?,INSTALLCOST = ?,DEVICECOST = ?,OTHERCOST = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(5,b.getId());
            ps.setDouble(1,b.getConstructionCost());
            ps.setDouble(2,b.getInstallCost());
            ps.setDouble(3,b.getDeviceCost());
            ps.setDouble(4,b.getOtherCost());
            i = ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateExcute(mc_node b) {
        try{
            Statement stmt;
            Connection conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            String sql ="update mc_node set title = '"+b.getTitle()+"' where id ='"+ b.getId()+"'";
            boolean hasResultSet = stmt.execute(sql);
            conn.commit();
            stmt.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public mc_node selectByName(mc_node b) {
//        for (Book book:list){
//            if(book.getName().toUpperCase()==b.getName().toUpperCase()){
//                return book;
//            }
//        }
     return null;
    }

}
