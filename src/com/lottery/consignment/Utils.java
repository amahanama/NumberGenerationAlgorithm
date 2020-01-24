package com.lottery.consignment;

import com.lottery.db.DBConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class Utils {

    public static int count = 0;
    /**
     * @param input
     * @return
     */
    public static String getMd5(String input) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /* arr[]  ---> Input Array
   data[] ---> Temporary array to store current combination
   start & end ---> Staring and Ending indexes in arr[]
   index  ---> Current index in data[]
   r ---> Size of a combination to be printed */
    public static void combinationUtil(Map<String, String> results, int[] arr, int[] data, int start,
                                       int end, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            String s = "";
            for (int j = 0; j < r; j++) {
                String suffix = j == r - 1 ? "" : " ";
                s = s + (data[j] < 10 ? "0" + data[j] : data[j]) + suffix;
            }
            results.put(s, getMd5(s));
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(results, arr, data, i + 1, end, index + 1, r);
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public static Map<String, String> getAllCombinations(int[] arr, int n, int r) {
        // A temporary array to store all combination one by one
        int[] data = new int[r];
        Map<String, String> results = new HashMap<>();

        // Print all combination using temprary array 'data[]'
        combinationUtil(results, arr, data, 0, n - 1, 0, r);
        return results;
    }

    /**
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * @param file
     * @param arrData
     * @throws IOException
     */
/**
    public static void createFile(String file, List<String> arrData)
            throws IOException {
        FileWriter writer = new FileWriter(file + ".txt");
        int size = arrData.size();
        for (int i = 0; i < size; i++) {
            String str = arrData.get(i);
            writer.write(str);
            if (i < size - 1)//This prevent creating a blank line at the end of the file**
                writer.write("\n");
        }
        writer.close();
    }*/

   public static void createFile(int file, List<String> arrData,String brand_code)
            throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        int bookNo=1;
        int bookCount=0;


        try {

            if (conn == null)
                conn = DBConnection.getConnection();
            int size = arrData.size();
            //int current_drw=getLatestDrawNo("AK") +1;
            System.out.println("Writing draw no>> "+file);
            String INSERT_RECORD = "INSERT INTO `"+brand_code+"_combinations`(`no1`, `no2`, `no3`, `no4`, `eng_letter`, `draw_no`, `book_no`,`order_status`,`consigment_no`) VALUES (?,?,?,?,?,?,?,?,?)";


            System.out.println("INSERT_RECORD>> "+INSERT_RECORD);
            ps = conn.prepareStatement(INSERT_RECORD);

           /* FileWriter writer = new FileWriter("TICKET_FILES/"+file + ".txt");*///AVOID WRITING TO .txt files ANURUDDHIKA

            int consig_no=getLatestConsignment(brand_code);
            ++consig_no;

            for (int i = 0; i < size; i++) {
                String str = arrData.get(i);
               /* writer.write(str);
                if (i < size - 1)//This prevent creating a blank line at the end of the file**
                    writer.write("\n");*///AVOID WRITING TO .txt files ANURUDDHIKA
                count++;
                String[] parts = str.split(" ");//Split string based on spaces and add to array

                //Save to DB
                ps.setInt(1, Integer.parseInt(parts[1]));
                ps.setInt(2, Integer.parseInt(parts[2]));
                ps.setInt(3, Integer.parseInt(parts[3]));
                ps.setInt(4, Integer.parseInt(parts[4]));
                ps.setString(5, parts[0]);
                ps.setInt(6, file);

               // ps.setInt(7, count);
                bookCount++;
                ps.setInt(7, bookNo);
                ps.setString(8, "Y");
                if(bookCount == 10) {
                    bookNo++;
                    bookCount=0;
                }
                ps.setInt(9,consig_no);
                ps.executeUpdate();

               // updateDrawNo("AK", current_drw);
                //END
            }
           /* if(writer != null)
                writer.close();*///AVOID WRITING TO .txt files ANURUDDHIKA


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
    }


    public static void update_unique_Status(int draw_no, int count){
        Connection conn = null;
        PreparedStatement ps = null;


        try {

            if (conn == null)
                conn = DBConnection.getConnection();
            System.out.println("Unordered Count :"+count);

            String INSERT_RECORD = "UPDATE `ak_combinations` SET `order_status`  = 'N' where draw_no = "+draw_no+" ORDER BY rec_count DESC LIMIT "+count+" ";
            ps = conn.prepareStatement(INSERT_RECORD);
            ps.executeUpdate();

            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }   finally {
            DBConnection.closeConnection(conn);
        }
    }

    /**
    *
    * Function to select the latest draw number for each draw
    * */

    public static int getLatestDrawNo(String brandCode) {
        int drawNo = 0;
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT MAX(`draw_no`) FROM `" + brandCode + "_combinations`  WHERE order_status=\"Y\"";
            ResultSet rs= stmt.executeQuery(sql);

            while (rs.next())
                drawNo = rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return drawNo;
    }

    public static int getLatestConsignment(String brandCode) {
        int con_no = 0;
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT MAX(`consigment_no`) FROM `" + brandCode + "_combinations`  WHERE order_status=\"Y\"";
            ResultSet rs= stmt.executeQuery(sql);

            while (rs.next())
                con_no = rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con_no;
    }


   /* public static int getLatestDrawNo(String brand_code){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        int latest_draw=0;

        try {

            if (conn == null)
                conn = DBConnection.getConnection();
                stmt=conn.createStatement();

                String query="SELECT MAX(draw_no) from latest_draw_no where brand= '"+brand_code+"'";
                rs=stmt.executeQuery(query);

                if(rs.next()){
                    latest_draw=rs.getInt(1);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return latest_draw;
    }*/

    /**
     * Function to update Draw no to automate the consignment generation process
     * */
    public static void updateDrawNo(String brand, int draw_no)
            throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            if (conn == null)
                conn = DBConnection.getConnection();

                String UPDATE_RECORD = "UPDATE `latest_draw_no` SET `draw_no`="+draw_no+" WHERE brand='"+brand+"'";
                ps = conn.prepareStatement(UPDATE_RECORD);

                ps.setInt(1, draw_no);
                ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

/**
 * Function to calculate check digit usin lugn algorithm
 */

    private static int generateCheckDigit(long l) {
        String str = Long.toString(l);
        int[] ints = new int[str.length()];
        for(int i = 0;i< str.length(); i++){
            ints[i] = Integer.parseInt(str.substring(i, i+1));
        }
        for(int i = ints.length-2; i>=0; i=i-2){
            int j = ints[i];
            j = j*2;
            if(j>9){
                j = j%10 + 1;
            }
            ints[i]=j;
        }
        int sum=0;
        for(int i = 0;i< ints.length; i++){
            sum+=ints[i];
        }
        if(sum%10==0){
            return 0;
        }else return 10-(sum%10);
    }

}
