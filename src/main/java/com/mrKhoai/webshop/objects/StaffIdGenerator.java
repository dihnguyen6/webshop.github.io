package com.mrKhoai.webshop.objects;

import com.mrKhoai.webshop.controller.WebshopConst;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StaffIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        Staff s = (Staff) o;
        Connection connection = sharedSessionContractImplementor.connection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(staff_id) as Id from staff");
            if(resultSet.next()) {
                int id = resultSet.getInt(1) + 1;
                String generatedId = WebshopConst.PREFIX + s.getRole().getRoleName() + new Integer(id).toString();
                return generatedId;
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            AccountIdGenerator.closeStatement(statement, resultSet);
        }
        return WebshopConst.PREFIX;
    }
}
