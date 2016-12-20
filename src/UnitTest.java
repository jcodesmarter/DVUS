/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.project.db.Database;
import com.project.db.Mysql;
import com.project.logger.Logs;
import java.sql.SQLException;
/**
 *
 * @author RavirajS
 */
public class UnitTest {
    public static void logSystemTest()
    {
        Logs.write("Test");
    }
    
    public static void mysqlTest() throws ClassNotFoundException, SQLException
    {
        Database m = new Mysql("localhost", "3306", "database_schema", "root", "root");        
        m.testMetaData();
        
    }
    
    public static void main(String args[])
    {
        try
        {
        UnitTest.mysqlTest();
        }
        catch(Exception Exc)
        {
            Logs.write("Unit Testing", Exc);
        }
    }
    
}
