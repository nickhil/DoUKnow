using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Script.Services;
using System.Data.SqlClient;
using System.Data;

/// <summary>
/// Summary description for DoUKnow
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class DoUKnow : System.Web.Services.WebService {

    public DoUKnow () {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }

    [WebMethod(Description = "This executes spSaveUSer stored procedure and if result is 1 then not exist else if 2 then exist else if 0 then some error")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string SaveUser(String UserName, String FullName, String Password, String Profession, String MobileNo, String Email, String Imei)
    {
        SqlConnection con = new SqlConnection(DL.getConnection());
        SqlCommand cmd = new SqlCommand("spSaveUser", con);
        cmd.CommandType = CommandType.StoredProcedure;

        // parameters.........
        cmd.Parameters.Add("@PUserName", SqlDbType.VarChar, 25).Value = UserName;
        cmd.Parameters.Add("@PFullName", SqlDbType.VarChar, 75).Value = FullName;
        cmd.Parameters.Add("@PPassword", SqlDbType.VarChar, 75).Value = Password;
        cmd.Parameters.Add("@PProfession", SqlDbType.VarChar, 25).Value = Profession;
        cmd.Parameters.Add("@PMobileNo", SqlDbType.VarChar, 15).Value = MobileNo;
        cmd.Parameters.Add("@PEmail", SqlDbType.VarChar, 30).Value = Email;
        cmd.Parameters.Add("@PImei", SqlDbType.VarChar, 30).Value = Imei;
        cmd.Parameters.Add("@oErrorCode", SqlDbType.Char, 1).Direction = ParameterDirection.Output;
        con.Open();
        cmd.ExecuteNonQuery();
        con.Close();
        String s = " ";
        if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("1"))
            s = "1";
        else if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("0"))
            s = "0";
        else if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("2"))
            s = "2";

        string str = "[{result:\"" + s + "\"}]";
        return str;
    }

    [WebMethod(Description = "This executes spLogin stored procedure and if result is 1 then success then else if 0 then wrong credential")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string LoginUser(String UserName, String Password)
    {
        SqlConnection con = new SqlConnection(DL.getConnection());
        SqlCommand cmd = new SqlCommand("spLogin", con);
        cmd.CommandType = CommandType.StoredProcedure;

        // parameters.........
        cmd.Parameters.Add("@PUserName", SqlDbType.VarChar, 25).Value = UserName;
        cmd.Parameters.Add("@PPassword", SqlDbType.VarChar, 75).Value = Password;
        
        cmd.Parameters.Add("@oErrorCode", SqlDbType.Char, 1).Direction = ParameterDirection.Output;
        con.Open();
        cmd.ExecuteNonQuery();
        con.Close();
        String s = " ";
        if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("1"))
            s = "1";
        else if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("0"))
            s = "0";
        

        string str = "[{result:\"" + s + "\"}]";
        return str;
    }

    [WebMethod(Description = "This executes spSubmitQuestion stored procedure and if result is 1 then success then else if 0 then some problem")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string SubmitQuestion(String Category,String SubCategory,String Question,String AskedBy)
    {
        SqlConnection con = new SqlConnection(DL.getConnection());
        SqlCommand cmd = new SqlCommand("spSubmitQuestion", con);
        cmd.CommandType = CommandType.StoredProcedure;

        // parameters.........
        cmd.Parameters.Add("@PAskedBy", SqlDbType.VarChar, 25).Value = AskedBy;
        cmd.Parameters.Add("@PCategory", SqlDbType.VarChar, 50).Value = Category;
        cmd.Parameters.Add("@PSubCategory", SqlDbType.VarChar, 50).Value = SubCategory;
        cmd.Parameters.Add("@PQuestion", SqlDbType.VarChar, 500).Value = Question;
        

        cmd.Parameters.Add("@oErrorCode", SqlDbType.Char, 1).Direction = ParameterDirection.Output;
        con.Open();
        cmd.ExecuteNonQuery();
        con.Close();
        String s = " ";
        if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("1"))
            s = "1";
        else if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("0"))
            s = "0";


        string str = "[{result:\"" + s + "\"}]";
        return str;
    }


    [WebMethod(Description = "It executes spGetQuestions stored procedure.")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string GetQuestions(String SubCategory)
    {
        SqlConnection con = new SqlConnection(DL.getConnection());
        DataTable dt = new DataTable();
        SqlCommand cmd = new SqlCommand("spGetQuestions", con);
        cmd.CommandType = CommandType.StoredProcedure;
        cmd.Parameters.Add("@PSubCategory", SqlDbType.VarChar).Value = SubCategory;

        SqlDataAdapter da = new SqlDataAdapter(cmd);
        da.Fill(dt);
        string str = DL.makejsonoftable(dt);
        return str;
    }

    [WebMethod(Description = "It executes spGetAnswers stored procedure.")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string GetAnswers(String QNo)
    {
        Int64 Qno = Int64.Parse(QNo);
        SqlConnection con = new SqlConnection(DL.getConnection());
        DataTable dt = new DataTable();
        SqlCommand cmd = new SqlCommand("spGetAnswers", con);
        cmd.CommandType = CommandType.StoredProcedure;
        cmd.Parameters.Add("@PQNo", SqlDbType.BigInt).Value = Qno;

        SqlDataAdapter da = new SqlDataAdapter(cmd);
        da.Fill(dt);
        string str = DL.makejsonoftable(dt);
        return str;
    }

    [WebMethod(Description = "This executes spPostAnswer stored procedure and if result is 1 then success then else if 0 then some problem")]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string PostAnswer(String QNo, String Answers, String AnsweredBy)
    {
        Int64 Qno = Int64.Parse(QNo);
        SqlConnection con = new SqlConnection(DL.getConnection());
        SqlCommand cmd = new SqlCommand("spPostAnswer", con);
        cmd.CommandType = CommandType.StoredProcedure;

        // parameters.........
        cmd.Parameters.Add("@PQNo", SqlDbType.BigInt).Value = Qno;
        cmd.Parameters.Add("@PAnswers", SqlDbType.VarChar, 200).Value = Answers;
        cmd.Parameters.Add("@PAnsweredBy", SqlDbType.VarChar, 50).Value = AnsweredBy;
        


        cmd.Parameters.Add("@oErrorCode", SqlDbType.Char, 1).Direction = ParameterDirection.Output;
        con.Open();
        cmd.ExecuteNonQuery();
        con.Close();
        String s = " ";
        if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("1"))
            s = "1";
        else if (cmd.Parameters["@oErrorCode"].Value.ToString().Equals("0"))
            s = "0";


        string str = "[{result:\"" + s + "\"}]";
        return str;
    }

}
