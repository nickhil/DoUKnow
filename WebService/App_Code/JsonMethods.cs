using System;
using System.Data;
using System.Data.SqlClient;
using System.Text;


public class JsonMethods
{
	public JsonMethods()
	{
		
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
}