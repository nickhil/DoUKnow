using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text;
using System.Data.SqlClient;
using System.Data;


public class clsPin
{
    SqlConnection con;
    SqlCommand cmd;
    String pin1, sLoginuName, day_, month_, year_;

    public clsPin()
    {
        con = new SqlConnection(DL.getConnection());
    }
    public DateTime getServerDateTime()
    {
        try
        {
            SqlCommand cmd = new SqlCommand();
            SqlDataAdapter da = new SqlDataAdapter();
            // con = DL.getConnection();
            cmd.Connection = con;
            cmd.CommandText = "select GETDATE()";
            con.Open();
            DateTime dtt = Convert.ToDateTime(cmd.ExecuteScalar());
            cmd.Dispose();
            da.Dispose();
            return dtt;
        }
        catch (Exception) { return DateTime.Now; }
    }

    public String pin_new_old(String loginUName, int offerId)
    {
        //int day = DateTime.Now.Day;
        //int month = DateTime.Now.Month;
        //int year = DateTime.Now.Year;
        int day = getServerDateTime().Day;
        int month = getServerDateTime().Month;
        int year = getServerDateTime().Year;


        // TWO APLPHABETS REVERSED 7 APPEND EACH OTHER with # ------------------------------------
        cmd = new SqlCommand();
        //con = DL.getConnection();
        cmd.Connection = con;
        cmd.CommandText = "SELECT u.userName FROM users u, offers o WHERE o.offerId=" + offerId + " AND u.identityId = o.identityId";
        String restauUName = cmd.ExecuteScalar().ToString();
        StringBuilder sb = new StringBuilder(restauUName);
        int j = 0;
        while (j < restauUName.Length - 1)
        {
            char t = sb[j];
            char t1 = sb[j + 1];
            sb[j] = t1;
            sb[j + 1] = t;
            j = j + 2;
        }
        StringBuilder sb_ = new StringBuilder();
        if (sb.Length < 8)
        {
            int i = 0;
            while (i < (8 - sb.Length))
            {
                sb_ = sb_.Append("#");
                i++;
            }
            sb_ = sb_.Append(sb);
        }

        //IF LOGIN USER NAME IS < 8 IN LENGTH THEN APPEND IT WITH $ ------------------------------
        if (loginUName.Length < 8)
        {
            int i = 0;
            while (i < (8 - loginUName.Length))
            {
                sLoginuName = "$" + sLoginuName;
                i++;
            }
            sLoginuName = sLoginuName + loginUName;
        }

        //RANDOM NUMBER ------------------------------
        Random rnd = new Random();
        int len = 6;
        string str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb1 = new StringBuilder();

        while ((len--) > 0)
        {
            sb1.Append(str[(int)(rnd.NextDouble() * str.Length)]);
        }

        //DATE NAMINGS ------------------------------       
        switch (month)
        {
            case 1:
                month_ = "z";
                break;
            case 2:
                month_ = "y";
                break;
            case 3:
                month_ = "x";
                break;
            case 4:
                month_ = "w";
                break;
            case 5:
                month_ = "v";
                break;
            case 6:
                month_ = "u";
                break;
            case 7:
                month_ = "t";
                break;
            case 8:
                month_ = "s";
                break;
            case 9:
                month_ = "r";
                break;
            case 10:
                month_ = "q";
                break;
            case 11:
                month_ = "p";
                break;
            case 12:
                month_ = "o";
                break;
        }

        switch (day)
        {
            case 1:
                day_ = "D0";
                break;
            case 2:
                day_ = "E0";
                break;
            case 3:
                day_ = "F0";
                break;
            case 4:
                day_ = "G0";
                break;
            case 5:
                day_ = "H0";
                break;
            case 6:
                day_ = "I0";
                break;
            case 7:
                day_ = "J0";
                break;
            case 8:
                day_ = "K0";
                break;
            case 9:
                day_ = "L0";
                break;
            case 10:
                day_ = "DA";
                break;
            case 11:
                day_ = "EA";
                break;
            case 12:
                day_ = "FA";
                break;
            case 13:
                day_ = "GA";
                break;
            case 14:
                day_ = "HA";
                break;
            case 15:
                day_ = "IA";
                break;
            case 16:
                day_ = "JA";
                break;
            case 17:
                day_ = "KA";
                break;
            case 18:
                day_ = "LA";
                break;
            case 19:
                day_ = "DB";
                break;
            case 20:
                day_ = "EB";
                break;
            case 21:
                day_ = "FB";
                break;
            case 22:
                day_ = "GB";
                break;
            case 23:
                day_ = "HB";
                break;
            case 24:
                day_ = "IB";
                break;
            case 25:
                day_ = "JB";
                break;
            case 26:
                day_ = "KB";
                break;
            case 27:
                day_ = "LB";
                break;
            case 28:
                day_ = "DC";
                break;
            case 29:
                day_ = "EC";
                break;
            case 30:
                day_ = "FC";
                break;
            case 31:
                day_ = "GC";
                break;
        }

        year_ = year.ToString();
        year_ = year_.Substring(2);


        pin1 = "#" + sb_ + "$" + sLoginuName + sb1 + "_" + month_ + year_ + day_;

        //CHEKING PIN ALREADY EXISTS OR NOT -----------------------------
        try
        {
            cmd = new SqlCommand();
            con = new SqlConnection(DL.getConnection());
            cmd.Connection = con;
            cmd.CommandText = "SELECT pin FROM availedOffers WHERE pin='" + pin1 + "'";
            String existPin = cmd.ExecuteScalar().ToString();
            con.Close();
            if (existPin == null || existPin == "") { }
            else { pin1 = pin_new_old(loginUName, offerId); }
        }
        catch { }

        return pin1;
    }

