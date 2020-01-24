package com.lottery.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.lottery.db.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is to create a PDF file.
 */
public class PDFCreator {

    public  static  Connection connection = null;
    public  static Statement stmt;
    public  static PreparedStatement ps =null;
    public  static ResultSet rs=null;
    public  static String brand_code="";
    public  static String txt4NOEngLetter,txt4NO, txt3NOEngLetter, txt3NO, txt2NOEngLetter, txt2NO, txt1NOEngLetter, txt1NO, EngLetter, txtTicketPrice, txtJackpotPercentage, txtJackpot, txt4NOEngLetterWin, txt4NOWin, txt3NOEngLetterWin, txt3NOWin, txt2NOEngLetterWin, txt2NOWin, txt1NOEngLetterWin,txt1NOWin, EngLetterWin="";

    public  static int tot_consignment=0;
    public static int eng_letter_count=0;

    private final static String[] HEADER_ARRAY = {"MATCHES", "NO.OF WINNERS", "PRIZE", "AMOUNT"};
    public final static Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    public final static Font MEDIUM_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    public final static Font LARGE_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    public final static Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL);

    public static void addMetaData(Document document, String sqlXMLFileName) {
        document.addTitle("ADA KOTIPATHI RESULTS");
        document.addSubject("PRIZE DISTRIBUTION");
        document.addAuthor("Anuruddhika");
    }

    /*static int nCr(int n, int r)
    {
        int ncr_val=0;
        System.out.println(n+" - "+r);
        int fr=factorial(r);
        int x= (n - r);
        int fx= factorial(x);
        System.out.println(" x "+x);
        System.out.println(" fr "+fr);
        System.out.println(" fx "+fx);

        if(fr > 0  && fx >0) {
            ncr_val = factorial(n) / (fr * fx);
            System.out.println(" ncr_valx "+ncr_val);
            return ncr_val;
        }else
            return ncr_val;
    }

    // Returns factorial of n

    static int factorial(int n)
    {
        if (n == 0)
            return 1;

        return n*factorial(n-1);
    }*/

    static int printNcR(int n, int r) {

        // p holds the value of n*(n-1)*(n-2)...,
        // k holds the value of r*(r-1)...
        int p = 1, k = 1;

        // C(n, r) == C(n, n-r),
        // choosing the smaller value
        if (n - r < r) {
            r = n - r;
        }

        if (r != 0) {
            while (r > 0) {
                p *= n;
                k *= r;

                // gcd of p, k
                long m = __gcd(p, k);

                // dividing by gcd, to simplify product
                // division by their gcd saves from the overflow
                p /= m;
                k /= m;

                n--;
                r--;
            }

            // k should be simplified to 1
            // as C(n, r) is a natural number
            // (denominator should be 1 ) .
        } else {
            p = 1;
        }

        // if our approach is correct p = ans and k =1
        System.out.println(p);
        return p;

    }

    static long __gcd(long n1, long n2) {
        long gcd = 1;

        for (int i = 1; i <= n1 && i <= n2; ++i) {
            // Checks if i is factor of both integers
            if (n1 % i == 0 && n2 % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }



    /**
     * Helper methods start here
     **/
    public static void addTitlePage(Document document, String title, int no1, int no2, int no3, int no4, String letter, int draw_no, int sold_count, int start_no, String brand, String brand_code) throws DocumentException {
        String n1 = "";
        String n2 = "";
        String n3 = "";
        String n4 = "";
        int j_count = 0;
        int count4No = 0;
        int count3No_L = 0;
        int count3No = 0;
        int count2Nos_L = 0;
        int count2No = 0;
        int count1NO_L = 0;
        int count1No = 0;
        int count_Letter = 0;
        int total_count = 0;
        int start = 0;

        int ltr_count = 0;

        double tot_j_count = 0;
        double tot_count4No = 0;
        double tot_count3No_L = 0;
        double tot_count3No = 0;
        double tot_count2Nos_L = 0;
        double tot_count2No = 0;
        double tot_count1NO_L = 0;
        double tot_count1No = 0;
        double tot_count_Letter = 0;
        double sum_total_count = 0;
        double next_jackpot = 0;
        double per_day_count = 0;

        if (no1 < 10)
            n1 = "0" + no1;
        else
            n1 = "" + no1;

        if (no2 < 10)
            n2 = "0" + no2;
        else
            n2 = "" + no2;

        if (no3 < 10)
            n3 = " 0" + no3;
        else
            n3 = "" + no3;

        if (no4 < 10)
            n4 = " 0" + no4;
        else
            n4 = "" + no4;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String p1 = n1 + " " + n2 + " " + n3 + " " + n4;
        Paragraph preface1 = new Paragraph();
        preface1.setAlignment(Paragraph.ALIGN_CENTER);
        preface1.add(new Phrase(title, LARGE_BOLD));
        addEmptyLine(preface1, 1);
        document.add(preface1);

        Paragraph preface2 = new Paragraph();
        preface2.setAlignment(Paragraph.ALIGN_CENTER);
        preface2.add(new Phrase("DRAW NO : ", PDFCreator.MEDIUM_BOLD));
        preface2.add(new Phrase(draw_no + " ", PDFCreator.MEDIUM_BOLD));
        preface2.add(new Phrase(" DRAW DATE: ", PDFCreator.MEDIUM_BOLD));
        preface2.add(new Phrase(format.format(new Date()), PDFCreator.MEDIUM_BOLD));
        addEmptyLine(preface2, 1);
        document.add(preface2);

        Paragraph preface3 = new Paragraph();
        preface3.setAlignment(Paragraph.ALIGN_CENTER);
        preface3.add(new Phrase("WINNING NUMBERS ", PDFCreator.LARGE_BOLD));
        addEmptyLine(preface3, 1);
        document.add(preface3);

        Paragraph preface4 = new Paragraph();
        preface4.setAlignment(Paragraph.ALIGN_CENTER);
        preface4.add(new Phrase(p1, PDFCreator.LARGE_BOLD));
        addEmptyLine(preface4, 1);
        document.add(preface4);

        Paragraph preface5 = new Paragraph();
        preface5.setAlignment(Paragraph.ALIGN_CENTER);
        preface5.add(new Phrase("ENGLISH LETTER ", PDFCreator.LARGE_BOLD));
        addEmptyLine(preface5, 1);
        document.add(preface5);

        Paragraph preface6 = new Paragraph();
        preface6.setAlignment(Paragraph.ALIGN_CENTER);
        preface6.add(new Phrase(letter, PDFCreator.LARGE_BOLD));
        addEmptyLine(preface6, 1);
        document.add(preface6);

        //Properties prop = new Properties();
        //ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("test.properties");
        //InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties");
        brand_code=brand_code;



        try {
            getBrandProperties(brand_code);
            DecimalFormat formatter = new DecimalFormat("#,###");
            DecimalFormat fmt = new DecimalFormat("#,###.##");
            //prop.load(stream);
            start = getNoOfRecords(draw_no,brand_code);
            DeleteExistingRecords(draw_no,brand_code);

            System.out.println("START >>> " + start);
            start_no = start + start_no - 1;
            int act_sold_count = start_no + sold_count;

            System.out.println("SOLD >>> " + act_sold_count);

            j_count = get4NosLettterCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no, brand_code);
            count4No = get4NosCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count3No_L = get3NosLetrCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count3No = get3NosCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count2Nos_L = get2NosLetteCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count2No = get2NosCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count1NO_L = get1NoLetterCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count1No = get1NoCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            count_Letter = getLetterCount(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);
            total_count = j_count + count4No + count3No_L + count3No + count2Nos_L + count2No + count1NO_L + count1No + count_Letter;
            getWinnings(no1, no2, no3, no4, letter, draw_no, act_sold_count, start_no,brand_code);


            if (j_count == 0)
                next_jackpot = Integer.parseInt(txt4NOEngLetter.replaceAll(",", "")) + (act_sold_count * Integer.parseInt(txtTicketPrice) * Double.parseDouble(txtJackpotPercentage)) / 100;
            else
                next_jackpot = Double.parseDouble(txtJackpot);

            Paragraph preface7 = new Paragraph();
            preface7.add(Integer.toString(j_count));
            preface7.add(new Phrase("    TICKET(S) SOLD WITH 4 MATCHES & ENGLISH LETTER", PDFCreator.NORMAL_FONT));
            document.add(preface7);
            document.add(new Paragraph(""));

            Paragraph preface8 = new Paragraph();
            preface8.add(Integer.toString(count4No));
            preface8.add(new Phrase("    TICKET(S) SOLD WITH 4 MATCHES ", PDFCreator.NORMAL_FONT));
            addEmptyLine(preface8, 1);
            document.add(preface8);



            tot_j_count = j_count * Integer.parseInt(txt4NOEngLetter.replaceAll(",", ""));
            tot_count4No = count4No * Integer.parseInt(txt4NO.replaceAll(",", ""));
            tot_count3No_L = count3No_L * Integer.parseInt(txt3NOEngLetter.replaceAll(",", ""));
            tot_count3No = count3No * Integer.parseInt(txt3NO.replaceAll(",", ""));
            tot_count2Nos_L = count2Nos_L * Integer.parseInt(txt2NOEngLetter.replaceAll(",", ""));
            tot_count2No = count2No * Integer.parseInt(txt2NO.replaceAll(",", ""));
            tot_count1NO_L = count1NO_L * Integer.parseInt(txt1NOEngLetter.replaceAll(",", ""));
            tot_count1No = count1No * Integer.parseInt(txt1NO.replaceAll(",", ""));
            tot_count_Letter = count_Letter * Integer.parseInt(EngLetter.replaceAll(",", ""));
            sum_total_count = tot_j_count + tot_count4No + tot_count3No_L + tot_count3No + tot_count2Nos_L + tot_count2No + tot_count1NO_L + tot_count1No + tot_count_Letter;

            // Creating a table
            PdfPTable table = new PdfPTable(new float[]{30, 20, 25, 25});
            table.setTotalWidth(510f);//table size
            table.setWidthPercentage(100);//table size
            table.setLockedWidth(true);
            table.setSpacingBefore(10f); //both are used to mention the space from heading
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(0);
            table.addCell(createHdrLabelCell("    MATCHES"));
            table.addCell(createHdrLabelCell("   NO. OF WINNERS"));
            table.addCell(createHdrLabelCell("   PRIZE"));
            table.addCell(createHdrLabelCell("   AMOUNT"));


            //ADD CONTENT
            table.addCell(createLabelCell("4NOS, ENGLISH LETTER"));
            table.addCell(createLabelCell(formatter.format(j_count)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt4NOEngLetter))));
            table.addCell(createLabelCell(formatter.format(tot_j_count)));

            table.addCell(createLabelCell("4NOS"));
            table.addCell(createLabelCell(formatter.format(count4No)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt4NO))));
            table.addCell(createLabelCell(formatter.format(tot_count4No)));

            table.addCell(createLabelCell("3NOS, ENGLISH LETTER"));
            table.addCell(createLabelCell(formatter.format(count3No_L)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt3NOEngLetter))));
            table.addCell(createLabelCell(formatter.format(tot_count3No_L)));

            table.addCell(createLabelCell("3NOS"));
            table.addCell(createLabelCell(formatter.format(count3No)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt3NO))));
            table.addCell(createLabelCell(formatter.format(tot_count3No)));

            table.addCell(createLabelCell("2NOS, ENGLISH LETTER"));
            table.addCell(createLabelCell(formatter.format(count2Nos_L)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt2NOEngLetter))));
            table.addCell(createLabelCell(formatter.format(tot_count2Nos_L)));

            table.addCell(createLabelCell("2NOS"));
            table.addCell(createLabelCell(formatter.format((count2No))));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt2NO))));
            table.addCell(createLabelCell(formatter.format(tot_count2No)));

            table.addCell(createLabelCell("1NOS, ENGLISH LETTER"));
            table.addCell(createLabelCell(formatter.format(count1NO_L)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt1NOEngLetter))));
            table.addCell(createLabelCell(formatter.format(tot_count1NO_L)));

            table.addCell(createLabelCell("1NOS"));
            table.addCell(createLabelCell(formatter.format(count1No)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(txt1NO))));
            table.addCell(createLabelCell(formatter.format(tot_count1No)));

            table.addCell(createLabelCell("LETTER"));
            table.addCell(createLabelCell(formatter.format(count_Letter)));
            table.addCell(createLabelCell(formatter.format(Double.parseDouble(EngLetter))));
            table.addCell(createLabelCell(formatter.format(tot_count_Letter)));

            table.addCell(createFooterLabelCell("TOTAL"));
            table.addCell(createFooterLabelCell(formatter.format(total_count)));
            table.addCell(createFooterLabelCell(""));
            table.addCell(createFooterLabelCell(formatter.format(sum_total_count)));

            // Adding Table to document
            document.add(table);
            Paragraph preface10 = new Paragraph();
            preface10.setSpacingBefore(10f);
            preface10.setAlignment(Paragraph.ALIGN_CENTER);
            preface10.add(new Phrase("NEXT JACKPOT", PDFCreator.LARGE_BOLD));
            addEmptyLine(preface10, 1);
            document.add(preface10);

            Paragraph preface11 = new Paragraph();
            preface11.setAlignment(Paragraph.ALIGN_CENTER);
            preface11.add(new Phrase(formatter.format(next_jackpot), PDFCreator.LARGE_BOLD));
            addEmptyLine(preface11, 1);
            document.add(preface11);

            document.newPage();
            addEmptyLine(preface11, 3);
            Paragraph preface12 = new Paragraph();
            preface12.setAlignment(Paragraph.ALIGN_CENTER);
            preface12.add(new Phrase(brand+" "+"DRAW RESULT ANALYSIS", PDFCreator.LARGE_BOLD));
            document.add(preface12);

            tot_consignment=getBrandDetails(brand_code);

            System.out.println("tot_consignment"+tot_consignment);
            System.out.println("eng_letter_count"+eng_letter_count);

            per_day_count = Math.round(tot_consignment / eng_letter_count);
            //Create table with results
            // Creating a table
            PdfPTable table_res = new PdfPTable(new float[]{30, 20, 15, 15, 10, 10});
            table_res.setTotalWidth(510f);//table size
            table_res.setWidthPercentage(100);//table size
            table_res.setLockedWidth(true);
            table_res.setSpacingBefore(10f); //both are used to mention the space from heading
            table_res.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table_res.getDefaultCell().setBorder(0);

            table_res.addCell(createHdrLabelCell("    PRIZE MATCHES"));
            table_res.addCell(createHdrLabelCell("Total Consignment (" + formatter.format(tot_consignment) + ")"));
            table_res.addCell(createHdrLabelCell("Combinations per Day (" + formatter.format(per_day_count) + ")"));
            table_res.addCell(createHdrLabelCell("Expected Winnings (" + formatter.format(sold_count) + ")"));
            table_res.addCell(createHdrLabelCell("   Actual Winnings"));
            table_res.addCell(createHdrLabelCell("   Error %"));
            ltr_count = eng_letter_count;


            //`txt4NOEngLetter`, `txt4NO`, `txt3NOEngLetter`, `txt3NO`, `txt2NOEngLetter`, `txt2NO`, `txt1NOEngLetter`, `txt1NO`, `EngLetter`, `txtTicketPrice`, `txtJackpotPercentage`, `txtJackpot`,
            // `txt4NOEngLetterWin`, `txt4NOWin`, `txt3NOEngLetterWin`, `txt3NOWin`, `txt2NOEngLetterWin`, `txt2NOWin`, `txt1NOEngLetterWin`, `txt1NOWin`, `EngLetterWin`,



            //ADD CONTENT
            /**JACKPOT**/
            table_res.addCell(createLabelCell("4NOS, ENGLISH LETTER"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt4NOEngLetterWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt4NOEngLetterWin) / tot_consignment) * per_day_count))));
            // table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("4NOS_LETTER_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_jackpot = Math.round((Double.parseDouble(txt4NOEngLetterWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_jackpot)));
            table_res.addCell(createLabelCell(formatter.format(j_count)));
            if (exp_jackpot > 0)
                table_res.addCell(createLabelCell(fmt.format((j_count - exp_jackpot) / exp_jackpot * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));

            /**4NOS**/
            table_res.addCell(createLabelCell("4NOS"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt4NOWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt4NOWin) / tot_consignment) * per_day_count))));
            double exp_4NoCont = Math.round((Double.parseDouble(txt4NOWin) / tot_consignment) * sold_count);
            // table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("4NOS_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            table_res.addCell(createLabelCell(formatter.format(exp_4NoCont)));
            table_res.addCell(createLabelCell(formatter.format(count4No)));
            if (exp_4NoCont > 0)
                table_res.addCell(createLabelCell(fmt.format((count4No - exp_4NoCont) / exp_4NoCont * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));



            /**3NOS+LETTER**/
            table_res.addCell(createLabelCell("3NOS, ENGLISH LETTER"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt3NOEngLetterWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt3NOEngLetterWin) / tot_consignment) * per_day_count))));
            //table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("3NOS_LETTER_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_3NoLet = Math.round((Double.parseDouble(txt3NOEngLetterWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_3NoLet)));
            table_res.addCell(createLabelCell(formatter.format(count3No_L)));
            if (exp_3NoLet > 0)
                table_res.addCell(createLabelCell(fmt.format((count3No_L - exp_3NoLet) / exp_3NoLet * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));



            /**3NOS**/
            table_res.addCell(createLabelCell("3NOS"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt3NOWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt3NOWin) / tot_consignment) * per_day_count))));
            //table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("3NOS_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_3No = Math.round((Double.parseDouble(txt3NOWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_3No)));
            table_res.addCell(createLabelCell(formatter.format(count3No)));
            if (exp_3No > 0)
                table_res.addCell(createLabelCell(fmt.format((count3No - exp_3No) / exp_3No * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));



            /**2NOS+LETTER**/
            table_res.addCell(createLabelCell("2NOS, ENGLISH LETTER"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt2NOEngLetterWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt2NOEngLetterWin) / tot_consignment) * per_day_count))));
            // table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("2NOS_LETTER_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_2NoLet = Math.round((Double.parseDouble(txt2NOEngLetterWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_2NoLet)));
            table_res.addCell(createLabelCell(formatter.format(count2Nos_L)));
            if (exp_2NoLet > 0)
                table_res.addCell(createLabelCell(fmt.format((count2Nos_L - exp_2NoLet) / exp_2NoLet * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));

            /**2NOS**/
            table_res.addCell(createLabelCell("2NOS"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt2NOWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt2NOWin) / tot_consignment) * per_day_count))));
            //table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("2NOS_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_2Nos = Math.round((Double.parseDouble(txt2NOWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_2Nos)));
            table_res.addCell(createLabelCell(formatter.format(count2No)));
            if (exp_2Nos > 0)
                table_res.addCell(createLabelCell(fmt.format((count2No - exp_2Nos) / exp_2Nos * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));

            // `txt4NOEngLetterWin`, `txt4NOWin`, `txt3NOEngLetterWin`, `txt3NOWin`, `txt2NOEngLetterWin`, `txt2NOWin`, `txt1NOEngLetterWin`, `txt1NOWin`, `EngLetterWin`,


            /**1NOS+LETTER**/
            table_res.addCell(createLabelCell("1NOS, ENGLISH LETTER"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt1NOEngLetterWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt1NOEngLetterWin) / tot_consignment) * per_day_count))));
            //table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("1NO_LETTER_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_1NOLet = Math.round((Double.parseDouble(txt1NOEngLetterWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_1NOLet)));
            table_res.addCell(createLabelCell(formatter.format(count1NO_L)));
            if (exp_1NOLet > 0)
                table_res.addCell(createLabelCell(fmt.format((count1NO_L - exp_1NOLet) / exp_1NOLet * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));


            /**1NOS**/
            table_res.addCell(createLabelCell("1NOS"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(txt1NOWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(txt1NOWin) / tot_consignment) * per_day_count))));
            //table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("1NO_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_1no = Math.round((Double.parseDouble(txt1NOWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_1no)));
            table_res.addCell(createLabelCell(formatter.format(count1No)));
            if (exp_1no > 0)
                table_res.addCell(createLabelCell(fmt.format((count1No - exp_1no) / exp_1no * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));

            /**LETTER**/
            table_res.addCell(createLabelCell("ENGLISH LETTER"));
            table_res.addCell(createLabelCell(formatter.format(Double.parseDouble(EngLetterWin))));
            table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(EngLetterWin) / tot_consignment) * per_day_count))));
            // table_res.addCell(createLabelCell(formatter.format(Math.round((Double.parseDouble(prop.getProperty("LETTER_WINNINGS"))/Double.parseDouble(prop.getProperty("TOTAL_COSIGNMENT")))*sold_count))));
            double exp_let = Math.round((Double.parseDouble(EngLetterWin) / tot_consignment) * sold_count);
            table_res.addCell(createLabelCell(formatter.format(exp_let)));

            table_res.addCell(createLabelCell(formatter.format(count_Letter)));
            if (exp_let > 0)
                table_res.addCell(createLabelCell(fmt.format((count_Letter - exp_let) / exp_let * 100)));
            else
                table_res.addCell(createLabelCell(formatter.format(0)));


            // Adding Table to document
            document.add(table_res);


            // Closing the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            savePrizeCount(draw_no,brand_code);
        }
    }

    public static void getBrandProperties(String brand_code){
        try {
            connection= DBConnection.getConnection();
            stmt =connection.createStatement();

            String query=  "SELECT * FROM `no_letter_prize_structure` WHERE brand_code='"+brand_code+"'";

            rs=stmt.executeQuery(query);

            while (rs.next()){
                txt4NOEngLetter=rs.getString("txt4NOEngLetter");
                txt4NO=rs.getString("txt4NO");
                txt3NOEngLetter=rs.getString("txt3NOEngLetter");
                txt3NO=rs.getString("txt3NO");
                txt2NOEngLetter=rs.getString("txt2NOEngLetter");
                txt2NO=rs.getString("txt2NO");
                txt1NOEngLetter=rs.getString("txt1NOEngLetter");
                txt1NO=rs.getString("txt1NO");
                EngLetter=rs.getString("EngLetter");
                txtTicketPrice=rs.getString("txtTicketPrice");
                txtJackpotPercentage=rs.getString("txtJackpotPercentage");
                txtJackpot=rs.getString("txtJackpot");
                txt4NOEngLetterWin=rs.getString("txt4NOEngLetterWin");
                txt4NOWin=rs.getString("txt4NOWin");
                txt3NOEngLetterWin=rs.getString("txt3NOEngLetterWin");
                txt3NOWin=rs.getString("txt3NOWin");
                txt2NOEngLetterWin=rs.getString("txt2NOEngLetterWin");
                txt2NOWin=rs.getString("txt2NOWin");
                txt1NOEngLetterWin=rs.getString("txt1NOEngLetterWin");
                txt1NOWin=rs.getString("txt1NOWin");
                EngLetterWin=rs.getString("EngLetterWin");
            }
            getBrandDetails(brand_code);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static int getBrandDetails(String brand_code){

        System.out.println("BCODE:"+brand_code);
        eng_letter_count=0;
        String symbols="";
        int no_of_balls=0;
        int selection=4;
        try {
            connection= DBConnection.getConnection();
            stmt =connection.createStatement();

            String query=  "SELECT * FROM `tbl_lottery_det` WHERE brand_code='"+brand_code+"'";

            System.out.println("QQQQQ:"+query);
            rs=stmt.executeQuery(query);

            if (rs.next()){
                System.out.println("QQQQQAA:"+rs.getInt(4));
                no_of_balls=rs.getInt(4);
                symbols=rs.getString(5);
                if(symbols.equals("English Letters"))
                    eng_letter_count=26;


                tot_consignment=printNcR(no_of_balls, selection)*eng_letter_count;
                System.out.println("PRINT :tot_consignment11111 :"+tot_consignment);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("PRINT :tot_consignment :"+tot_consignment);
        return tot_consignment;
    }


    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * This function is used to save the prize count per ticket book
     */

    public static void savePrizeCount(int draw_no,String brand_code) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();


            String QUERY1 = "INSERT INTO `"+brand_code+"_prize_count` SELECT book_no, count(rec_no), draw_no from `"+brand_code+"_prize_det` WHERE rec_no <> 0 AND draw_no=" + draw_no + " GROUP BY book_no ";
            stmt.execute(QUERY1);

            String QUERY2 = "INSERT INTO `"+brand_code+"_prize_count` SELECT book_no, 0, draw_no from `"+brand_code+"_prize_det` WHERE rec_no = 0 AND draw_no=" + draw_no + " GROUP BY book_no ";
            stmt.execute(QUERY2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHeaderInTable(String[] headerArray, PdfPTable table) {
        PdfPCell c1 = null;
        for (String header : headerArray) {
            c1 = new PdfPCell(new Phrase(header, PDFCreator.SMALL_BOLD));
            c1.setBackgroundColor(BaseColor.GREEN);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        table.setHeaderRows(1);
    }

    public static void labelCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);
        // border
        cell.setBorder(0);

        // height
        cell.setMinimumHeight(18f);
    }

    // create cells
    private static PdfPCell createLabelCell(String text) {
        // font
        Font font = new Font(PDFCreator.SMALL_BOLD);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        labelCellStyle(cell);
        return cell;
    }

    //create Header style
    public static void headerCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);
        // border
        cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);

        // height
        cell.setMinimumHeight(18f);
    }

    //create Header style
    public static void footerCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_RIGHT);
        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);
        // border
        cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);

        // height
        cell.setMinimumHeight(18f);
    }

    // create header Footer cells
    private static PdfPCell createHdrLabelCell(String text) {
        // font
        Font font = new Font(PDFCreator.MEDIUM_BOLD);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        headerCellStyle(cell);
        return cell;
    }

    // create header Footer cells
    private static PdfPCell createFooterLabelCell(String text) {
        // font
        Font font = new Font(PDFCreator.MEDIUM_BOLD);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        footerCellStyle(cell);
        return cell;
    }


    public static void addToTable(PdfPTable table, String data) {
        table.addCell(new Phrase(data, PDFCreator.NORMAL_FONT));
    }

    public static Paragraph getParagraph() {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(PDFCreator.NORMAL_FONT);
        addEmptyLine(paragraph, 1);
        return paragraph;
    }


    /**
     * Function to calculate Number of winning records
     */
    public static int getNoOfRecords(int draw_no,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int start = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();

            stmt1 = conn.createStatement();
            /****start***/
            String SELECT_RESULT = "SELECT rec_count FROM `"+brand_code+"_combinations`  WHERE draw_no=" + draw_no + " LIMIT 1";
            System.out.println("First Row>>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                start = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return start;
    }


    /**
     * Function to calculate Number of Winnings for 4NOS+LETTER Winnings
     */
    public static int get4NosLettterCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();

            stmt1 = conn.createStatement();
            /****Jackpot***/
            String SELECT_RESULT = "SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations`  WHERE no1=" + num1 + " AND no2=" + num2 + " AND no3=" + num3 + " AND no4=" + num4 + " AND eng_letter='" + let + "' AND draw_no=" + draw_no + " AND rec_count < " + soldCount + " AND rec_count > " + start;
            System.out.println("Q 9 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;
    }


    /**
     * Function to calculate Number of Winnings for 4NOS Winnings
     */
    public static int get4NosCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations`  WHERE no1=" + num1 + " AND no2=" + num2 + " AND no3=" + num3 + " AND no4=" + num4 + " AND eng_letter<>'" + let + "' AND draw_no=" + draw_no + " AND rec_count < " + soldCount + " AND rec_count > " + start;
            System.out.println("Q 8 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }

    /**
     * Function to calculate Number of Winnings for 3NOS+LETTER Winnings
     */
    public static int get3NosLetrCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();


            String SELECT_RESULT = "SELECT SUM(c)  FROM(" +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count < " + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count < " + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count < " + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count) as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + "  AND rec_count < " + soldCount + "  AND rec_count > " + start + ")c ";

            System.out.println("Q 7 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);
            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;
    }

    /**
     * Function to calculate Number of Winnings for 3NOS Winnings
     */
    public static int get3NosCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT SUM(c)  FROM(" +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count) as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start + ")c";


            System.out.println("Q 6 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);
            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;
    }

    /**
     * Function to calculate Number of Winnings for 2NOS+LETTER Winnings
     */
    public static int get2NosLetteCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            /****Jackpot***/
            String SELECT_RESULT = " SELECT SUM(c)  FROM( " +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start + ")c ";
            System.out.println("Q 5 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }


    /**
     * Function to calculate Number of Winnings for 2NOS Winnings
     */
    public static int get2NosCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT SUM(c)  FROM(" +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start + ") c";

            System.out.println("Q 4 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }

    /**
     * Function to calculate Number of Winnings for 1NOS+LETTER Winnings
     */
    public static int get1NoLetterCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT SUM(c)  FROM(" +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start + ")c ";
            System.out.println("Q 3 >>> " + SELECT_RESULT);

            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }

    /**
     * Function to calculate Number of Winnings for 1NOS Winnings
     */
    public static int get1NoCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT SUM(c)  FROM(" +
                    " SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start + ")c ";
            System.out.println("Q 2 >>> " + SELECT_RESULT);

            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);


            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }

    /**
     * Function to calculate Number of Winnings for Letter Winnings
     */
    public static int getLetterCount(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {
        Connection conn = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        int j_count = 0;
        try {
            if (conn == null)
                conn = DBConnection.getConnection();
            stmt1 = conn.createStatement();

            String SELECT_RESULT = "SELECT COUNT(rec_count)  as c FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;
            System.out.println("Q 1 >>> " + SELECT_RESULT);
            if (stmt1 == null)
                System.out.println("stmt   : " + stmt1);

            rs1 = stmt1.executeQuery(SELECT_RESULT);
            while (rs1.next()) {
                j_count = rs1.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j_count;

    }

    /**
     * This function used to analyze sold ticket prizes of specific draw
     *
     * @param num1
     * @param num2
     * @param num3
     * @param num4
     * @param let
     * @param draw_no
     * @param soldCount
     * @throws IOException
     */
    public static void getWinnings(int num1, int num2, int num3, int num4, String let, int draw_no, int soldCount, int start,String brand_code)
            throws IOException {

        Connection conn = null;
        Statement stmt1, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9;
        stmt1 = stmt2 = stmt3 = stmt4 = stmt5 = stmt6 = stmt7 = stmt8 = stmt9 = null;

        List jackpots = new ArrayList();
        List no4 = new ArrayList();
        List no3Let = new ArrayList();
        List no3 = new ArrayList();
        List no2Let = new ArrayList();
        List no2 = new ArrayList();
        List no1Let = new ArrayList();
        List no1 = new ArrayList();
        List lettr = new ArrayList();
        ResultSet rs, rs1, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rs9;
        rs1 = rs2 = rs3 = rs4 = rs5 = rs6 = rs7 = rs8 = rs9 = null;

        try {
            System.out.println("---------------Analyze Consignment of Draw No: " + draw_no + "---------------");

            if (conn == null)
                conn = DBConnection.getConnection();

            stmt1 = conn.createStatement();
            stmt2 = conn.createStatement();
            stmt3 = conn.createStatement();
            stmt4 = conn.createStatement();
            stmt5 = conn.createStatement();
            stmt6 = conn.createStatement();
            stmt7 = conn.createStatement();
            stmt8 = conn.createStatement();
            stmt9 = conn.createStatement();

            /****Jackpot***/
            String SELECT_RESULT = "SELECT book_no, rec_count FROM `"+brand_code+"_combinations`  WHERE no1=" + num1 + " AND no2=" + num2 + " AND no3=" + num3 + " AND no4=" + num4 + " AND eng_letter='" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;
            System.out.println(SELECT_RESULT);
            rs1 = stmt1.executeQuery(SELECT_RESULT);

            while (rs1.next()) {
                jackpots.add(rs1.getInt(1) + " " + rs1.getInt(2));
            }

            System.out.println("1. Jackpot Count      : " + jackpots.size());

            saveWinningCountPerBook(jackpots, "JKP", draw_no,brand_code);

            /*************4Nos************/
            SELECT_RESULT = null;
            SELECT_RESULT = "SELECT book_no, rec_count FROM `"+brand_code+"_combinations`  WHERE no1=" + num1 + " AND no2=" + num2 + " AND no3=" + num3 + " AND no4=" + num4 + " AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;
            System.out.println(SELECT_RESULT);
            rs2 = stmt2.executeQuery(SELECT_RESULT);
            while (rs2.next()) {
                no4.add(rs2.getInt(1) + " " + rs2.getInt(2));
                System.out.println(rs2.getInt(1) + " ---- " + rs2.getInt(2));
            }
            System.out.println("2. 4NO count:" + no4.size());

            saveWinningCountPerBook(no4, "NO4", draw_no,brand_code);


            /****3Nos + Letter****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter ='" + let + "'AND draw_no=" + draw_no + "  AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + "  AND rec_count <" + soldCount + " AND rec_count > " + start;


            System.out.println(SELECT_RESULT);
            rs3 = stmt3.executeQuery(SELECT_RESULT);
            while (rs3.next()) {
                System.out.println(rs3.getInt(1) + " ---- " + rs3.getInt(2));
                no3Let.add(rs3.getInt(1) + " " + rs3.getInt(2));
            }
            System.out.println("3. 3Nos + English Letter Count      : " + no3Let.size());
            saveWinningCountPerBook(no3Let, "NO3LET", draw_no,brand_code);

            /****3Nos****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> 'L' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> 'L' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> 'L' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> 'L' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs4 = stmt4.executeQuery(SELECT_RESULT);
            while (rs4.next()) {

                no3.add(rs4.getInt(1) + " " + rs4.getInt(2));
            }
            saveWinningCountPerBook(no3, "NO3", draw_no,brand_code);
            System.out.println("3Nos Count      : " + no3.size());

            /****2Nos+Letter****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no ,rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "'  AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs5 = stmt5.executeQuery(SELECT_RESULT);
            while (rs5.next()) {

                no2Let.add(rs5.getInt(1) + " " + rs5.getInt(2));
            }
            saveWinningCountPerBook(no2Let, "NO2LET", draw_no,brand_code);
            System.out.println("4. 2Nos + Letter Count      : " + no2Let.size());

            /****2Nos****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs6 = stmt6.executeQuery(SELECT_RESULT);
            while (rs6.next()) {

                no2.add(rs6.getInt(1) + " " + rs6.getInt(2));
            }
            saveWinningCountPerBook(no2, "NO2", draw_no,brand_code);
            System.out.println("5. 2Nos Count      : " + no2.size());

            /****1No+Letter****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs7 = stmt7.executeQuery(SELECT_RESULT);
            while (rs7.next()) {
                no1Let.add(rs7.getInt(1) + " " + rs7.getInt(2));
            }
            saveWinningCountPerBook(no1Let, "NO1LET", draw_no,brand_code);
            System.out.println("6. 1No Count + Letter     : " + no1Let.size());

            /****1No*****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start +
                    " UNION ALL SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter <> '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs8 = stmt8.executeQuery(SELECT_RESULT);
            while (rs8.next()) {

                no1.add(rs8.getInt(1) + " " + rs8.getInt(2));
            }
            saveWinningCountPerBook(no1, "NO1", draw_no,brand_code);
            System.out.println("7. 1No Count   : " + no1.size());

            /****Letter*****/
            SELECT_RESULT = null;
            SELECT_RESULT = " SELECT book_no, rec_count FROM `"+brand_code+"_combinations` WHERE no1 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no2 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no3 NOT IN (" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND no4 NOT IN(" + num1 + "," + num2 + "," + num3 + "," + num4 + ") AND eng_letter = '" + let + "' AND draw_no=" + draw_no + " AND rec_count <" + soldCount + " AND rec_count > " + start;

            System.out.println(SELECT_RESULT);
            rs9 = stmt9.executeQuery(SELECT_RESULT);
            while (rs9.next()) {

                lettr.add(rs9.getInt(1) + " " + rs9.getInt(2));
            }
            saveWinningCountPerBook(lettr, "LETTER", draw_no,brand_code);
            System.out.println("8. Letter     : " + lettr.size());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                String INSERT_RECORD = "INSERT INTO "+brand_code+"_prize_det (book_no,rec_no,prize_code,draw_no)Select DISTINCT book_no,0,'ZERO',draw_no FROM "+brand_code+"_combinations where draw_no=" + draw_no + " AND book_no NOT IN(SELECT book_no FROM "+brand_code+"_prize_det where draw_no=" + draw_no + ")";
                PreparedStatement ps = conn.prepareStatement(INSERT_RECORD);
                ps.executeUpdate();

                String WINNING_DATA = "INSERT INTO `"+brand_code+"_winning_details`(`draw_no`, `no1`, `no2`, `no3`, `no4`, `eng_letter`) VALUES (?,?,?,?,?,?)";
                PreparedStatement ps1 = conn.prepareStatement(WINNING_DATA);
                ps1.setInt(1, draw_no);
                ps1.setInt(2, num1);
                ps1.setInt(3, num2);
                ps1.setInt(4, num3);
                ps1.setInt(5, num4);
                ps1.setString(6, let);
                ps1.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(conn);
        }
    }

    public static void saveWinningCountPerBook(List winnigData, String prizeCode, int draw_no,String brand_code) {
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {

            if (conn == null)
                conn = DBConnection.getConnection();

           /* String DELETE_RECORDS= "DELETE FROM `prize_det` WHERE draw_no="+draw_no;
            ps = conn.prepareStatement(DELETE_RECORDS);
            ps.executeUpdate();

            String DELETE_RECORD= "DELETE FROM `prize_count` WHERE draw_no="+draw_no;
            ps = conn.prepareStatement(DELETE_RECORD);
            ps.executeUpdate();*/


            String INSERT_RECORD = "INSERT INTO `"+brand_code+"_prize_det`(`book_no`, `rec_no`, `prize_code`,`draw_no`) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(INSERT_RECORD);

            for (int i = 0; i < winnigData.size(); i++) {
                String[] parts = winnigData.get(i).toString().split(" ");
                ps.setInt(1, Integer.parseInt(parts[0]));
                ps.setInt(2, Integer.parseInt(parts[1]));
                ps.setString(3, prizeCode);
                ps.setInt(4, draw_no);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DeleteExistingRecords(int draw_no,String brand_code) {
        Connection conn = null;
        PreparedStatement ps,ps1 = null;

        try {
            if (conn == null)
                conn = DBConnection.getConnection();

            String DELETE_RECORDS = "DELETE FROM `"+brand_code+"_prize_det` WHERE draw_no=" + draw_no;
            ps = conn.prepareStatement(DELETE_RECORDS);
            ps.executeUpdate();

            String DELETE_RECORD = "DELETE FROM `"+brand_code+"_prize_count` WHERE draw_no=" + draw_no;
            ps1 = conn.prepareStatement(DELETE_RECORD);
            ps1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}