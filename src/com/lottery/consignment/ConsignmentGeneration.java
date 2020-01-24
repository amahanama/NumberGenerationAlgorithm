package com.lottery.consignment;

import java.io.IOException;
import java.util.*;


public class ConsignmentGeneration {
    public static  int unordered_count=0;


    public int run(int nValue, int rValue,int numOfLettrs,int numOfSigns,int fate_no,int latestDrawNo,String brand_code) {
        // record start time for calculating the total time taken for the job.
        long startTime = System.currentTimeMillis();
        System.out.println( "Job started!!!" );
        int status=0;

        int n= nValue;
        int[] numbersArray=new int[n];
        int r = rValue;
        int c=0;

        for(int i=0; i<n; i++){
            c++;
            numbersArray[i]=c;
        }
        System.out.println( "n=" + n + " ,r=" + r );

        /*
         * following method will calculate all possible combinations of nCr and their md5 hash values.
         * Map key = number combintion, Map value = corresponding md5 hash value (32 long)
         * Ex: Key = "10 20 30 40" Value = "e50b1fb783847baef965cc6a5af7b83"
         *
         * when n=75 and r=4, 75C4 = 1,215,450. Thus map will contain 1,215,450 entries.
         * */
        Map<String, String> combinationsMap = Utils.getAllCombinations( numbersArray, n, r );
        System.out.println( "Total number of Combinations = " + combinationsMap.size() );

        /*Now sort the Map by md5 hash values. It will unformly distribute 'number combvinations' across the map*/
        Map<String, String> shuffledCombinationsMap = Utils.sortByValue( combinationsMap );
        /*We don't need md5 hash values anymore. Therefore collect 'number combinations' to a separate map*/
        List<String> shuffledCombinationsList = new ArrayList<>( shuffledCombinationsMap.keySet() );

        if(numOfLettrs == 26) {
            List<String> day1 = new ArrayList<>();
            List<String> day2 = new ArrayList<>();
            List<String> day3 = new ArrayList<>();
            List<String> day4 = new ArrayList<>();
            List<String> day5 = new ArrayList<>();
            List<String> day6 = new ArrayList<>();
            List<String> day7 = new ArrayList<>();
            List<String> day8 = new ArrayList<>();
            List<String> day9 = new ArrayList<>();
            List<String> day10 = new ArrayList<>();
            List<String> day11 = new ArrayList<>();
            List<String> day12 = new ArrayList<>();
            List<String> day13 = new ArrayList<>();
            List<String> day14 = new ArrayList<>();
            List<String> day15 = new ArrayList<>();
            List<String> day16 = new ArrayList<>();
            List<String> day17 = new ArrayList<>();
            List<String> day18 = new ArrayList<>();
            List<String> day19 = new ArrayList<>();
            List<String> day20 = new ArrayList<>();
            List<String> day21 = new ArrayList<>();
            List<String> day22 = new ArrayList<>();
            List<String> day23 = new ArrayList<>();
            List<String> day24 = new ArrayList<>();
            List<String> day25 = new ArrayList<>();
            List<String> day26 = new ArrayList<>();


            List<String> letterArray = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
            // List<String> letterArray = Arrays.asList( "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");

            // Now multiply 1.2 millions records with 26 English letters and pupulate 26 lists (1 list for each day).
            for (String combination : shuffledCombinationsList) {
                Collections.shuffle(letterArray);
                for (int i = 0; i < letterArray.size(); i++) {
                    switch (i) {
                        case 0:
                            day1.add(letterArray.get(i) + " " + combination);
                            break;
                        case 1:
                            day2.add(letterArray.get(i) + " " + combination);
                            break;
                        case 2:
                            day3.add(letterArray.get(i) + " " + combination);
                            break;
                        case 3:
                            day4.add(letterArray.get(i) + " " + combination);
                            break;
                        case 4:
                            day5.add(letterArray.get(i) + " " + combination);
                            break;
                        case 5:
                            day6.add(letterArray.get(i) + " " + combination);
                            break;
                        case 6:
                            day7.add(letterArray.get(i) + " " + combination);
                            break;
                        case 7:
                            day8.add(letterArray.get(i) + " " + combination);
                            break;
                        case 8:
                            day9.add(letterArray.get(i) + " " + combination);
                            break;
                        case 9:
                            day10.add(letterArray.get(i) + " " + combination);
                            break;
                        case 10:
                            day11.add(letterArray.get(i) + " " + combination);
                            break;
                        case 11:
                            day12.add(letterArray.get(i) + " " + combination);
                            break;
                    case 12:
                        day13.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 13:
                        day14.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 14:
                        day15.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 15:
                        day16.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 16:
                        day17.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 17:
                        day18.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 18:
                        day19.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 19:
                        day20.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 20:
                        day21.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 21:
                        day22.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 22:
                        day23.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 23:
                        day24.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 24:
                        day25.add( letterArray.get( i ) + " " + combination );
                        break;
                    case 25:
                        day26.add( letterArray.get( i ) + " " + combination );
                        break;
                    }
                }
            }

            /*Now write each list into a file*/
            System.out.println("Writing to files...");
            int drw_no=0;
            drw_no=Utils.getLatestDrawNo(brand_code);

            try {
                System.out.println("DRAW NO :"+drw_no);

                Collections.shuffle(day1);
                Utils.createFile(++drw_no, reOrderList(day1),brand_code); // re-order shuffled list to make sure no duplicate numbers within each 10 concecutive tickets.
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day2);
                Utils.createFile(++drw_no, reOrderList(day2),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day3);
                Utils.createFile(++drw_no, reOrderList(day3),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day4);
                Utils.createFile(++drw_no, reOrderList(day4),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day5);
                Utils.createFile(++drw_no, reOrderList(day5),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day6);
                Utils.createFile(++drw_no, reOrderList(day6),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day7);
                Utils.createFile(++drw_no, reOrderList(day7),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day8);
                Utils.createFile(++drw_no, reOrderList(day8),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day9);
                Utils.createFile(++drw_no, reOrderList(day9),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day10);
                Utils.createFile(++drw_no, reOrderList(day10),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day11);
                Utils.createFile(++drw_no, reOrderList(day11),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

                Collections.shuffle(day12);
                Utils.createFile(++drw_no, reOrderList(day12),brand_code);
                Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day13 );
            Utils.createFile( ++drw_no, reOrderList( day13 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day14 );
            Utils.createFile( ++drw_no, reOrderList( day14 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day15 );
            Utils.createFile( ++drw_no, reOrderList( day15 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day16 );
            Utils.createFile( ++drw_no, reOrderList( day16 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day17 );
            Utils.createFile( ++drw_no, reOrderList( day17 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day18 );
            Utils.createFile( ++drw_no, reOrderList( day18 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day19 );
            Utils.createFile( ++drw_no, reOrderList( day19 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day20 );
            Utils.createFile( ++drw_no, reOrderList( day20 ) ,brand_code);
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day21 );
            Utils.createFile( ++drw_no, reOrderList( day21 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day22 );
            Utils.createFile( ++drw_no, reOrderList( day22 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day23 );
            Utils.createFile( ++drw_no, reOrderList( day23 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day24 );
            Utils.createFile( ++drw_no, reOrderList( day24 ) ,brand_code);
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day25 );
            Utils.createFile( ++drw_no, reOrderList( day25 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);

            Collections.shuffle( day26 );
            Utils.createFile( ++drw_no, reOrderList( day26 ),brand_code );
            Utils.update_unique_Status(++latestDrawNo, unordered_count);


            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println("Writing completed!\n");
                System.out.println("Job Finished!!!");

                long end = System.currentTimeMillis();
                //finding the time difference and converting it into seconds
                float sec = (end - startTime) / 1000F;
                System.out.println("Total Time taken for the job : " + sec + " seconds");
                status= 1;
            }



        }
        return status;

    }

    /**
     * Re order list so that it groups each consecutive 10 entries (starting from first record) to have distinct values.
     * @param shuffledList
     * @return
     */
    public List<String> reOrderList(List<String> shuffledList) {
        unordered_count=0;
        List<String> orderedList = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        Set<String> numSet = new HashSet<>();

        for (int i = 0; i < shuffledList.size(); i++) {
            String record = shuffledList.get( i );
            //System.out.println("OUTPUT >>>> "+record);

            boolean anyValueExist = checkForExistence( record, numSet );
            if (anyValueExist) {
                continue;
            } else {
                temp.add( shuffledList.remove( i ) );
                if (temp.size() >= 10) {
                    orderedList.addAll( temp );
                    temp.clear();
                    numSet.clear();
                    i = -1; // start from the beginning again
                } else {
                    i = i - 1;
                }
            }
        }
        if (temp.size() > 0) {
            // If there are any partially grouped records, add them at the end of the re-ordered list
            orderedList.addAll( temp );
        }
        System.out.println( "Number of records which are unable to group: " + shuffledList.size() );
        unordered_count= shuffledList.size();
        // add remaining records (which are unable to group) at the end of the re-ordered list
        orderedList.addAll( shuffledList );
        return orderedList;
    }

    /**
     *
     * @param record Ex: "T 10, 20, 30, 40"
     * @param numSet Ex: [05,10,15,25,35,45,55,60]
     * @return TRUE if any number of the input record contains in the numSet; FALSE otherwise.
     */
    public boolean checkForExistence(String record, Set<String> numSet) {
        String[] array = record.trim().split( " " );

        //System.out.println("NUMS : "+numSet.toString());
        for (int i = 0; i < array.length; i++) {
            if (numSet.contains( array[i] ))
                return true;
        }
        // Add Four numbers to the Set
        numSet.add( array[0] );
        numSet.add( array[1] );
        numSet.add( array[2] );
        numSet.add( array[3] );
        numSet.add( array[4] );

        return false;


        /**
         String[] array = record.trim().split( " " );
         for (int i = 1; i < array.length; i++) {
         if (numSet.contains( array[i] ))
         return true;
         }
         // Add Four numbers to the Set
         numSet.add( array[1] );
         numSet.add( array[2] );
         numSet.add( array[3] );
         numSet.add( array[4] );

         return false;
         */
    }
}
