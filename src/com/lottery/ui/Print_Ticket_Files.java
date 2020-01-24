package com.lottery.ui;

import com.lottery.db.DBConnection;

import java.util.Scanner;

import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class Print_Ticket_Files {
    Connection conn = null;
    Statement stmt=null;
    ResultSet rs =null;

    public void print_tickets(String brand_code, int draw_no){
        try {

            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            String query = "SELECT rec_count,no1,no2,no3,no4,eng_letter FROM `"+brand_code+"_combinations` WHERE draw_no="+draw_no+"";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            FileWriter writer = new FileWriter("TICKET_FILES/DLB_"+brand_code+"_"+draw_no+".txt");
            while (rs.next()) {
                writer.write(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6)+"\n");
            }

            if(writer != null)
                writer.close();


        } catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}


