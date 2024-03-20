package org.gitee.ztkyn.core.db;

import cn.hutool.db.ds.pooled.DbConfig;
import cn.hutool.db.ds.pooled.PooledDataSource;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    /**
     * 从查询语句中解析字段类型，并生成对应的POJO类
     */
    public static void genPOJOFromSQL(DbConfig dbConfig, String sql) {
        try (PooledDataSource dataSource = new PooledDataSource(dbConfig);
             Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            TreeMap<String, String> columnMap = new TreeMap<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                String columnTypeName = metaData.getColumnTypeName(i);
                String columnClassName = metaData.getColumnClassName(i);
                List<String> packageArrays = Splitter.on(".").omitEmptyStrings().splitToList(columnClassName);
                String fieldStr = MessageFormat.format("private {0} {1};", packageArrays.get(packageArrays.size() - 1), columnName);
                columnMap.put(columnName, fieldStr);
            }
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                System.out.println(entry.getValue());
            }

        } catch (Exception exception) {
            logger.error("error", exception);
        }
    }

    /**
     * // 测试类
     public static void main(String[] args) throws ClassNotFoundException {
     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
     DbConfig dbConfig = new DbConfig();
     dbConfig.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
     dbConfig.setUrl("jdbc:sqlserver://119.96.222.153:1433;databaseName=" + "JiJiaUplus_7");
     dbConfig.setUser("sa");
     dbConfig.setPass("Jj20190610@Tx");
     dbConfig.setInitialSize(5);
     dbConfig.setMaxActive(10);
     String sql = "WITH H AS( SELECT * FROM ( SELECT PropertyCode, UnitCode, BuildingCode, ROW_NUMBER () OVER ( ORDER BY PropertyCode ASC ) OrderNum FROM dbo.H_Property WITH ( NOLOCK ) WHERE ModifyDate >= '2000-01-01' AND ModifyDate <= '2025-01-01' AND Trade = 2 AND CityID = 7 ) AS h ) SELECT TOP 10 P.HouseNumCode, P.PropertyCode, P.CityID , P.SystemTag , P.PropertyID , P.DataPropertyCode , P.LStatus , P.Trade , P.Name , P.Tel , P.BuildingCode , P.UnitCode , P.HouseNum , P.Private , P.Status , P.RunningStatus , P.Purpose , P.Grade , P.CountF , P.CountT , P.CountW , P.CountY , P.Price , P.UnitPrice , P.RentType , P.RentWay , P.MJ , P.UseMJ , P.GardenMJ , P.Floor, P.SumFloor, P.Orientation , P.Decorate , P.Title , P.PhotoNum , P.TrustDate , P.HomeAppliances , P.Furniture , P.RunLabel , P.TransLabel , P.FeatureLabel , P.RetainedState , P.EntrustType , P.TitleImgUrl , P.IsLock , P.NewHouseCID , P.KPEmpCode , P.KPDepartCode , P.KPDate , P.WHEmpCode , P.WHDepartCode , P.BeltWatchDate , P.FollowDate , P.ToDataTime , P.CallDate , P.ConvertPublicDate , P.BeltTimes , P.IsDel , P.Creator , P.CreateDate , P.Modifier , P.ModifyDate, P.CheckResult, P.PropertyLawCode, P.IsShow, P.CurrentSituation, PET.FloorHeight, PET.ElectricCharge, PET.WaterRate, PET.PropertyType, PET.WorkStation, PET.PropertyFee, PET.AirCondition, PET.HouseRate, PET.ElectricPower, PET.OfficeNum, PET.IsCanteen, PET.IsDorm, PET.IsWC, PET.FloorNum, PET.OfficeFeature, PET.DoorWidth, PET.IsLoan, PET.Depth, PET.RegCertificateDate, B.Nature, PET.PayWay, PET.LookHouse, PET.Expenses, PET.Certificate, law.PropertyLawNo, law.PropertyLawUrl, law.CompanyOrgID, law.CompanyLawUrl, law.LawEmpNo, law.LawEmpUrl, P.IsElevator, u.Stairs, u.HouseHolds, B.MetroID, B.MetroSiteID, PE.EmpID, ValidExploration = ( SELECT TOP 1 PEL.Status FROM dbo.H_PropertyExploration PEL ( NOLOCK ) WHERE PEL.IsDel = 0 AND PEL.PropertyCode = P.PropertyCode AND PEL.Status = 1 ) FROM H WITH ( NOLOCK ) INNER JOIN dbo.H_Property P ( NOLOCK ) ON P.PropertyCode = H.PropertyCode LEFT JOIN dbo.H_PropertyLaw law ( NOLOCK ) ON P.PropertyLawCode= law.PropertyLawCode LEFT JOIN dbo.H_PropertyExtend PET ( NOLOCK ) ON PET.PropertyCode = H.PropertyCode LEFT JOIN dbo.B_RidgepoleUnit AS u ( NOLOCK ) ON u.RidgepoleUnitCode = H.UnitCode LEFT JOIN dbo.B_Building AS B ( NOLOCK ) ON B.BuildingCode = H.BuildingCode LEFT JOIN dbo.P_Employee PE ( NOLOCK ) ON PE.EmpCode = P.WHEmpCode AND PE.CityID= 7 ORDER BY H.OrderNum ASC";
     genPOJOFromSQL(dbConfig, sql);
     }
     */

}
