using System;
using System.Data;
using System.Data.SqlClient;
using System.Text;

public static class DL
{
    public static string getConnection()
    {
        string connStr = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
        return connStr;
    }
    public static string makejsonoftable(DataTable table)
    {
        StringBuilder sb = new StringBuilder();
        foreach (DataRow dr in table.Rows)
        {
            if (sb.Length != 0)
                sb.Append(",");
            sb.Append("{");
            StringBuilder sb2 = new StringBuilder();
            foreach (DataColumn col in table.Columns)
            {
                string fieldname = col.ColumnName;
                string fieldvalue = dr[fieldname].ToString();
                if (sb2.Length != 0)
                    sb2.Append(",");
                sb2.Append(string.Format("{0}:\"{1}\"", fieldname, fieldvalue));
            }
            sb.Append(sb2.ToString());
            sb.Append("}");
        }
        sb.Insert(0, "[");
        sb.Append("]");
        return sb.ToString();
    }
    public static DateTime getServerDateTime()
    {
        try
        {
            SqlCommand cmd = new SqlCommand();
            SqlDataAdapter da = new SqlDataAdapter();
            SqlConnection   con = new SqlConnection(getConnection());
            cmd.Connection = con;
            cmd.CommandText = "select GETDATE()";
            DateTime dtt = Convert.ToDateTime(cmd.ExecuteScalar());
            cmd.Dispose();
            da.Dispose();
            return dtt;
        }
        catch (Exception) { return DateTime.Now; }
    }

    public static string generateRandomStr(Int16 length)
    {
        Random rnd = new Random();
        string str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sbName = new StringBuilder();

        while ((length--) > 0)
        {
            sbName.Append(str[(int)(rnd.NextDouble() * str.Length)]);
        }

        return sbName.ToString();
    }
}