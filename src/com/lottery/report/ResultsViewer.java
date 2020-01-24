package com.lottery.report;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResultsViewer {

    public static final String PDF_EXTENSION = ".pdf";

    public static void  printReport(int n1, int n2, int n3, int n4,String ltr,int draw_no,String title,int start,int end,String brand_code) {


        String TITLE = title+"_RESULT";
        String pdf_name="";

        Document document = null;
        /*Scanner kbd = new Scanner(System.in);

        System.out.println("Enter Draw Number:");
        int draw_no = kbd.nextInt();
        System.out.println("Enter winning Number 01:");
        int n1 = kbd.nextInt();
        System.out.println("Enter winning Number 02:");
        int n2 = kbd.nextInt();
        System.out.println("Enter winning Number 03:");
        int n3 = kbd.nextInt();
        System.out.println("Enter winning Number 04:");
        int n4 = kbd.nextInt();
        System.out.println("Enter English Letter:");
        String ltr = kbd.next();*/

/*
        int draw_no=4;
        ArrayList numbers = new ArrayList();
        for(int i = 0; i < 62; i++)
        {
            numbers.add(i+1);
        }
        Collections.shuffle(numbers);

        int n1=Integer.parseInt(numbers.get(0).toString());
        int n2=Integer.parseInt(numbers.get(1).toString());
        int n3=Integer.parseInt(numbers.get(2).toString());
        int n4=Integer.parseInt(numbers.get(3).toString());

        ArrayList letters=new ArrayList();
        char ch;
        for (ch = 'A'; ch <= 'L'; ch++)
            letters.add(ch);
        Collections.shuffle(letters);
        String ltr= letters.get(0).toString();*/


        int s_count = end;

        /*if (n1 > 75 || n2 > 75 || n3 > 75 || n4 > 75) {
            System.out.println("Invalid Input");
        }*/

        int[] num = {n1, n2, n3, n4};

        Arrays.sort(num);

        n1 = num[0];
        n2 = num[1];
        n3 = num[2];
        n4 = num[3];


        try {
            //Document is not auto-closable hence need to close it separately
            document = new Document(PageSize.A4);
            pdf_name=draw_no+"_" +TITLE+ PDF_EXTENSION;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
                    new File(pdf_name)));
            HeaderFooter event = new HeaderFooter();
            event.setHeader(title);
            writer.setPageEvent(event);
            document.open();
            PDFCreator.addMetaData(document, TITLE);
            PDFCreator.addTitlePage(document, title, n1, n2, n3, n4, ltr, draw_no, s_count, start, title,brand_code);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FileNotFoundException occurs.." + e.getMessage());
        } finally {
            if (null != document) {
                document.close();
            }
        }

        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(pdf_name);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }
}

