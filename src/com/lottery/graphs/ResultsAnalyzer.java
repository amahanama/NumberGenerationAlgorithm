package com.lottery.graphs;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lottery.db.DBConnection;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ResultsAnalyzer {
    Document document = new Document();
    Connection conn = null;
    ResultSet rs = null;
    Statement stmt = null;

    public  void resultAnalyzer(String game_code, int draw_no,String game_name) {
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            int range = 100;

            //Create PDF with Given Name
            PdfWriter.getInstance(document,
                    new FileOutputStream("DLB_"+game_code+"_Result_Analyzys.pdf"));

            //OPEN PDF DOCUMENT
            document.open();
            Paragraph pr = new Paragraph(game_name+" RESULT ANALYSYS");
            pr.setAlignment(Element.ALIGN_CENTER);
            pr.setSpacingBefore(50);
            pr.setSpacingAfter(25);
            document.add(pr);

            //CREATE AND ADD TABLE HEADERS
            PdfPTable table = new PdfPTable(3); // 3 columns.
            PdfPCell cell1 = new PdfPCell(new Paragraph("Book Range"));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell2 = new PdfPCell(new Paragraph("Zero Winnings Count"));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell3 = new PdfPCell(new Paragraph("Zero Winnings %"));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            //SELECT RECORDS
            int count = 0;
            int tot_count = 0;

            System.out.println("Draw Number >>>>>>>>>>>> "+draw_no);

            //QUERY to find Total prize count
            String query = "SELECT COUNT(rec_count) FROM "+game_code+"_prize_count where draw_no="+draw_no;

            System.out.println(query);
            rs = stmt.executeQuery(query);

            while (rs.next())
                tot_count = rs.getInt(1);

            double p = 0.00;//0 winning percentage

            //CREATE DECIMAL FOrmatter for format the percentage in to 2 decimal points
            Locale locale = new Locale("en", "UK");
            String pattern = "###.##";
            DecimalFormat decimalFormat = (DecimalFormat)
                    NumberFormat.getNumberInstance(locale);
            decimalFormat.applyPattern(pattern);
            ArrayList n=new ArrayList();


            int i=0;

            //Find the 0 winnings book count of each range
            do {
                String select_query = "SELECT COUNT(rec_count) FROM `"+game_code+"_prize_count` WHERE book_no > " + count + " AND book_no <= " + (count + range) + " AND rec_count=0 AND draw_no ="+draw_no+"";

                System.out.println("Q > "+select_query);
                rs = stmt.executeQuery(select_query);

                PdfPCell c1 = new PdfPCell(new Paragraph(count + " - " + (count + range))); //Book range
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell c2 = null;
                while (rs.next()) {
                    c2 = new PdfPCell(new Paragraph(String.valueOf(rs.getInt(1))));//0 winnings count
                    c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    if (range > 0)
                        p = (rs.getDouble(1) / range * 100);//Calculate the 0 winnings percentage for each range
                    n.add(rs.getInt(1));
                    i++;
                }

                table.addCell(c1);
                table.addCell(c2);
                table.addCell(decimalFormat.format(p));
                count = count + range;
            } while (count < tot_count); //Continue for total books

            document.add(table);

            double[] numArray = new double[n.size()];
            for(int x=0;x<n.size();x++){
                numArray[x] =Double.valueOf(n.get(x).toString());
            }
            double sd=calculateSD(numArray);
            double mode=calculateMode(numArray);
            double mean=calculateMean(numArray);
            double total =calculateTotal(numArray);

            saveProbData(draw_no,total,mean,mode,sd,game_code);

            Paragraph pr1 = new Paragraph("Total : "+total+" Mean : " +mean+" Standard Deviation : "+ sd + " Mode : "+mode);
            pr1.setAlignment(Element.ALIGN_CENTER);
            pr1.setSpacingBefore(50);
            pr1.setSpacingAfter(25);
            document.add(pr1);
            document.close();
            System.out.println("PDF completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to calculate Total
     *
     * @param numArray*/
    public static double calculateTotal(double[] numArray)
    {
        double sum = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        return sum;
    }


    /**
     * Function to calculate Mean
     *
     * @param numArray*/
    public static double calculateMean(double[] numArray)
    {
        double sum = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        return mean;
    }

/**
 * Function to calculate Standard Deviation
 *
 * @param numArray*/
    public static double calculateSD(double[] numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

    /**
     * Function to calculate mode value of number array
     * */
    public static double calculateMode(double a[]) {
        int n =a.length;
        double maxValue = 0;
        int maxCount = 0, i, j;

        for (i = 0; i < n; ++i) {
            int count = 0;
            for (j = 0; j < n; ++j) {
                if (a[j] == a[i])
                    ++count;
            }

            if (count > maxCount) {
                maxCount = count;
                maxValue = a[i];
            }
        }
        return maxValue;
    }

    public static void saveProbData(int draw_no, double total,double mean, double mode, double stdDeviation,String game_name) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            if (conn == null)
                conn = DBConnection.getConnection();

            String INSERT_RECORD = "INSERT INTO `prize_probability`(`draw_no`, `tot_zero_count`, `mean`, `mode`, `stdDeviation`,`brand_code`,`ent_user` ) VALUES (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(INSERT_RECORD);
            ps.setInt(1, draw_no);
            ps.setDouble(2, total);
            ps.setDouble(3, mean);
            ps.setDouble(4,mode);
            ps.setDouble(5,stdDeviation);
            ps.setString(6,game_name);
            ps.setString(7,"Anuruddhika");
            ps.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }   finally {
            DBConnection.closeConnection(conn);
        }
    }

}
