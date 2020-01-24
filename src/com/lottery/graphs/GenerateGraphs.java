package com.lottery.graphs;

import com.lottery.db.DBConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * CREATED BY:ANURUDDHIKA
 * DATE:2019/07/09
 * For: DISPLAY THE GRAPHICAL REPRESENTATION OF PRIZE DISTRIBUTION in LINEAR GRAPH
 * USE: jFreeChart Open source Library for generate graphs
 * */

public class GenerateGraphs extends JFrame{

    //public GenerateGraphs() {   // the constructor will contain the panel of a certain size and the close operations
        //super("XY Line Chart Example with JFreechart");
    //}

        public GenerateGraphs(String brand_code, int draw_no,String brand_name) {   // the constructor will contain the panel of a certain size and the close operations
            super("XY Line Chart Example with JFreechart"); // calls the super class constructor

            try{
                int count=0;
                int bk_count=0;

                int chrt_count=0;
                Connection conn = null;
                conn = DBConnection.getConnection();
                Statement stmt1=null;
                ResultSet rs1=null;
                stmt1=conn.createStatement();

               // Scanner kbd= new Scanner(System.in);
               // System.out.println();
                //int draw_no=0;
                //System.out.println("Enter Required Draw Number:");
               // draw_no=kbd.nextInt();

                /**
                * This query is for count the ticket books which are having the winnings(at least one)
                * */
                String query="SELECT COUNT(book_no) FROM `"+brand_code+"_prize_count` WHERE draw_no= "+draw_no+"" ;
                rs1=stmt1.executeQuery(query);

                while(rs1.next()){
                    count=rs1.getInt(1);
                }

           for(int i=0; i<count;i++) {
               bk_count++;

               if(bk_count == 100){
                   chrt_count++;
                   JPanel chartPanel = createChartPanel(chrt_count,draw_no,brand_code,brand_name);
                   add(chartPanel, BorderLayout.CENTER);
                   bk_count=0;
               }
           }



            setSize(640, 480);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private JPanel createChartPanel(int no, int draw_no,String brand_code,String brand_name) { // this method will create the chart panel containin the graph
            String chartTitle = brand_name+" Prize Distribution Over the Book Number - DRAW NO:"+draw_no;
            String xAxisLabel = "Book NO";
            String yAxisLabel = "# of Winnings";

            XYDataset dataset = createDataset(no,draw_no,brand_code);

            JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                    xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,true,true,true);

            customizeChart(chart);

            // saves the chart as an image files
            File imageFile = new File("GRAPHS/"+brand_code+"_"+draw_no+"_XYLineChart"+no+".png");
            int width = 640;
            int height = 480;

            try {
                ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
            } catch (IOException ex) {
                System.err.println(ex);
            }

            return new ChartPanel(chart);
        }

        private XYDataset createDataset(int no, int draw_no, String brand_code) {    // this method creates the data as time seris
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series1 = new XYSeries("Winning Count");

            Connection conn = null;
            Statement stmt1;
            stmt1 = null;
            ResultSet rs1;
            rs1=null;
            int start=0;
            int end=0;

            try{
                start= (no-1)*100;
                end =no*100;

                if (conn == null)
                    conn = DBConnection.getConnection();

                stmt1 = conn.createStatement();


                String SELECT_RESULT = "SELECT book_no, rec_count FROM `"+brand_code+"_prize_count` WHERE draw_no ="+draw_no+" AND book_no > "+start+" AND book_no <= "+end+" ORDER BY book_no";


                System.out.println(SELECT_RESULT);

                rs1 = stmt1.executeQuery(SELECT_RESULT);

                while (rs1.next()) {
                    series1.add(rs1.getInt(1),rs1.getInt(2));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            dataset.addSeries(series1);

            return dataset;
        }

        private void customizeChart(JFreeChart chart) {   // here we make some customization
            XYPlot plot = chart.getXYPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

            // sets paint color for each series
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.GREEN);
            renderer.setSeriesPaint(2, Color.YELLOW);
            renderer.setSeriesPaint(3, Color.BLUE);
            renderer.setSeriesPaint(4, Color.PINK);
            renderer.setSeriesPaint(5, Color.DARK_GRAY);
            renderer.setSeriesPaint(6, Color.MAGENTA);
            renderer.setSeriesPaint(7, Color.ORANGE);
            renderer.setSeriesPaint(8, Color.WHITE);

            // sets thickness for series (using strokes)
            renderer.setSeriesStroke(0, new BasicStroke(1.0f));
            renderer.setSeriesStroke(1, new BasicStroke(3.0f));
            renderer.setSeriesStroke(2, new BasicStroke(2.0f));
            renderer.setSeriesStroke(3, new BasicStroke(3.0f));
            renderer.setSeriesStroke(4, new BasicStroke(3.0f));
            renderer.setSeriesStroke(5, new BasicStroke(2.0f));
            renderer.setSeriesStroke(6, new BasicStroke(2.0f));
            renderer.setSeriesStroke(7, new BasicStroke(3.0f));
            renderer.setSeriesStroke(8, new BasicStroke(2.0f));

            // sets paint color for plot outlines
            plot.setOutlinePaint(Color.BLUE);
            plot.setOutlineStroke(new BasicStroke(2.0f));

            // sets renderer for lines
            plot.setRenderer(renderer);

            // sets plot background
            plot.setBackgroundPaint(Color.DARK_GRAY);

            // sets paint color for the grid lines
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.BLACK);

            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.BLACK);

        }

        public void loadGraphs(){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //new GenerateGraphs().setVisible(true);
                }
            });
        }
}