    public String pin_new(String loginUName, int offerId)
    {
        //int day = DateTime.Now.Day;
        //int month = DateTime.Now.Month;
        //int year = DateTime.Now.Year;
        int day = getServerDateTime().Day;
        int month = getServerDateTime().Month;
        int year = getServerDateTime().Year;

        //RANDOM NUMBER ------------------------------
        Random rnd = new Random();
        int len = 4;
        string str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb1 = new StringBuilder();

        while ((len--) > 0)
        {
            sb1.Append(str[(int)(rnd.NextDouble() * str.Length)]);
        }

        //DATE NAMINGS ------------------------------       
        switch (month)
        {
            case 1:
                month_ = "z";
                break;
            case 2:
                month_ = "y";
                break;
            case 3:
                month_ = "x";
                break;
            case 4:
                month_ = "w";
                break;
            case 5:
                month_ = "v";
                break;
            case 6:
                month_ = "u";
                break;
            case 7:
                month_ = "t";
                break;
            case 8:
                month_ = "s";
                break;
            case 9:
                month_ = "r";
                break;
            case 10:
                month_ = "q";
                break;
            case 11:
                month_ = "p";
                break;
            case 12:
                month_ = "o";
                break;
        }

        switch (day)
        {
            case 1:
                day_ = "D0";
                break;
            case 2:
                day_ = "E0";
                break;
            case 3:
                day_ = "F0";
                break;
            case 4:
                day_ = "G0";
                break;
            case 5:
                day_ = "H0";
                break;
            case 6:
                day_ = "I0";
                break;
            case 7:
                day_ = "J0";
                break;
            case 8:
                day_ = "K0";
                break;
            case 9:
                day_ = "L0";
                break;
            case 10:
                day_ = "DA";
                break;
            case 11:
                day_ = "EA";
                break;
            case 12:
                day_ = "FA";
                break;
            case 13:
                day_ = "GA";
                break;
            case 14:
                day_ = "HA";
                break;
            case 15:
                day_ = "IA";
                break;
            case 16:
                day_ = "JA";
                break;
            case 17:
                day_ = "KA";
                break;
            case 18:
                day_ = "LA";
                break;
            case 19:
                day_ = "DB";
                break;
            case 20:
                day_ = "EB";
                break;
            case 21:
                day_ = "FB";
                break;
            case 22:
                day_ = "GB";
                break;
            case 23:
                day_ = "HB";
                break;
            case 24:
                day_ = "IB";
                break;
            case 25:
                day_ = "JB";
                break;
            case 26:
                day_ = "KB";
                break;
            case 27:
                day_ = "LB";
                break;
            case 28:
                day_ = "DC";
                break;
            case 29:
                day_ = "EC";
                break;
            case 30:
                day_ = "FC";
                break;
            case 31:
                day_ = "GC";
                break;
        }

        year_ = year.ToString();
        year_ = year_.Substring(2);


        pin1 = sb1 + "_" + month_ + year_ + day_;

        //CHEKING PIN ALREADY EXISTS OR NOT -----------------------------
        try
        {
            cmd = new SqlCommand();
            con = new SqlConnection(DL.getConnection());
            cmd.Connection = con;
            con.Open();
            cmd.CommandText = "SELECT pin FROM availedOffers WHERE pin='" + pin1 + "'";
            String existPin = cmd.ExecuteScalar().ToString();
            con.Close();
            if (existPin == null || existPin == "") { }
            else { pin1 = pin_new(loginUName, offerId); }
        }
        catch { }

        return pin1;
    }
}